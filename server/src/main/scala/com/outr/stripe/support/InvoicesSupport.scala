package com.outr.stripe.support

import com.outr.stripe.subscription.{Invoice, InvoiceLine}
import com.outr.stripe.{Implicits, Money, Pickler, QueryConfig, ResponseError, Stripe, StripeList, TimestampFilter}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class InvoicesSupport(stripe: Stripe) extends Implicits {
  def create(customerId: String,
             applicationFee: Option[Money] = None,
             description: Option[String] = None,
             metadata: Map[String, String] = Map.empty,
             statementDescriptor: Option[String] = None,
             subscription: Option[String] = None,
             taxPercent: Option[BigDecimal] = None): Future[Either[ResponseError, Invoice]] = {
    val data = List(
      write("customer", customerId),
      write("application_fee", applicationFee),
      write("description", description),
      write("metadata", metadata),
      write("statement_descriptor", statementDescriptor),
      write("subscription", subscription),
      write("tax_percent", taxPercent)
    ).flatten
    stripe.post[Invoice]("invoices", QueryConfig.default, data: _*)
  }

  def byId(invoiceId: String): Future[Either[ResponseError, Invoice]] = {
    stripe.get[Invoice](s"invoices/$invoiceId", QueryConfig.default)
  }

  def linesById(invoiceId: String,
                coupon: Option[String] = None,
                customer: Option[String] = None,
                subscription: Option[String] = None,
                subscriptionPlan: Option[String] = None,
                subscriptionProrate: Option[String] = None,
                subscriptionProrationDate: Option[Long] = None,
                subscriptionQuantity: Option[Int] = None,
                subscriptionTrialEnd: Option[Long] = None,
                config: QueryConfig = QueryConfig.default): Future[Either[ResponseError, StripeList[InvoiceLine]]] = {
    val data = List(
      write("coupon", coupon),
      write("customer", customer),
      write("subscription", subscription),
      write("subscription_plan", subscriptionPlan),
      write("subscription_prorate", subscriptionProrate),
      write("subscription_proration_date", subscriptionProrationDate),
      write("subscription_quantity", subscriptionQuantity),
      write("subscription_trial_end", subscriptionTrialEnd)
    ).flatten
    stripe.get[StripeList[InvoiceLine]](s"invoices/$invoiceId/lines", config, data: _*)
  }

  def upcoming(customerId: String,
               coupon: Option[String] = None,
               subscription: Option[String] = None,
               subscriptionPlan: Option[String] = None,
               subscriptionProrate: Option[String] = None,
               subscriptionProrationDate: Option[Long] = None,
               subscriptionQuantity: Option[Int] = None,
               subscriptionTrialEnd: Option[Long] = None): Future[Either[ResponseError, Invoice]] = {
    val data = List(
      write("coupon", coupon),
      write("customer", customerId),
      write("subscription", subscription),
      write("subscription_plan", subscriptionPlan),
      write("subscription_prorate", subscriptionProrate),
      write("subscription_proration_date", subscriptionProrationDate),
      write("subscription_quantity", subscriptionQuantity),
      write("subscription_trial_end", subscriptionTrialEnd)
    ).flatten
    stripe.get[Invoice](s"invoices/upcoming", QueryConfig.default, data: _*)
  }

  def update(invoiceId: String,
             applicationFee: Option[Money] = None,
             closed: Option[Boolean] = None,
             description: Option[String] = None,
             forgiven: Option[Boolean] = None,
             metadata: Map[String, String] = Map.empty,
             statementDescriptor: Option[String] = None,
             taxPercent: Option[BigDecimal] = None): Future[Either[ResponseError, Invoice]] = {
    val data = List(
      write("application_fee", applicationFee),
      write("closed", closed),
      write("description", description),
      write("forgiven", forgiven),
      write("metadata", metadata),
      write("statement_descriptor", statementDescriptor),
      write("tax_percent", taxPercent)
    ).flatten
    stripe.post[Invoice](s"invoices/$invoiceId", QueryConfig.default, data: _*)
  }

  def pay(invoiceId: String): Future[Either[ResponseError, Invoice]] = {
    stripe.post[Invoice](s"invoices/$invoiceId/pay", QueryConfig.default)
  }

  def list(customerId: Option[String] = None,
           date: Option[TimestampFilter] = None,
           config: QueryConfig = QueryConfig.default): Future[Either[ResponseError, StripeList[Invoice]]] = {
    val data = List(
      write("date", date)
    ).flatten
    stripe.get[StripeList[Invoice]]("invoices", config, data: _*)
  }
}
