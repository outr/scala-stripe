package com.outr.stripe.support

import com.outr.stripe.refund.Refund
import com.outr.stripe.{Implicits, Money, Pickler, QueryConfig, ResponseError, Stripe, StripeList}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RefundsSupport(stripe: Stripe) extends Implicits {
  def create(chargeId: String,
             amount: Option[Money] = None,
             metadata: Map[String, String] = Map.empty,
             reason: Option[String] = None,
             refundApplicationFee: Boolean = false,
             reverseTransfer: Boolean = false): Future[Either[ResponseError, Refund]] = {
    val data = List(
      write("amount", amount),
      write("metadata", metadata),
      write("reason", reason),
      write("refund_application_fee", refundApplicationFee, false),
      write("reverse_transfer", reverseTransfer, false)
    ).flatten
    stripe.post[Refund]("refunds", QueryConfig.default, data: _*)
  }

  def byId(refundId: String): Future[Either[ResponseError, Refund]] = {
    stripe.get[Refund](s"refunds/$refundId", QueryConfig.default)
  }

  def update(refundId: String, metadata: Map[String, String] = Map.empty): Future[Either[ResponseError, Refund]] = {
    val data = List(
      write("metadata", metadata)
    ).flatten
    stripe.post[Refund](s"refunds/$refundId", QueryConfig.default, data: _*)
  }

  def list(chargeId: Option[String] = None,
           config: QueryConfig = QueryConfig.default): Future[Either[ResponseError, StripeList[Refund]]] = {
    val data = List(
      write("charge", chargeId)
    ).flatten
    stripe.get[StripeList[Refund]]("refunds", config, data: _*)
  }
}
