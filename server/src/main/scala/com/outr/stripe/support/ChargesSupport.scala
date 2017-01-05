package com.outr.stripe.support

import com.outr.stripe.charge.{Card, Charge, FraudDetails, Shipping}
import com.outr.stripe.{Implicits, Money, Pickler, QueryConfig, ResponseError, Stripe, StripeList, TimestampFilter}

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
             statementDescriptor: Option[String] = None): Future[Either[ResponseError, Charge]] = {
    val data = List(
      write("amount", amount),
      write("currency", currency),
      write("application_fee", applicationFee),
      write("capture", capture),
      write("description", description),
      write("destination", destination),
      write("metadata", metadata),
      write("receipt_email", receiptEmail),
      write("shipping", shipping),
      write("customer", customer),
      write("source", source),
      write("statement_descriptor", statementDescriptor)
    ).flatten
    stripe.post[Charge]("charges", QueryConfig.default, data: _*)
  }

  def byId(chargeId: String): Future[Either[ResponseError, Charge]] = {
    stripe.get[Charge](s"charges/$chargeId", QueryConfig.default)
  }

  def update(chargeId: String,
             description: Option[String] = None,
             fraudDetails: Option[FraudDetails] = None,
             metadata: Map[String, String] = Map.empty,
             receiptEmail: Option[String] = None,
             shipping: Option[Shipping] = None): Future[Either[ResponseError, Charge]] = {
    val data = List(
      write("description", description),
      write("fraud_details", fraudDetails),
      write("metadata", metadata),
      write("receipt_email", receiptEmail),
      write("shipping", shipping)
    ).flatten
    stripe.post[Charge](s"charges/$chargeId", QueryConfig.default, data: _*)
  }

  def capture(chargeId: String,
              amount: Option[Money] = None,
              applicationFee: Option[Money] = None,
              receiptEmail: Option[String] = None,
              statementDescriptor: Option[String] = None): Future[Either[ResponseError, Charge]] = {
    val data = List(
      write("amount", amount),
      write("application_fee", applicationFee),
      write("receipt_email", receiptEmail),
      write("statement_descriptor", statementDescriptor)
    ).flatten
    stripe.post[Charge](s"charges/$chargeId/capture", QueryConfig.default, data: _*)
  }

  def list(created: Option[TimestampFilter] = None,
           customer: Option[String] = None,
           source: Option[String] = None,
           config: QueryConfig = QueryConfig.default): Future[Either[ResponseError, StripeList[Charge]]] = {
    val data = List(
      write("created", created),
      write("customer", customer),
      write("source", source)
    ).flatten
    stripe.get[StripeList[Charge]]("charges", config, data: _*)
  }
}
