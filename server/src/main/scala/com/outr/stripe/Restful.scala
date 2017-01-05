package com.outr.stripe

import gigahorse.{Gigahorse, HttpVerbs, Realm, Response}
import io.circe.Decoder

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait Restful extends Implicits {
  def apiKey: String

  protected def url(endPoint: String): String

  private[stripe] def get[R](endPoint: String,
                             config: QueryConfig,
                             data: (String, String)*)
                            (implicit decoder: Decoder[R]): Future[Either[ResponseError, R]] = {
    process[R](HttpVerbs.GET, endPoint = endPoint, config = config, data = data, decoder = decoder)
  }

  private[stripe] def post[R](endPoint: String,
                              config: QueryConfig,
                              data: (String, String)*)
                             (implicit decoder: Decoder[R]): Future[Either[ResponseError, R]] = {
    process[R](HttpVerbs.POST, endPoint = endPoint, config = config, data = data, decoder = decoder)
  }

  private[stripe] def delete[R](endPoint: String,
                                config: QueryConfig,
                                data: (String, String)*)
                               (implicit decoder: Decoder[R]): Future[Either[ResponseError, R]] = {
    process[R](HttpVerbs.DELETE, endPoint = endPoint, config = config, data = data, decoder = decoder)
  }

  private[stripe] def process[R](method: String,
                                 endPoint: String,
                                 config: QueryConfig,
                                 data: Seq[(String, String)],
                                 decoder: Decoder[R]): Future[Either[ResponseError, R]] = {
    call(method, endPoint = endPoint, config = config, data = data).map { response =>
      if (response.status == 200) {
        Right(Pickler.read[R](response.body)(decoder))
      } else {
        val wrapper = Pickler.read[ErrorMessageWrapper](response.body)
        Left(ResponseError(response.statusText, response.status, wrapper.error))
      }
    }
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

      val future = client.process(request)
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

case class ResponseError(text: String, code: Int, error: ErrorMessage)

case class ErrorMessageWrapper(error: ErrorMessage)

case class ErrorMessage(message: String, `type`: String, param: String, code: String)