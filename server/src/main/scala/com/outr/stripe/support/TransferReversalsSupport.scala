package com.outr.stripe.support

import com.outr.stripe.transfer.{Transfer, TransferReversal}
import com.outr.stripe.{Implicits, Money, Pickler, QueryConfig, Stripe, StripeList, TimestampFilter}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TransferReversalsSupport(stripe: Stripe) extends Implicits {
  def create(transferId: String,
             amount: Option[Money] = None,
             description: Option[String] = None,
             metadata: Map[String, String] = Map.empty,
             refundApplicationFee: Boolean = false): Future[TransferReversal] = {
    val data = List(
      Some("amount" -> Pickler.write(amount)),
      description.map("description" -> _),
      if (metadata.nonEmpty) Some("metadata" -> Pickler.write[Map[String, String]](metadata)) else None,
      if (refundApplicationFee) Some("refund_application_fee" -> "true") else None
    ).flatten
    stripe.post(s"transfers/$transferId/reversals", QueryConfig.default, data: _*).map { response =>
      Pickler.read[TransferReversal](response.body)
    }
  }

  def byId(transferId: String, transferReversalId: String): Future[TransferReversal] = {
    stripe.get(s"transfers/$transferId/reversals/$transferReversalId", QueryConfig.default).map { response =>
      Pickler.read[TransferReversal](response.body)
    }
  }

  def update(transferId: String,
             transferReversalId: String,
             description: Option[String] = None,
             metadata: Map[String, String] = Map.empty): Future[TransferReversal] = {
    val data = List(
      description.map("description" -> _),
      if (metadata.nonEmpty) Some("metadata" -> Pickler.write[Map[String, String]](metadata)) else None
    ).flatten
    stripe.post(s"transfers/$transferId/reversals/$transferReversalId", QueryConfig.default, data: _*).map { response =>
      Pickler.read[TransferReversal](response.body)
    }
  }

  def list(transferId: String,
           config: QueryConfig = QueryConfig.default): Future[StripeList[TransferReversal]] = {
    stripe.get(s"transfers/$transferId/reversals", config).map { response =>
      Pickler.read[StripeList[TransferReversal]](response.body)
    }
  }
}
