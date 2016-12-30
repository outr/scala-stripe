package com.outr.stripe

import gigahorse.{Gigahorse, HttpVerbs, Realm, Response}

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

trait Restful {
  def apiKey: String

  protected def url(endPoint: String): String

  private[stripe] def get(endPoint: String,
                          config: QueryConfig,
                          data: (String, String)*): Future[Response] = {
    call(HttpVerbs.GET, endPoint = endPoint, config = config, data = data)
  }

  private[stripe] def post(endPoint: String,
                           config: QueryConfig,
                           data: (String, String)*): Future[Response] = {
    call(HttpVerbs.POST, endPoint = endPoint, config = config, data = data)
  }

  private[stripe] def delete(endPoint: String,
                             config: QueryConfig,
                             data: (String, String)*): Future[Response] = {
    call(HttpVerbs.DELETE, endPoint = endPoint, config = config, data = data)
  }

  private[stripe] def call(method: String,
                           endPoint: String,
                           config: QueryConfig,
                           data: Seq[(String, String)]): Future[Response] = {
    val client = Gigahorse.http(Gigahorse.config)
    try {
      val headers = ListBuffer.empty[(String, String)]
      headers += "Stripe-Version" -> Stripe.Version
      config.idempotencyKey.foreach(headers += "Idempotency-Key" -> _)

      val args = ListBuffer(data: _*)
      if (config.limit != QueryConfig.default.limit) args += "limit" -> config.limit.toString
      config.startingAfter.foreach(args += "starting_after" -> _)
      config.endingBefore.foreach(args += "ending_before" -> _)

      val request = Gigahorse.url(url(endPoint)).withAuth(Realm(apiKey, "")).addHeaders(headers: _*) match {
        case r if method == HttpVerbs.POST => r.post(data.map(t => t._1 -> List(t._2)).toMap)
        case r => r.withMethod(method).addQueryString(args: _*)
      }

      val future = client.run(request)
      future.onComplete { t =>
        client.close()
      }
      future
    } catch {
      case t: Throwable => {
        client.close()
        throw t
      }
    }
  }
}
