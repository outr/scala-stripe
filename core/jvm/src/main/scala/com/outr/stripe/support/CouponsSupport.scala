package com.outr.stripe.support

import com.outr.stripe.subscription.Coupon
import com.outr.stripe.{Deleted, Implicits, Money, QueryConfig, ResponseError, Stripe, StripeList, TimestampFilter}

import scala.concurrent.Future

class CouponsSupport(stripe: Stripe) extends Implicits {
  def create(couponId: String,
             duration: String,
             amountOff: Option[Money] = None,
             currency: Option[String] = None,
             durationInMonths: Option[Int] = None,
             maxRedemptions: Option[Int] = None,
             metadata: Map[String, String] = Map.empty,
             percentOff: Option[Int] = None,
             redeemBy: Option[Long] = None): Future[Either[ResponseError, Coupon]] = {
    val data = List(
      write("id", couponId),
      write("duration", duration),
      write("amount_off", amountOff),
      write("currency", currency),
      write("duration_in_months", durationInMonths),
      write("max_redemptions", maxRedemptions),
      write("metadata", metadata),
      write("percent_off", percentOff),
      write("redeem_by", redeemBy)
    ).flatten
    stripe.post[Coupon]("coupons", QueryConfig.default, data: _*)
  }

  def byId(couponId: String): Future[Either[ResponseError, Coupon]] = {
    stripe.get[Coupon](s"coupons/$couponId", QueryConfig.default)
  }

  def update(couponId: String, metadata: Map[String, String]): Future[Either[ResponseError, Coupon]] = {
    val data = List(
      write("metadata", metadata)
    ).flatten
    stripe.post[Coupon](s"coupons/$couponId", QueryConfig.default, data: _*)
  }

  def delete(couponId: String): Future[Either[ResponseError, Deleted]] = {
    stripe.delete[Deleted](s"coupons/$couponId", QueryConfig.default)
  }

  def list(created: Option[TimestampFilter] = None,
           config: QueryConfig = QueryConfig.default): Future[Either[ResponseError, StripeList[Coupon]]] = {
    val data = List(
      write("created", created)
    ).flatten
    stripe.get[StripeList[Coupon]]("coupons", config, data: _*)
  }
}
