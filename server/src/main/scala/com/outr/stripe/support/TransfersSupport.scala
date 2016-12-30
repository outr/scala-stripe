package com.outr.stripe.support

import com.outr.stripe.transfer.Transfer
import com.outr.stripe.{Implicits, Money, Pickler, QueryConfig, Stripe, StripeList, TimestampFilter}

import scala.concurrent.ExecutionContext.Implicits.global
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
             method: String = "standard"): Future[Transfer] = {
    val data = List(
      Some("amount" -> Pickler.write(amount)),
      Some("currency" -> currency),
      Some("destination" -> destination),
      applicationFee.map("application_fee" -> Pickler.write(_)),
      description.map("description" -> _),
      if (metadata.nonEmpty) Some("metadata" -> Pickler.write[Map[String, String]](metadata)) else None,
      sourceTransaction.map("source_transaction" -> _),
      statementDescriptor.map("statement_descriptor" -> _),
      if (sourceType != "card") Some("source_type" -> sourceType) else None,
      if (method != "standard") Some("method" -> method) else None
    ).flatten
    stripe.post("transfers", QueryConfig.default, data: _*).map { response =>
      Pickler.read[Transfer](response.body)
    }
  }

  def byId(transferId: String): Future[Transfer] = {
    stripe.get(s"transfers/$transferId", QueryConfig.default).map { response =>
      Pickler.read[Transfer](response.body)
    }
  }

  def update(transferId: String,
             description: Option[String] = None,
             metadata: Map[String, String] = Map.empty): Future[Transfer] = {
    val data = List(
      description.map("description" -> _),
      if (metadata.nonEmpty) Some("metadata" -> Pickler.write[Map[String, String]](metadata)) else None
    ).flatten
    stripe.post(s"transfers/$transferId", QueryConfig.default, data: _*).map { response =>
      Pickler.read[Transfer](response.body)
    }
  }

  def list(created: Option[TimestampFilter] = None,
           date: Option[TimestampFilter] = None,
           destination: Option[String] = None,
           recipient: Option[String] = None,
           status: Option[String] = None,
           config: QueryConfig = QueryConfig.default): Future[StripeList[Transfer]] = {
    val data = List(
      created.map("created" -> Pickler.write(_)),
      date.map("date" -> Pickler.write(_)),
      destination.map("destination" -> _),
      recipient.map("recipient" -> _),
      status.map("status" -> _)
    ).flatten
    stripe.get("transfers", config, data: _*).map { response =>
      Pickler.read[StripeList[Transfer]](response.body)
    }
  }
}
