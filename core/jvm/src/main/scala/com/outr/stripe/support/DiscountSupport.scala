package com.outr.stripe.support

import com.outr.stripe.{Deleted, Implicits, QueryConfig, ResponseError, Stripe}

import scala.concurrent.Future

class DiscountSupport(stripe: Stripe) extends Implicits {
  def deleteCustomerDiscount(customerId: String): Future[Either[ResponseError, Deleted]] = {
    stripe.delete[Deleted](s"customers/$customerId/discount", QueryConfig.default)
  }

  def deleteSubscriptionDiscount(subscriptionId: String): Future[Either[ResponseError, Deleted]] = {
    stripe.delete[Deleted](s"subscriptions/$subscriptionId/discount", QueryConfig.default)
  }
}