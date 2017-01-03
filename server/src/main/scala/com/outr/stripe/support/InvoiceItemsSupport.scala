package com.outr.stripe.support

import com.outr.stripe.subscription.InvoiceItem
import com.outr.stripe.{Deleted, Implicits, Money, Pickler, QueryConfig, Stripe, StripeList, TimestampFilter}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class InvoiceItemsSupport(stripe: Stripe) extends Implicits {
  def create(amount: Money,
             currency: String,
             customerId: String,
             description: Option[String] = None,
             discountable: Option[Boolean] = None,
             invoice: Option[String] = None,
             metadata: Map[String, String] = Map.empty,
             subscription: Option[String] = None): Future[InvoiceItem] = {
    val data = List(
      Some("amount" -> amount.pennies.toString),
      Some("currency" -> currency),
      Some("customer" -> customerId),
      description.map("description" -> _),
      discountable.map("discountable" -> _.toString),
      invoice.map("invoice" -> _),
      if (metadata.nonEmpty) Some("metadata" -> Pickler.write(metadata)) else None,
      subscription.map("subscription" -> _)
    ).flatten
    stripe.post("invoiceitems", QueryConfig.default, data: _*).map { response =>
      Pickler.read[InvoiceItem](response.body)
    }
  }

  def byId(invoiceItemId: String): Future[InvoiceItem] = {
    stripe.get(s"invoiceitems/$invoiceItemId", QueryConfig.default).map { response =>
      Pickler.read[InvoiceItem](response.body)
    }
  }

  def update(invoiceItemId: String,
             amount: Option[Money] = None,
             description: Option[String] = None,
             discountable: Option[Boolean] = None,
             metadata: Map[String, String] = Map.empty): Future[InvoiceItem] = {
    val data = List(
      amount.map("amount" -> _.pennies.toString),
      description.map("description" -> _),
      discountable.map("discountable" -> _.toString),
      if (metadata.nonEmpty) Some("metadata" -> Pickler.write(metadata)) else None
    ).flatten
    stripe.post(s"invoiceitems/$invoiceItemId", QueryConfig.default, data: _*).map { response =>
      Pickler.read[InvoiceItem](response.body)
    }
  }

  def delete(invoiceItemId: String): Future[Deleted] = {
    stripe.delete(s"invoiceitems/$invoiceItemId", QueryConfig.default).map { response =>
      Pickler.read[Deleted](response.body)
    }
  }

  def list(created: Option[TimestampFilter] = None,
           customer: Option[String] = None,
           config: QueryConfig = QueryConfig.default): Future[StripeList[InvoiceItem]] = {
    val data = List(
      created.map("created" -> Pickler.write(_)),
      customer.map("customer" -> _)
    )
    stripe.get("invoiceitems", config).map { response =>
      Pickler.read[StripeList[InvoiceItem]](response.body)
    }
  }
}