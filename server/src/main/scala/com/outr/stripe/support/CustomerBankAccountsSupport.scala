package com.outr.stripe.support

import com.outr.stripe.charge.BankAccount
import com.outr.stripe.{Deleted, Implicits, Money, Pickler, QueryConfig, Stripe, StripeList}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CustomerBankAccountsSupport(stripe: Stripe) extends Implicits {
  def create(customerId: String,
             source: Option[String] = None,
             defaultForCurrency: Option[String] = None,
             metadata: Map[String, String] = Map.empty): Future[BankAccount] = {
    val data = List(
      source.map("source" -> _),
      defaultForCurrency.map("default_for_currency" -> _),
      if (metadata.nonEmpty) Some("metadata" -> Pickler.write(metadata)) else None
    ).flatten
    stripe.post(s"customers/$customerId/sources", QueryConfig.default, data: _*).map { response =>
      Pickler.read[BankAccount](response.body)
    }
  }

  def byId(customerId: String, bankAccountId: String): Future[BankAccount] = {
    stripe.get(s"customers/$customerId/sources/$bankAccountId", QueryConfig.default).map { response =>
      Pickler.read[BankAccount](response.body)
    }
  }

  def update(customerId: String,
             bankAccountId: String,
             accountHolderName: Option[String] = None,
             accountHolderType: Option[String] = None,
             metadata: Map[String, String] = Map.empty): Future[BankAccount] = {
    val data = List(
      accountHolderName.map("account_holder_name" -> _),
      accountHolderType.map("account_holder_type" -> _),
      if (metadata.nonEmpty) Some("metadata" -> Pickler.write(metadata)) else None
    ).flatten
    stripe.post(s"customers/$customerId/sources/$bankAccountId", QueryConfig.default, data: _*).map { response =>
      Pickler.read[BankAccount](response.body)
    }
  }

  def verify(customerId: String,
             bankAccountId: String,
             amount1: Option[Money] = None,
             amount2: Option[Money] = None,
             verificationMethod: Option[String] = None): Future[BankAccount] = {
    val data = List(
      amount1.map("amounts[]" -> _.pennies.toString),
      amount2.map("amounts[]" -> _.pennies.toString),
      verificationMethod.map("verification_method" -> _)
    ).flatten
    stripe.post(s"customers/$customerId/sources/$bankAccountId/verify", QueryConfig.default, data: _*).map { response =>
      Pickler.read[BankAccount](response.body)
    }
  }

  def delete(customerId: String, bankAccountId: String): Future[Deleted] = {
    stripe.delete(s"customers/$customerId/sources/$bankAccountId", QueryConfig.default).map { response =>
      Pickler.read[Deleted](response.body)
    }
  }

  def list(customerId: String, config: QueryConfig = QueryConfig.default): Future[StripeList[BankAccount]] = {
    stripe.get(s"customers/$customerId/sources", config, "object" -> "bank_account").map { response =>
      Pickler.read[StripeList[BankAccount]](response.body)
    }
  }
}
