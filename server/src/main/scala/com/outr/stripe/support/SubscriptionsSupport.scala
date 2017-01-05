//package com.outr.stripe.support
//
//import com.outr.stripe.subscription.Subscription
//import com.outr.stripe.{Implicits, Pickler, QueryConfig, Stripe, StripeList, TimestampFilter}
//
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.Future
//
//class SubscriptionsSupport(stripe: Stripe) extends Implicits {
//  def create(customerId: String,
//             applicationFeePercent: Option[BigDecimal] = None,
//             coupon: Option[String] = None,
//             metadata: Map[String, String] = Map.empty,
//             plan: Option[String] = None,
//             prorate: Option[Boolean] = None,
//             quantity: Option[Int] = None,
//             source: Option[String] = None,
//             taxPercent: Option[BigDecimal] = None,
//             trialEnd: Option[Long] = None,
//             trialPeriodDays: Option[Int] = None): Future[Subscription] = {
//    val data = List(
//      Some("customer" -> customerId),
//      applicationFeePercent.map("application_fee_percent" -> Pickler.write(_)),
//      coupon.map("coupon" -> _),
//      if (metadata.nonEmpty) Some("metadata" -> Pickler.write(metadata)) else None,
//      plan.map("plan" -> _),
//      prorate.map("prorate" -> _.toString),
//      quantity.map("quantity" -> _.toString),
//      source.map("source" -> _),
//      taxPercent.map("tax_percent" -> Pickler.write(_)),
//      trialEnd.map("trial_end" -> _.toString),
//      trialPeriodDays.map("trial_period_days" -> _.toString)
//    ).flatten
//    stripe.post("subscriptions", QueryConfig.default, data: _*).map { response =>
//      Pickler.read[Subscription](response.body)
//    }
//  }
//
//  def byId(subscriptionId: String): Future[Subscription] = {
//    stripe.get(s"subscriptions/$subscriptionId", QueryConfig.default).map { response =>
//      Pickler.read[Subscription](response.body)
//    }
//  }
//
//  def update(subscriptionId: String,
//             applicationFeePercent: Option[BigDecimal] = None,
//             coupon: Option[String] = None,
//             metadata: Map[String, String] = Map.empty,
//             plan: Option[String] = None,
//             prorate: Option[Boolean] = None,
//             prorationDate: Option[Long] = None,
//             quantity: Option[Int] = None,
//             source: Option[String] = None,
//             taxPercent: Option[BigDecimal],
//             trialEnd: Option[Long] = None,
//             trialPeriodDays: Option[Int] = None): Future[Subscription] = {
//    val data = List(
//      applicationFeePercent.map("application_fee_percent" -> Pickler.write(_)),
//      coupon.map("coupon" -> _),
//      if (metadata.nonEmpty) Some("metadata" -> Pickler.write(metadata)) else None,
//      plan.map("plan" -> _),
//      prorate.map("prorate" -> _.toString),
//      quantity.map("quantity" -> _.toString),
//      source.map("source" -> _),
//      taxPercent.map("tax_percent" -> Pickler.write(_)),
//      trialEnd.map("trial_end" -> _.toString),
//      trialPeriodDays.map("trial_period_days" -> _.toString)
//    ).flatten
//    stripe.post(s"subscriptions/$subscriptionId", QueryConfig.default, data: _*).map { response =>
//      Pickler.read[Subscription](response.body)
//    }
//  }
//
//  def cancel(customerId: String,
//             subscriptionId: String,
//             atPeriodEnd: Boolean = false): Future[Subscription] = {
//    stripe.delete(s"customers/$customerId/subscriptions/$subscriptionId", QueryConfig.default, "at_period_end" -> atPeriodEnd.toString).map { response =>
//      Pickler.read[Subscription](response.body)
//    }
//  }
//
//  def list(created: Option[TimestampFilter] = None,
//           customer: Option[String] = None,
//           plan: Option[String] = None,
//           status: Option[String] = None,
//           config: QueryConfig = QueryConfig.default): Future[StripeList[Subscription]] = {
//    val data = List(
//      created.map("created" -> Pickler.write(_)),
//      customer.map("customer" -> _),
//      plan.map("plan" -> _),
//      status.map("status" -> _)
//    ).flatten
//    stripe.get("subscriptions", config, data: _*).map { response =>
//      Pickler.read[StripeList[Subscription]](response.body)
//    }
//  }
//}
