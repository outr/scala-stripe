package com.outr.stripe.support

import com.outr.stripe.charge.{BankAccount, Card, PII}
import com.outr.stripe.token.Token
import com.outr.stripe.{Deleted, Implicits, Pickler, QueryConfig, Stripe, StripeList}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class BankAccountsSupport(stripe: Stripe) extends Implicits {
  def create(accountId: String,
             source: Option[String] = None,
             externalAccount: Option[String] = None,
             defaultForCurrency: Option[String] = None,
             metadata: Map[String, String] = Map.empty): Future[BankAccount] = {
    val data = List(
      source.map("source" -> _),
      externalAccount.map("external_account" -> _),
      defaultForCurrency.map("default_for_currency" -> _),
      if (metadata.nonEmpty) Some("metadata" -> Pickler.write(metadata)) else None
    ).flatten
    stripe.post(s"accounts/$accountId/external_accounts", QueryConfig.default, data: _*).map { response =>
      Pickler.read[BankAccount](response.body)
    }
  }

  def byId(accountId: String, bankAccountId: String): Future[BankAccount] = {
    stripe.get(s"accounts/$accountId/external_accounts/$bankAccountId", QueryConfig.default).map { response =>
      Pickler.read[BankAccount](response.body)
    }
  }

  def update(accountId: String,
             bankAccountId: String,
             defaultForCurrency: Option[String] = None,
             metadata: Map[String, String] = Map.empty): Future[BankAccount] = {
    val data = List(
      defaultForCurrency.map("default_for_currency" -> _),
      if (metadata.nonEmpty) Some("metadata" -> Pickler.write(metadata)) else None
    ).flatten
    stripe.post(s"accounts/$accountId/external_accounts/$bankAccountId", QueryConfig.default, data: _*).map { response =>
      Pickler.read[BankAccount](response.body)
    }
  }

  def delete(accountId: String, bankAccountId: String): Future[Deleted] = {
    stripe.delete(s"accounts/$accountId/external_accounts/$bankAccountId", QueryConfig.default).map { response =>
      Pickler.read[Deleted](response.body)
    }
  }

  def list(accountId: String, config: QueryConfig = QueryConfig.default): Future[StripeList[BankAccount]] = {
    stripe.get(s"accounts/$accountId/external_accounts", config, "object" -> "bank_account").map { response =>
      Pickler.read[StripeList[BankAccount]](response.body)
    }
  }
}
