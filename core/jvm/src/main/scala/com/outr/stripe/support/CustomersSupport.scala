package com.outr.stripe.support

import com.outr.stripe.charge.{Card, Shipping}
import com.outr.stripe.customer.Customer
import com.outr.stripe.{Deleted, Implicits, Money, QueryConfig, ResponseError, Stripe, StripeList, TimestampFilter}

import scala.concurrent.Future

class CustomersSupport(stripe: Stripe) extends Implicits {
  def create(accountBalance: Option[Money] = None,
             businessVatId: Option[String] = None,
             coupon: Option[String] = None,
             description: Option[String] = None,
             email: Option[String] = None,
             metadata: Map[String, String] = Map.empty,
             plan: Option[String] = None,
             quantity: Option[Int] = None,
             shipping: Option[Shipping] = None,
             source: Option[Card] = None,
             taxPercent: Option[BigDecimal] = None,
             trialEnd: Option[Long] = None): Future[Either[ResponseError, Customer]] = {
    val data = List(
      write("account_balance", accountBalance),
      write("business_vat_id", businessVatId),
      write("coupon", coupon),
      write("description", description),
      write("email", email),
      write("metadata", metadata),
      write("plan", plan),
      write("quantity", quantity),
      write("shipping", shipping),
      write("source", source),
      write("tax_percent", taxPercent),
      write("trial_end", trialEnd)
    ).flatten
    stripe.post[Customer]("customers", QueryConfig.default, data: _*)
  }

  def byId(customerId: String): Future[Either[ResponseError, Customer]] = {
    stripe.get[Customer](s"customers/$customerId", QueryConfig.default)
  }

  def update(customerId: String,
             accountBalance: Option[Money] = None,
             businessVatId: Option[String] = None,
             coupon: Option[String] = None,
             defaultSource: Option[String] = None,
             description: Option[String] = None,
             email: Option[String] = None,
             metadata: Map[String, String] = Map.empty,
             shipping: Option[Shipping] = None,
             source: Option[Card] = None): Future[Either[ResponseError, Customer]] = {
    val data = List(
      write("account_balance", accountBalance),
      write("business_vat_id", businessVatId),
      write("coupon", coupon),
      write("description", description),
      write("email", email),
      write("metadata", metadata),
      write("shipping", shipping),
      write("source", source),
      write("default_source", defaultSource)
    ).flatten
    stripe.post[Customer](s"customers/$customerId", QueryConfig.default, data: _*)
  }

  def delete(customerId: String): Future[Either[ResponseError, Deleted]] = {
    stripe.delete[Deleted](s"customers/$customerId", QueryConfig.default)
  }

  def list(created: Option[TimestampFilter] = None,
           config: QueryConfig = QueryConfig.default,
           email: Option[String] = None): Future[Either[ResponseError, StripeList[Customer]]] = {
    val data = List(
      write("created", created),
      write("email", email)
    ).flatten
    stripe.get[StripeList[Customer]]("customers", config, data: _*)
  }

  object sources {
    lazy val bankAccounts: CustomerBankAccountsSupport = new CustomerBankAccountsSupport(stripe)
    lazy val cards: CustomerCreditCardsSupport = new CustomerCreditCardsSupport(stripe)
  }
}
