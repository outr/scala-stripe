package com.outr.stripe.support

import com.outr.stripe.subscription.{Invoice, InvoiceLine}
import com.outr.stripe.{Implicits, Money, Pickler, QueryConfig, Stripe, StripeList, TimestampFilter}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class InvoicesSupport(stripe: Stripe) extends Implicits {
  def create(customerId: String,
             applicationFee: Option[Money] = None,
             description: Option[String] = None,
             metadata: Map[String, String] = Map.empty,
             statementDescriptor: Option[String] = None,
             subscription: Option[String] = None,
             taxPercent: Option[BigDecimal] = None): Future[Invoice] = {
    val data = List(
      Some("customer" -> customerId),
      applicationFee.map("application_fee" -> _.pennies.toString),
      description.map("description" -> _),
      if (metadata.nonEmpty) Some("metadata" -> Pickler.write(metadata)) else None,
      statementDescriptor.map("statement_descriptor" -> _),
      subscription.map("subscription" -> _),
      taxPercent.map("tax_percent" -> Pickler.write(_))
    ).flatten
    stripe.post("invoices", QueryConfig.default, data: _*).map { response =>
      Pickler.read[Invoice](response.body)
    }
  }

  def byId(invoiceId: String): Future[Invoice] = {
    stripe.get(s"invoices/$invoiceId", QueryConfig.default).map { response =>
      Pickler.read[Invoice](response.body)
    }
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
                config: QueryConfig = QueryConfig.default): Future[StripeList[InvoiceLine]] = {
    val data = List(
      coupon.map("coupon" -> _),
      customer.map("customer" -> _),
      subscription.map("subscription" -> _),
      subscriptionPlan.map("subscription_plan" -> _),
      subscriptionProrate.map("subscription_prorate" -> _),
      subscriptionProrationDate.map("subscription_proration_date" -> _.toString),
      subscriptionQuantity.map("subscription_quantity" -> _.toString),
      subscriptionTrialEnd.map("subscription_trial_end" -> _.toString)
    ).flatten
    stripe.get(s"invoices/$invoiceId/lines", config, data: _*).map { response =>
      Pickler.read[StripeList[InvoiceLine]](response.body)
    }
  }

  def upcoming(customerId: String,
               coupon: Option[String] = None,
               subscription: Option[String] = None,
               subscriptionPlan: Option[String] = None,
               subscriptionProrate: Option[String] = None,
               subscriptionProrationDate: Option[Long] = None,
               subscriptionQuantity: Option[Int] = None,
               subscriptionTrialEnd: Option[Long] = None): Future[Invoice] = {
    val data = List(
      Some("customer" -> customerId),
      coupon.map("coupon" -> _),
      subscription.map("subscription" -> _),
      subscriptionPlan.map("subscription_plan" -> _),
      subscriptionProrate.map("subscription_prorate" -> _),
      subscriptionProrationDate.map("subscription_proration_date" -> _.toString),
      subscriptionQuantity.map("subscription_quantity" -> _.toString),
      subscriptionTrialEnd.map("subscription_trial_end" -> _.toString)
    ).flatten
    stripe.get(s"invoices/upcoming", QueryConfig.default, data: _*).map { response =>
      Pickler.read[Invoice](response.body)
    }
  }

  def update(invoiceId: String,
             applicationFee: Option[Money] = None,
             closed: Option[Boolean] = None,
             description: Option[String] = None,
             forgiven: Option[Boolean] = None,
             metadata: Map[String, String] = Map.empty,
             statementDescriptor: Option[String] = None,
             taxPercent: Option[BigDecimal] = None): Future[Invoice] = {
    val data = List(
      applicationFee.map("application_fee" -> _.pennies.toString),
      closed.map("closed" -> _.toString),
      description.map("description" -> _),
      forgiven.map("forgiven" -> _.toString),
      if (metadata.nonEmpty) Some("metadata" -> Pickler.write(metadata)) else None,
      statementDescriptor.map("statement_descriptor" -> _),
      taxPercent.map("tax_percent" -> Pickler.write(_))
    ).flatten
    stripe.post(s"invoices/$invoiceId", QueryConfig.default, data: _*).map { response =>
      Pickler.read[Invoice](response.body)
    }
  }

  def pay(invoiceId: String): Future[Invoice] = {
    stripe.post(s"invoices/$invoiceId/pay", QueryConfig.default).map { response =>
      Pickler.read[Invoice](response.body)
    }
  }

  def list(customerId: Option[String] = None,
           date: Option[TimestampFilter] = None,
           config: QueryConfig = QueryConfig.default): Future[StripeList[Invoice]] = {
    val data = List(
      date.map("date" -> Pickler.write(_))
    ).flatten
    stripe.get("invoices", config, data: _*).map { response =>
      Pickler.read[StripeList[Invoice]](response.body)
    }
  }
}
