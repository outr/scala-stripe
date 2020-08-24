package com.outr.stripe.support

import com.outr.stripe._
import com.outr.stripe.subscription.SubscriptionItem

import scala.concurrent.Future

class SubscriptionItemsSupport(stripe: Stripe) extends Implicits {
  def create(subscriptionId: String,
             billingThresholds: Map[String, String] = Map(),
             metadata: Map[String, String] = Map(),
             paymentBehavior: Option[String] = None,
             priceId: Option[String] = None,
             prorationBehavior: Option[String] = None,
             prorationDate: Option[Long] = None,
             quantity: Option[Int] = None,
             taxRates: List[String] = List()): Future[Either[ResponseError, SubscriptionItem]] = {
    val data = List(
      write("subscription", subscriptionId),
      write("billing_thresholds", billingThresholds),
      write("metadata", metadata),
      write("payment_behavior", paymentBehavior),
      write("price", priceId),
      write("proration_behavior", prorationBehavior),
      write("proration_date", prorationDate),
      write("quantity", quantity),
      write("tax_rates", taxRates)
    ).flatten
    stripe.post[SubscriptionItem]("subscription_items", QueryConfig.default, data: _*)
  }

  def byId(subscriptionItemId: String): Future[Either[ResponseError, SubscriptionItem]] = {
    stripe.get[SubscriptionItem](s"subscription_items/$subscriptionItemId", QueryConfig.default)
  }

  def update(subscriptionItemId: String,
             billingThresholds: Map[String, String] = Map(),
             metadata: Map[String, String] = Map(),
             offSession: Option[Boolean] = None,
             paymentBehavior: Option[String] = None,
             priceId: Option[String] = None,
             prorationBehavior: Option[String] = None,
             prorationDate: Option[Long] = None,
             quantity: Option[Int] = None,
             taxRates: List[String] = List()): Future[Either[ResponseError, SubscriptionItem]] = {
    val data = List(
      write("billing_thresholds", billingThresholds),
      write("metadata", metadata),
      write("off_session", offSession),
      write("payment_behavior", paymentBehavior),
      write("price", priceId),
      write("proration_behavior", prorationBehavior),
      write("proration_date", prorationDate),
      write("quantity", quantity),
      write("tax_rates", taxRates)
    ).flatten
    stripe.post[SubscriptionItem](s"subscription_items/$subscriptionItemId", QueryConfig.default, data: _*)
  }

  def delete(subscriptionItemId: String): Future[Either[ResponseError, Deleted]] = {
    stripe.delete[Deleted](s"subscription_items/$subscriptionItemId", QueryConfig.default)
  }

  def list(subscription: String,
           config: QueryConfig = QueryConfig.default): Future[Either[ResponseError, StripeList[SubscriptionItem]]] = {
    val data = List(
      write("subscription", subscription)
    ).flatten
    stripe.get[StripeList[SubscriptionItem]]("subscription_items", config, data: _*)
  }
}
