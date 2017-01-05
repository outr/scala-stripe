package com.outr.stripe.support

import com.outr.stripe.charge.Card
import com.outr.stripe.{Deleted, Implicits, QueryConfig, ResponseError, Stripe, StripeList}

import scala.concurrent.Future

class ExternalCreditCardsSupport(stripe: Stripe) extends Implicits {
  def create(accountId: String,
             source: Option[String] = None,
             externalAccount: Option[String] = None,
             defaultForCurrency: Option[String] = None,
             metadata: Map[String, String] = Map.empty): Future[Either[ResponseError, Card]] = {
    val data = List(
      write("source", source),
      write("external_account", externalAccount),
      write("default_for_currency", defaultForCurrency),
      write("metadata", metadata)
    ).flatten
    stripe.post[Card](s"accounts/$accountId/external_accounts", QueryConfig.default, data: _*)
  }

  def byId(accountId: String, cardId: String): Future[Either[ResponseError, Card]] = {
    stripe.get[Card](s"accounts/$accountId/external_accounts/$cardId", QueryConfig.default)
  }

  def update(accountId: String,
             cardId: String,
             addressCity: Option[String] = None,
             addressCountry: Option[String] = None,
             addressLine1: Option[String] = None,
             addressLine2: Option[String] = None,
             addressState: Option[String] = None,
             addressZip: Option[String] = None,
             defaultForCurrency: Option[String] = None,
             expMonth: Option[Int] = None,
             expYear: Option[Int] = None,
             metadata: Map[String, String] = Map.empty,
             name: Option[String] = None): Future[Either[ResponseError, Card]] = {
    val data = List(
      write("address_city", addressCity),
      write("address_country", addressCountry),
      write("address_line1", addressLine1),
      write("address_line2", addressLine2),
      write("address_state", addressState),
      write("address_zip", addressZip),
      write("default_for_currency", defaultForCurrency),
      write("exp_month", expMonth),
      write("exp_year", expYear),
      write("metadata", metadata),
      write("name", name)
    ).flatten
    stripe.post[Card](s"accounts/$accountId/external_accounts/$cardId", QueryConfig.default, data: _*)
  }

  def delete(accountId: String, cardId: String): Future[Either[ResponseError, Deleted]] = {
    stripe.delete[Deleted](s"accounts/$accountId/external_accounts/$cardId", QueryConfig.default)
  }

  def list(accountId: String, config: QueryConfig = QueryConfig.default): Future[Either[ResponseError, StripeList[Card]]] = {
    stripe.get[StripeList[Card]](s"accounts/$accountId/external_accounts", config, "object" -> "card")
  }
}
