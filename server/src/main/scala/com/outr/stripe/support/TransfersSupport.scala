package com.outr.stripe.support

import com.outr.stripe.transfer.Transfer
import com.outr.stripe.{Implicits, Money, QueryConfig, ResponseError, Stripe, StripeList, TimestampFilter}

import scala.concurrent.Future

class TransfersSupport(stripe: Stripe) extends Implicits {
  def create(amount: Money,
             currency: String,
             destination: String,
             applicationFee: Option[Money] = None,
             description: Option[String] = None,
             metadata: Map[String, String] = Map.empty,
             sourceTransaction: Option[String] = None,
             statementDescriptor: Option[String] = None,
             sourceType: String = "card",
             method: String = "standard"): Future[Either[ResponseError, Transfer]] = {
    val data = List(
      write("amount", amount),
      write("currency", currency),
      write("destination", destination),
      write("application_fee", applicationFee),
      write("description", description),
      write("metadata", metadata),
      write("source_transaction", sourceTransaction),
      write("statement_descriptor", statementDescriptor),
      write("source_type", if (sourceType != "card") Some(sourceType) else None),
      write("method", if (method != "standard") Some(method) else None)
    ).flatten
    stripe.post[Transfer]("transfers", QueryConfig.default, data: _*)
  }

  def byId(transferId: String): Future[Either[ResponseError, Transfer]] = {
    stripe.get[Transfer](s"transfers/$transferId", QueryConfig.default)
  }

  def update(transferId: String,
             description: Option[String] = None,
             metadata: Map[String, String] = Map.empty): Future[Either[ResponseError, Transfer]] = {
    val data = List(
      write("description", description),
      write("metadata", metadata)
    ).flatten
    stripe.post[Transfer](s"transfers/$transferId", QueryConfig.default, data: _*)
  }

  def list(created: Option[TimestampFilter] = None,
           date: Option[TimestampFilter] = None,
           destination: Option[String] = None,
           recipient: Option[String] = None,
           status: Option[String] = None,
           config: QueryConfig = QueryConfig.default): Future[Either[ResponseError, StripeList[Transfer]]] = {
    val data = List(
      write("created", created),
      write("date", date),
      write("destination", destination),
      write("recipient", recipient),
      write("status", status)
    ).flatten
    stripe.get[StripeList[Transfer]]("transfers", config, data: _*)
  }

  lazy val reversals: TransferReversalsSupport = new TransferReversalsSupport(stripe)
}
