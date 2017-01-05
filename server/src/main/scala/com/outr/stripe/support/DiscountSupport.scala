package com.outr.stripe.support

import com.outr.stripe.{Deleted, Implicits, Pickler, QueryConfig, ResponseError, Stripe}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DiscountSupport(stripe: Stripe) extends Implicits {
  def deleteCustomerDiscount(customerId: String): Future[Either[ResponseError, Deleted]] = {
    stripe.delete[Deleted](s"customers/$customerId/discount", QueryConfig.default)
  }

  def deleteSubscriptionDiscount(subscriptionId: String): Future[Either[ResponseError, Deleted]] = {
    stripe.delete[Deleted](s"subscriptions/$subscriptionId/discount", QueryConfig.default)
  }
}