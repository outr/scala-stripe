package com.outr.stripe.support

import com.outr.stripe.charge.{Card, Charge, FraudDetails, Shipping}
import com.outr.stripe.{Implicits, Money, Pickler, QueryConfig, Stripe, StripeList, TimestampFilter}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class ChargesSupport(stripe: Stripe) extends Implicits {
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
    stripe.post("charges", QueryConfig.default, data: _*).map { response =>
      Pickler.read[Charge](response.body)
    }
  }

  def byId(chargeId: String): Future[Charge] = {
    stripe.get(s"charges/$chargeId", QueryConfig.default).map { response =>
      Pickler.read[Charge](response.body)
    }
  }

  def update(chargeId: String,
             description: Option[String] = None,
             fraudDetails: Option[FraudDetails] = None,
             metadata: Map[String, String] = Map.empty,
             receiptEmail: Option[String] = None,
             shipping: Option[Shipping] = None): Future[Charge] = {
    val data = List(
      description.map("description" -> _),
      fraudDetails.map("fraud_details" -> Pickler.write[FraudDetails](_)),
      if (metadata.nonEmpty) Some("metadata" -> Pickler.write[Map[String, String]](metadata)) else None,
      receiptEmail.map("receipt_email" -> _),
      shipping.map("shipping" -> Pickler.write[Shipping](_))
    ).flatten
    stripe.post(s"charges/$chargeId", QueryConfig.default, data: _*).map { response =>
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
    stripe.post(s"charges/$chargeId/capture", QueryConfig.default, data: _*).map { response =>
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
    stripe.get("charges", config, data: _*).map { response =>
      Pickler.read[StripeList[Charge]](response.body)
    }
  }
}
