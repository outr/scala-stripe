package com.outr.stripe

import com.outr.scribe.Logging
import com.outr.stripe.balance._
import com.outr.stripe.charge.{Card, Charge, FraudDetails, Shipping}
import com.outr.stripe.customer.Customer
import com.outr.stripe.dispute.{Dispute, DisputeEvidence}
import gigahorse.{Gigahorse, HttpVerbs, Realm, Response}

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

    def byId(id: String, config: QueryConfig = QueryConfig.default): Future[BalanceTransaction] = {
      get(s"balance/history/$id", QueryConfig.default).map { response =>
        Pickler.read[BalanceTransaction](response.body)
      }
    }

    def list(availableOn: Option[TimestampFilter] = None,
             created: Option[TimestampFilter] = None,
             currency: Option[String] = None,
             source: Option[String] = None,
             transfer: Option[String] = None,
             `type`: Option[String] = None,
             config: QueryConfig = QueryConfig.default): Future[StripeList[BalanceTransaction]] = {
      val data = List(
        availableOn.map("available_on" -> Pickler.write[TimestampFilter](_)),
        created.map("created" -> Pickler.write[TimestampFilter](_)),
        currency.map("currency" -> _),
        source.map("source" -> _),
        transfer.map("transfer" -> _),
        `type`.map("type" -> _)
      ).flatten
      get("balance/history", config, data: _*).map { response =>
        Pickler.read[StripeList[BalanceTransaction]](response.body)
      }
    }
  }

  object charges {
    def create(amount: Money,
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

    def list(created: Option[TimestampFilter] = None,
             customer: Option[String] = None,
             source: Option[String] = None,
             config: QueryConfig = QueryConfig.default): Future[StripeList[Charge]] = {
      val data = List(
        created.map("created" -> Pickler.write[TimestampFilter](_)),
        customer.map("customer" -> _),
        source.map("source" -> _)
      ).flatten
      get("charges", config, data: _*).map { response =>
        Pickler.read[StripeList[Charge]](response.body)
      }
    }
  }

  object customers {
    def create(accountBalance: Option[Money] = None,
               businessVatId: Option[String] = None,
               coupon: Option[String] = None,
               description: Option[String] = None,
               email: Option[String] = None,
               metadata: Option[Map[String, String]] = None,
               plan: Option[String] = None,
               quantity: Option[Int] = None,
               shipping: Option[Shipping] = None,
               source: Option[Card] = None,
               taxPercent: Option[BigDecimal] = None,
               trialEnd: Option[Long] = None): Future[Customer] = {
      val data = List(
        accountBalance.map("account_balance" -> Pickler.write[Money](_)),
        businessVatId.map("business_vat_id" -> _),
        coupon.map("coupon" -> _),
        description.map("description" -> _),
        email.map("email" -> _),
        metadata.map("metadata" -> Pickler.write[Map[String, String]](_)),
        plan.map("plan" -> _),
        quantity.map("quantity" -> _.toString),
        shipping.map("shipping" -> Pickler.write[Shipping](_)),
        source.map("source" -> Pickler.write[Card](_)),
        taxPercent.map("tax_percent" -> _.toDouble.toString),
        trialEnd.map("trial_end" -> _.toString)
      ).flatten
      post("customers", QueryConfig.default, data: _*).map { response =>
        Pickler.read[Customer](response.body)
      }
    }

    def byId(customerId: String): Future[Customer] = {
      get(s"customers/$customerId", QueryConfig.default).map { response =>
        Pickler.read[Customer](response.body)
      }
    }

    def update(accountBalance: Option[Money] = None,
               businessVatId: Option[String] = None,
               coupon: Option[String] = None,
               defaultSource: Option[String] = None,
               description: Option[String] = None,
               email: Option[String] = None,
               metadata: Option[Map[String, String]] = None,
               shipping: Option[Shipping] = None,
               source: Option[Card] = None): Future[Customer] = {
      val data = List(
        accountBalance.map("account_balance" -> Pickler.write[Money](_)),
        businessVatId.map("business_vat_id" -> _),
        coupon.map("coupon" -> _),
        defaultSource.map("default_source" -> _),
        description.map("description" -> _),
        email.map("email" -> _),
        metadata.map("metadata" -> Pickler.write[Map[String, String]](_)),
        shipping.map("shipping" -> Pickler.write[Shipping](_)),
        source.map("source" -> Pickler.write[Card](_))
      ).flatten
      post("customers", QueryConfig.default, data: _*).map { response =>
        Pickler.read[Customer](response.body)
      }
    }

    def delete(customerId: String): Future[Deleted] = {
      Stripe.this.delete(s"customers/$customerId", QueryConfig.default).map { response =>
        Pickler.read[Deleted](response.body)
      }
    }

    def list(created: Option[TimestampFilter] = None,
             config: QueryConfig = QueryConfig.default): Future[StripeList[Customer]] = {
      val data = List(
        created.map("created" -> Pickler.write[TimestampFilter](_))
      ).flatten
      get("customers", config, data: _*).map { response =>
        Pickler.read[StripeList[Customer]](response.body)
      }
    }
  }

  object disputes {
    def byId(disputeId: String): Future[Dispute] = {
      get(s"disputes/$disputeId", QueryConfig.default).map { response =>
        Pickler.read[Dispute](response.body)
      }
    }

    def update(disputeId: String,
               evidence: Option[DisputeEvidence] = None,
               metadata: Option[Map[String, String]]): Future[Dispute] = {
      val data = List(
        evidence.map("evidence" -> Pickler.write[DisputeEvidence](_)),
        metadata.map("metadata" -> Pickler.write[Map[String, String]](_))
      ).flatten
      post(s"disputes/$disputeId", QueryConfig.default, data: _*).map { response =>
        Pickler.read[Dispute](response.body)
      }
    }

    def close(disputeId: String): Future[Dispute] = {
      post(s"disputes/$disputeId/close", QueryConfig.default).map { response =>
        Pickler.read[Dispute](response.body)
      }
    }

    def list(created: Option[TimestampFilter] = None,
             config: QueryConfig = QueryConfig.default): Future[StripeList[Dispute]] = {
      val data = List(
        created.map("created" -> Pickler.write[TimestampFilter](_))
      ).flatten
      get("disputes", config, data: _*).map { response =>
        Pickler.read[StripeList[Dispute]](response.body)
      }
    }
  }

  private def get(endPoint: String,
                  config: QueryConfig,
                  data: (String, String)*): Future[Response] = {
    call(HttpVerbs.GET, endPoint = endPoint, config = config, data = data)
  }

  private def post(endPoint: String,
                   config: QueryConfig,
                   data: (String, String)*): Future[Response] = {
    call(HttpVerbs.POST, endPoint = endPoint, config = config, data = data)
  }

  private def delete(endPoint: String,
                     config: QueryConfig,
                     data: (String, String)*): Future[Response] = {
    call(HttpVerbs.DELETE, endPoint = endPoint, config = config, data = data)
  }

  private def call(method: String,
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

object Stripe {
  val Version = "2016-07-06"
}