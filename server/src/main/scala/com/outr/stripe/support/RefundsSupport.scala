package com.outr.stripe.support

import com.outr.stripe.refund.Refund
import com.outr.stripe.{Implicits, Money, Pickler, QueryConfig, Stripe, StripeList}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RefundsSupport(stripe: Stripe) extends Implicits {
  def create(chargeId: String,
             amount: Option[Money] = None,
             metadata: Map[String, String] = Map.empty,
             reason: Option[String] = None,
             refundApplicationFee: Boolean = false,
             reverseTransfer: Boolean = false): Future[Refund] = {
    val data = List(
      amount.map("amount" -> Pickler.write[Money](_)),
      if (metadata.nonEmpty) Some("metadata" -> Pickler.write[Map[String, String]](metadata)) else None,
      reason.map("reason" -> _),
      if (refundApplicationFee) Some("refund_application_fee" -> "true") else None,
      if (reverseTransfer) Some("reverse_transfer" -> "true") else None
    ).flatten
    stripe.post("refunds", QueryConfig.default, data: _*).map { response =>
      Pickler.read[Refund](response.body)
    }
  }

  def byId(refundId: String): Future[Refund] = {
    stripe.get(s"refunds/$refundId", QueryConfig.default).map { response =>
      Pickler.read[Refund](response.body)
    }
  }

  def update(refundId: String, metadata: Map[String, String] = Map.empty): Future[Refund] = {
    val data = List(
      if (metadata.nonEmpty) Some("metadata" -> Pickler.write[Map[String, String]](metadata)) else None
    ).flatten
    stripe.post(s"refunds/$refundId", QueryConfig.default, data: _*).map { response =>
      Pickler.read[Refund](response.body)
    }
  }

  def list(chargeId: Option[String] = None,
           config: QueryConfig = QueryConfig.default): Future[StripeList[Refund]] = {
    val data = List(
      chargeId.map("charge" -> _)
    ).flatten
    stripe.get("refunds", config, data: _*).map { response =>
      Pickler.read[StripeList[Refund]](response.body)
    }
  }
}
