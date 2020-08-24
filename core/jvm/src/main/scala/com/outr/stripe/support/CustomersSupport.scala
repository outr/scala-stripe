package com.outr.stripe.support

import com.outr.stripe.charge.{Address, Card, Shipping}
import com.outr.stripe.customer.Customer
import com.outr.stripe.{Deleted, Implicits, Money, QueryConfig, ResponseError, Stripe, StripeList, TimestampFilter}

import scala.concurrent.Future

class CustomersSupport(stripe: Stripe) extends Implicits {
  def create(address: Option[Address] = None,
             balance: Option[Money] = None,
             coupon: Option[String] = None,
             description: Option[String] = None,
             email: Option[String] = None,
             invoicePrefix: Option[String] = None,
             metadata: Map[String, String] = Map.empty,
             name: Option[String] = None,
             nextInvoiceSequence: Option[Int] = None,
             paymentMethodId: Option[String] = None,
             phone: Option[String] = None,
             promotionCode: Option[String] = None,
             shipping: Option[Shipping] = None,
             source: Option[Card] = None,
             taxExempt: Option[String] = None): Future[Either[ResponseError, Customer]] = {
    val data = List(
      write("address", address),
      write("balance", balance),
      write("coupon", coupon),
      write("description", description),
      write("email", email),
      write("invoice_prefix", invoicePrefix),
      write("metadata", metadata),
      write("name", name),
      write("next_invoice_sequence", nextInvoiceSequence),
      write("payment_method", paymentMethodId),
      write("phone", phone),
      write("promotion_code", promotionCode),
      write("shipping", shipping),
      write("source", source),
      write("taxExempt", taxExempt)
    ).flatten
    stripe.post[Customer]("customers", QueryConfig.default, data: _*)
  }

  def byId(customerId: String): Future[Either[ResponseError, Customer]] = {
    stripe.get[Customer](s"customers/$customerId", QueryConfig.default)
  }

  def update(customerId: String,
             address: Option[Address] = None,
             balance: Option[Money] = None,
             coupon: Option[String] = None,
             defaultSource: Option[String] = None,
             description: Option[String] = None,
             email: Option[String] = None,
             invoicePrefix: Option[String] = None,
             metadata: Map[String, String] = Map.empty,
             name: Option[String] = None,
             nextInvoiceSequence: Option[Int] = None,
             phone: Option[String] = None,
             promotionCode: Option[String] = None,
             shipping: Option[Shipping] = None,
             source: Option[Card] = None,
             taxExempt: Option[String] = None): Future[Either[ResponseError, Customer]] = {
    val data = List(
      write("address", address),
      write("balance", balance),
      write("coupon", coupon),
      write("default_source", defaultSource),
      write("email", email),
      write("invoice_prefix", invoicePrefix),
      write("metadata", metadata),
      write("name", name),
      write("next_invoice_sequence", nextInvoiceSequence),
      write("phone", phone),
      write("promotion_code", promotionCode),
      write("shipping", shipping),
      write("source", source),
      write("tax_exempt", taxExempt)
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
