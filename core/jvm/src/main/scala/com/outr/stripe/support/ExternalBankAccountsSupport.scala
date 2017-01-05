package com.outr.stripe.support

import com.outr.stripe.charge.BankAccount
import com.outr.stripe.{Deleted, Implicits, QueryConfig, ResponseError, Stripe, StripeList}

import scala.concurrent.Future

class ExternalBankAccountsSupport(stripe: Stripe) extends Implicits {
  def create(accountId: String,
             source: Option[String] = None,
             externalAccount: Option[String] = None,
             defaultForCurrency: Option[String] = None,
             metadata: Map[String, String] = Map.empty): Future[Either[ResponseError, BankAccount]] = {
    val data = List(
      write("source", source),
      write("external_account", externalAccount),
      write("default_for_currency", defaultForCurrency),
      write("metadata", metadata)
    ).flatten
    stripe.post[BankAccount](s"accounts/$accountId/external_accounts", QueryConfig.default, data: _*)
  }

  def byId(accountId: String, bankAccountId: String): Future[Either[ResponseError, BankAccount]] = {
    stripe.get[BankAccount](s"accounts/$accountId/external_accounts/$bankAccountId", QueryConfig.default)
  }

  def update(accountId: String,
             bankAccountId: String,
             defaultForCurrency: Option[String] = None,
             metadata: Map[String, String] = Map.empty): Future[Either[ResponseError, BankAccount]] = {
    val data = List(
      write("default_for_currency", defaultForCurrency),
      write("metadata", metadata)
    ).flatten
    stripe.post[BankAccount](s"accounts/$accountId/external_accounts/$bankAccountId", QueryConfig.default, data: _*)
  }

  def delete(accountId: String, bankAccountId: String): Future[Either[ResponseError, Deleted]] = {
    stripe.delete[Deleted](s"accounts/$accountId/external_accounts/$bankAccountId", QueryConfig.default)
  }

  def list(accountId: String, config: QueryConfig = QueryConfig.default): Future[Either[ResponseError, StripeList[BankAccount]]] = {
    stripe.get[StripeList[BankAccount]](s"accounts/$accountId/external_accounts", config, "object" -> "bank_account")
  }
}
