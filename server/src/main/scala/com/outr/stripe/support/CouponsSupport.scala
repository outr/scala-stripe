//package com.outr.stripe.support
//
//import com.outr.stripe.charge.{BankAccount, Card, PII}
//import com.outr.stripe.subscription.Coupon
//import com.outr.stripe.token.Token
//import com.outr.stripe.{Deleted, Implicits, Money, Pickler, QueryConfig, Stripe, StripeList, TimestampFilter}
//
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.Future
//
//class CouponsSupport(stripe: Stripe) extends Implicits {
//  def create(couponId: String,
//             duration: String,
//             amountOff: Option[Money] = None,
//             currency: Option[String] = None,
//             durationInMonths: Option[Int] = None,
//             maxRedemptions: Option[Int] = None,
//             metadata: Map[String, String] = Map.empty,
//             percentOff: Option[Int] = None,
//             redeemBy: Option[Long] = None): Future[Coupon] = {
//    val data = List(
//      Some("id" -> couponId),
//      Some("duration" -> duration),
//      amountOff.map("amount_off" -> _.pennies.toString),
//      currency.map("currency" -> _),
//      durationInMonths.map("duration_in_months" -> _.toString),
//      maxRedemptions.map("max_redemptions" -> _.toString),
//      if (metadata.nonEmpty) Some("metadata" -> Pickler.write(metadata)) else None,
//      percentOff.map("percent_off" -> _.toString),
//      redeemBy.map("redeem_by" -> _.toString)
//    ).flatten
//    stripe.post("coupons", QueryConfig.default, data: _*).map { response =>
//      Pickler.read[Coupon](response.body)
//    }
//  }
//
//  def byId(couponId: String): Future[Coupon] = {
//    stripe.get(s"coupons/$couponId", QueryConfig.default).map { response =>
//      Pickler.read[Coupon](response.body)
//    }
//  }
//
//  def update(couponId: String, metadata: Map[String, String]): Future[Coupon] = {
//    val data = List(
//      "metadata" -> Pickler.write(metadata)
//    )
//    stripe.post(s"coupons/$couponId", QueryConfig.default, data: _*).map { response =>
//      Pickler.read[Coupon](response.body)
//    }
//  }
//
//  def delete(couponId: String): Future[Deleted] = {
//    stripe.delete(s"coupons/$couponId", QueryConfig.default).map { response =>
//      Pickler.read[Deleted](response.body)
//    }
//  }
//
//  def list(created: Option[TimestampFilter] = None,
//           config: QueryConfig = QueryConfig.default): Future[StripeList[Coupon]] = {
//    val data = List(
//      created.map("created" -> Pickler.write(_))
//    ).flatten
//    stripe.get("coupons", config, data: _*).map { response =>
//      Pickler.read[StripeList[Coupon]](response.body)
//    }
//  }
//}
