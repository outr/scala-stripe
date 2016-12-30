package com.outr.stripe.support

import com.outr.stripe.charge.{Card, Shipping}
import com.outr.stripe.customer.Customer
import com.outr.stripe.{Deleted, Implicits, Money, Pickler, QueryConfig, Stripe, StripeList, TimestampFilter}

import scala.concurrent.ExecutionContext.Implicits.global
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
             trialEnd: Option[Long] = None): Future[Customer] = {
    val data = List(
      accountBalance.map("account_balance" -> Pickler.write[Money](_)),
      businessVatId.map("business_vat_id" -> _),
      coupon.map("coupon" -> _),
      description.map("description" -> _),
      email.map("email" -> _),
      if (metadata.nonEmpty) Some("metadata" -> Pickler.write[Map[String, String]](metadata)) else None,
      plan.map("plan" -> _),
      quantity.map("quantity" -> _.toString),
      shipping.map("shipping" -> Pickler.write[Shipping](_)),
      source.map("source" -> Pickler.write[Card](_)),
      taxPercent.map("tax_percent" -> _.toDouble.toString),
      trialEnd.map("trial_end" -> _.toString)
    ).flatten
    stripe.post("customers", QueryConfig.default, data: _*).map { response =>
      Pickler.read[Customer](response.body)
    }
  }

  def byId(customerId: String): Future[Customer] = {
    stripe.get(s"customers/$customerId", QueryConfig.default).map { response =>
      Pickler.read[Customer](response.body)
    }
  }

  def update(accountBalance: Option[Money] = None,
             businessVatId: Option[String] = None,
             coupon: Option[String] = None,
             defaultSource: Option[String] = None,
             description: Option[String] = None,
             email: Option[String] = None,
             metadata: Map[String, String] = Map.empty,
             shipping: Option[Shipping] = None,
             source: Option[Card] = None): Future[Customer] = {
    val data = List(
      accountBalance.map("account_balance" -> Pickler.write[Money](_)),
      businessVatId.map("business_vat_id" -> _),
      coupon.map("coupon" -> _),
      defaultSource.map("default_source" -> _),
      description.map("description" -> _),
      email.map("email" -> _),
      if (metadata.nonEmpty) Some("metadata" -> Pickler.write[Map[String, String]](metadata)) else None,
      shipping.map("shipping" -> Pickler.write[Shipping](_)),
      source.map("source" -> Pickler.write[Card](_))
    ).flatten
    stripe.post("customers", QueryConfig.default, data: _*).map { response =>
      Pickler.read[Customer](response.body)
    }
  }

  def delete(customerId: String): Future[Deleted] = {
    stripe.delete(s"customers/$customerId", QueryConfig.default).map { response =>
      Pickler.read[Deleted](response.body)
    }
  }

  def list(created: Option[TimestampFilter] = None,
           config: QueryConfig = QueryConfig.default): Future[StripeList[Customer]] = {
    val data = List(
      created.map("created" -> Pickler.write[TimestampFilter](_))
    ).flatten
    stripe.get("customers", config, data: _*).map { response =>
      Pickler.read[StripeList[Customer]](response.body)
    }
  }

  object sources {
    lazy val bankAccounts: CustomerBankAccountsSupport = new CustomerBankAccountsSupport(stripe)
    lazy val cards: CustomerCreditCardsSupport = new CustomerCreditCardsSupport(stripe)
  }
}
