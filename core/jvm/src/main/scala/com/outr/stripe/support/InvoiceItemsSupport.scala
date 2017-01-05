package com.outr.stripe.support

import com.outr.stripe.subscription.InvoiceItem
import com.outr.stripe.{Deleted, Implicits, Money, QueryConfig, ResponseError, Stripe, StripeList, TimestampFilter}

import scala.concurrent.Future

class InvoiceItemsSupport(stripe: Stripe) extends Implicits {
  def create(amount: Money,
             currency: String,
             customerId: String,
             description: Option[String] = None,
             discountable: Option[Boolean] = None,
             invoice: Option[String] = None,
             metadata: Map[String, String] = Map.empty,
             subscription: Option[String] = None): Future[Either[ResponseError, InvoiceItem]] = {
    val data = List(
      write("amount", amount),
      write("currency", currency),
      write("customer", customerId),
      write("description", description),
      write("discountable", discountable),
      write("invoice", invoice),
      write("metadata", metadata),
      write("subscription", subscription)
    ).flatten
    stripe.post[InvoiceItem]("invoiceitems", QueryConfig.default, data: _*)
  }

  def byId(invoiceItemId: String): Future[Either[ResponseError, InvoiceItem]] = {
    stripe.get[InvoiceItem](s"invoiceitems/$invoiceItemId", QueryConfig.default)
  }

  def update(invoiceItemId: String,
             amount: Option[Money] = None,
             description: Option[String] = None,
             discountable: Option[Boolean] = None,
             metadata: Map[String, String] = Map.empty): Future[Either[ResponseError, InvoiceItem]] = {
    val data = List(
      write("amount", amount),
      write("description", description),
      write("discountable", discountable),
      write("metadata", metadata)
    ).flatten
    stripe.post[InvoiceItem](s"invoiceitems/$invoiceItemId", QueryConfig.default, data: _*)
  }

  def delete(invoiceItemId: String): Future[Either[ResponseError, Deleted]] = {
    stripe.delete[Deleted](s"invoiceitems/$invoiceItemId", QueryConfig.default)
  }

  def list(created: Option[TimestampFilter] = None,
           customer: Option[String] = None,
           config: QueryConfig = QueryConfig.default): Future[Either[ResponseError, StripeList[InvoiceItem]]] = {
    val data = List(
      write("created", created),
      write("customer", customer)
    )
    stripe.get[StripeList[InvoiceItem]]("invoiceitems", config)
  }
}