package com.outr.stripe.support

import com.outr.stripe.subscription.Subscription
import com.outr.stripe.{Implicits, QueryConfig, ResponseError, Stripe, StripeList, TimestampFilter}

import scala.concurrent.Future

class SubscriptionsSupport(stripe: Stripe) extends Implicits {
  def create(customerId: String,
             items: List[Map[String, String]],
             applicationFeePercent: Option[BigDecimal] = None,
             coupon: Option[String] = None,
             metadata: Map[String, String] = Map.empty,
             prorate: Option[Boolean] = None,
             quantity: Option[Int] = None,
             source: Option[String] = None,
             taxPercent: Option[BigDecimal] = None,
             trialEnd: Option[Long] = None,
             trialPeriodDays: Option[Int] = None): Future[Either[ResponseError, Subscription]] = {
    val data = List(
      write("customer", customerId),
      write("items", items),
      write("application_fee_percent", applicationFeePercent),
      write("coupon", coupon),
      write("metadata", metadata),
      write("prorate", prorate),
      write("quantity", quantity),
      write("source", source),
      write("tax_percent", taxPercent),
      write("trial_end", trialEnd),
      write("trial_period_days", trialPeriodDays)
    ).flatten
    stripe.post[Subscription]("subscriptions", QueryConfig.default, data: _*)
  }

  def byId(subscriptionId: String): Future[Either[ResponseError, Subscription]] = {
    stripe.get[Subscription](s"subscriptions/$subscriptionId", QueryConfig.default)
  }

  def update(subscriptionId: String,
             items: Option[List[Map[String, String]]] = None,
             applicationFeePercent: Option[BigDecimal] = None,
             coupon: Option[String] = None,
             metadata: Map[String, String] = Map.empty,
             prorate: Option[Boolean] = None,
             prorationDate: Option[Long] = None,
             quantity: Option[Int] = None,
             source: Option[String] = None,
             taxPercent: Option[BigDecimal],
             trialEnd: Option[Long] = None,
             trialPeriodDays: Option[Int] = None): Future[Either[ResponseError, Subscription]] = {
    val data = List(
      write("application_fee_percent", applicationFeePercent),
      write("coupon", coupon),
      write("metadata", metadata),
      write("items", items),
      write("prorate", prorate),
      write("quantity", quantity),
      write("source", source),
      write("tax_percent", taxPercent),
      write("trial_end", trialEnd),
      write("trial_period_days", trialPeriodDays)
    ).flatten
    stripe.post[Subscription](s"subscriptions/$subscriptionId", QueryConfig.default, data: _*)
  }

  def cancel(customerId: String,
             subscriptionId: String,
             atPeriodEnd: Boolean = false): Future[Either[ResponseError, Subscription]] = {
    stripe.delete[Subscription](s"customers/$customerId/subscriptions/$subscriptionId", QueryConfig.default, "at_period_end" -> atPeriodEnd.toString)
  }

  def list(created: Option[TimestampFilter] = None,
           customer: Option[String] = None,
           plan: Option[String] = None,
           status: Option[String] = None,
           config: QueryConfig = QueryConfig.default): Future[Either[ResponseError, StripeList[Subscription]]] = {
    val data = List(
      write("created", created),
      write("customer", customer),
      write("plan", plan),
      write("status", status)
    ).flatten
    stripe.get[StripeList[Subscription]]("subscriptions", config, data: _*)
  }
}
