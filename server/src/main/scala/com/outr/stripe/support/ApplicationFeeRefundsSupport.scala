package com.outr.stripe.support

import com.outr.stripe.connect.FeeRefund
import com.outr.stripe.{Implicits, Money, QueryConfig, ResponseError, Stripe, StripeList}

import scala.concurrent.Future

class ApplicationFeeRefundsSupport(stripe: Stripe) extends Implicits {
  def create(feeId: String,
             amount: Option[Money] = None,
             metadata: Map[String, String] = Map.empty): Future[Either[ResponseError, FeeRefund]] = {
    val data = List(
      write("amount", amount),
      write("metadata", metadata)
    ).flatten
    stripe.post[FeeRefund](s"application_fees/$feeId/refunds", QueryConfig.default, data: _*)
  }

  def byId(feeId: String, refundId: String): Future[Either[ResponseError, FeeRefund]] = {
    stripe.get[FeeRefund](s"/application_fees/$feeId/refunds/$refundId", QueryConfig.default)
  }

  def update(feeId: String, refundId: String, metadata: Map[String, String] = Map.empty): Future[Either[ResponseError, FeeRefund]] = {
    val data = List(
      write("metadata", metadata)
    ).flatten
    stripe.post[FeeRefund](s"application_fees/$feeId/refunds/$refundId", QueryConfig.default, data: _*)
  }

  def list(feeId: String, config: QueryConfig = QueryConfig.default): Future[Either[ResponseError, StripeList[FeeRefund]]] = {
    stripe.get[StripeList[FeeRefund]](s"application_fees/$feeId/refunds", config)
  }
}