package com.outr.stripe

import com.outr.scribe.Logging
import com.outr.stripe.balance._
import com.outr.stripe.charge.{Card, Charge, FraudDetails, Shipping}
import gigahorse.{Gigahorse, Realm, Response}

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import io.circe.generic.auto._

class Stripe(apiKey: String) extends Implicits with Logging {
  private val baseURL = "https://api.stripe.com/v1"
  private def url(endPoint: String): String = s"$baseURL/$endPoint"

  object balance {
    def apply(): Future[Balance] = get("balance", QueryConfig.default).map { response =>
      Pickler.read[Balance](response.body)
    }

    def historyById(id: String, config: QueryConfig = QueryConfig.default): Future[BalanceTransaction] = {
      get(s"balance/history/$id", QueryConfig.default).map { response =>
        Pickler.read[BalanceTransaction](response.body)
      }
    }

    def history(config: QueryConfig = QueryConfig.default): Future[StripeList[BalanceTransaction]] = {
      get("balance/history", config).map { response =>
        Pickler.read[StripeList[BalanceTransaction]](response.body)
      }
    }
  }

  object charges {
    def apply(amount: Money,
              currency: String,
              applicationFee: Option[Money] = None,
              capture: Boolean = true,
              description: Option[String] = None,
              destination: Option[String] = None,
              metadata: Map[String, String] = Map.empty,
              receiptEmail: Option[String] = None,
              shipping: Option[Shipping] = None,
              customer: Option[String] = None,
              source: Option[Card] = None,
              statementDescriptor: Option[String] = None): Future[Charge] = {
      val data = List(
        Some("amount" -> Pickler.write[Money](amount)),
        Some("currency" -> currency),
        applicationFee.map("application_fee" -> Pickler.write[Money](_)),
        Some("capture" -> capture.toString),
        description.map("description" -> _),
        destination.map("destination" -> _),
        if (metadata.nonEmpty) Some("metadata" -> Pickler.write[Map[String, String]](metadata)) else None,
        receiptEmail.map("receipt_email" -> _),
        shipping.map("shipping" -> Pickler.write[Shipping](_)),
        customer.map("customer" -> _),
        source.map("source" -> Pickler.write[Card](_)),
        statementDescriptor.map("statement_descriptor" -> _)
      ).flatten
      post("charges", QueryConfig.default, data: _*).map { response =>
        Pickler.read[Charge](response.body)
      }
    }

    def byId(chargeId: String): Future[Charge] = {
      get(s"charges/$chargeId", QueryConfig.default).map { response =>
        Pickler.read[Charge](response.body)
      }
    }

    def update(chargeId: String,
               description: Option[String] = None,
               fraudDetails: Option[FraudDetails] = None,
               metadata: Option[Map[String, String]] = None,
               receiptEmail: Option[String] = None,
               shipping: Option[Shipping] = None): Future[Charge] = {
      val data = List(
        description.map("description" -> _),
        fraudDetails.map("fraud_details" -> Pickler.write[FraudDetails](_)),
        metadata.map("metadata" -> Pickler.write[Map[String, String]](_)),
        receiptEmail.map("receipt_email" -> _),
        shipping.map("shipping" -> Pickler.write[Shipping](_))
      ).flatten
      post(s"charges/$chargeId", QueryConfig.default, data: _*).map { response =>
        Pickler.read[Charge](response.body)
      }
    }

    def capture(chargeId: String,
                amount: Option[Money] = None,
                applicationFee: Option[Money] = None,
                receiptEmail: Option[String] = None,
                statementDescriptor: Option[String] = None): Future[Charge] = {
      val data = List(
        amount.map("amount" -> Pickler.write[Money](_)),
        applicationFee.map("application_fee" -> Pickler.write[Money](_)),
        receiptEmail.map("receipt_email" -> _),
        statementDescriptor.map("statement_descriptor" -> _)
      ).flatten
      post(s"charges/$chargeId/capture", QueryConfig.default, data: _*).map { response =>
        Pickler.read[Charge](response.body)
      }
    }

    def list(created: Option[String] = None,
             customer: Option[String] = None,
             source: Option[String] = None,
             config: QueryConfig = QueryConfig.default): Future[StripeList[Charge]] = {
      val data = List(
        created.map("created" -> _),
        customer.map("customer" -> _),
        source.map("source" -> _)
      ).flatten
      get("charges", config, data: _*).map { response =>
        Pickler.read[StripeList[Charge]](response.body)
      }
    }
  }

  private def get(endPoint: String,
                  config: QueryConfig,
                  data: (String, String)*): Future[Response] = {
    call(isPost = false, endPoint = endPoint, config = config, data = data)
  }

  private def post(endPoint: String,
                   config: QueryConfig,
                   data: (String, String)*): Future[Response] = {
    call(isPost = true, endPoint = endPoint, config = config, data = data)
  }

  private def call(isPost: Boolean,
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
        case r if isPost => r.post(data.map(t => t._1 -> List(t._2)).toMap)
        case r => r.get.addQueryString(args: _*)
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

object Stripe {
  val Version = "2016-07-06"
}