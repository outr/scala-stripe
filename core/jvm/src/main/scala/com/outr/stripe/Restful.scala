package com.outr.stripe

import io.circe.Decoder
import io.youi.client.HttpClient
import io.youi.http.content.{Content, StringContent}
import io.youi.http.{Headers, HttpResponse, HttpStatus, HttpMethod}
import io.youi.net.{ContentType, Parameters, URL}

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait Restful extends Implicits {
  def apiKey: String

  protected def url(endPoint: String): URL

  private[stripe] def get[R](endPoint: String,
                             config: QueryConfig,
                             data: (String, String)*)
                            (implicit decoder: Decoder[R], manifest: Manifest[R]): Future[Either[ResponseError, R]] = {
    process[R](HttpMethod.Get, endPoint = endPoint, config = config, data = data)
  }

  private[stripe] def post[R](endPoint: String,
                              config: QueryConfig,
                              data: (String, String)*)
                             (implicit decoder: Decoder[R], manifest: Manifest[R]): Future[Either[ResponseError, R]] = {
    process[R](HttpMethod.Post, endPoint = endPoint, config = config, data = data)
  }

  private[stripe] def delete[R](endPoint: String,
                                config: QueryConfig,
                                data: (String, String)*)
                               (implicit decoder: Decoder[R], manifest: Manifest[R]): Future[Either[ResponseError, R]] = {
    process[R](HttpMethod.Delete, endPoint = endPoint, config = config, data = data)
  }

  private[stripe] def process[R](method: HttpMethod,
                                 endPoint: String,
                                 config: QueryConfig,
                                 data: Seq[(String, String)])
                                (implicit decoder: Decoder[R], manifest: Manifest[R]): Future[Either[ResponseError, R]] = {
    call(method, endPoint = endPoint, config = config, data = data).map { response =>
      if (response.status == HttpStatus.OK) {
        Right(Pickler.read[R](response.content.get.asInstanceOf[StringContent].value))
      } else {
        val wrapper = Pickler.read[ErrorMessageWrapper](response.content.get.asInstanceOf[StringContent].value)
        Left(ResponseError(response.status.message, response.status.code, wrapper.error))
      }
    }
  }

  private lazy val client = HttpClient
  private lazy val authorization = s"Bearer $apiKey"

  private[stripe] def call(method: HttpMethod,
                           endPoint: String,
                           config: QueryConfig,
                           data: Seq[(String, String)]): Future[HttpResponse] = {
    var headers = Headers.empty
    headers = headers.withHeader("Stripe-Version", Stripe.Version)
    config.idempotencyKey.foreach(value => headers = headers.withHeader("Idempotency-Key", value))
    headers = headers.withHeader(Headers.Request.Authorization(authorization))

    val args = ListBuffer(data: _*)
    if (config.limit != QueryConfig.default.limit) args += "limit" -> config.limit.toString
    config.startingAfter.foreach(args += "starting_after" -> _)
    config.endingBefore.foreach(args += "ending_before" -> _)

    var url = this.url(endPoint)
    args.foreach {
      case (key, value) => url = url.withParam(key, value)
    }
    val content = if (method == HttpMethod.Post) {
      val params = url.parameters.encoded.substring(1)
      url = url.copy(parameters = Parameters.empty)
      Some(Content.string(params, ContentType.`application/x-www-form-urlencoded`))
    } else {
      None
    }
    client
      .method(method)
      .url(url)
      .headers(headers)
      .content(content)
      .send()
  }

  def dispose(): Unit = {}
}

case class ResponseError(text: String, code: Int, error: ErrorMessage)

case class ErrorMessageWrapper(error: ErrorMessage)

case class ErrorMessage(message: String, `type`: String, param: Option[String], code: Option[String])