//package com.outr.stripe.support
//
//import com.outr.stripe.{Deleted, Implicits, Pickler, QueryConfig, Stripe}
//
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.Future
//
//class DiscountSupport(stripe: Stripe) extends Implicits {
//  def deleteCustomerDiscount(customerId: String): Future[Deleted] = {
//    stripe.delete(s"customers/$customerId/discount", QueryConfig.default).map { response =>
//      Pickler.read[Deleted](response.body)
//    }
//  }
//
//  def deleteSubscriptionDiscount(subscriptionId: String): Future[Deleted] = {
//    stripe.delete(s"subscriptions/$subscriptionId/discount", QueryConfig.default).map { response =>
//      Pickler.read[Deleted](response.body)
//    }
//  }
//}