package com.outr.stripe.support

import com.outr.stripe.charge.BankAccount
import com.outr.stripe.{Deleted, Implicits, Money, Pickler, QueryConfig, ResponseError, Stripe, StripeList}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CustomerBankAccountsSupport(stripe: Stripe) extends Implicits {
  def create(customerId: String,
             source: Option[String] = None,
             defaultForCurrency: Option[String] = None,
             metadata: Map[String, String] = Map.empty): Future[Either[ResponseError, BankAccount]] = {
    val data = List(
      write("source", source),
      write("default_for_currency", defaultForCurrency),
      write("metadata", metadata)
    ).flatten
    stripe.post[BankAccount](s"customers/$customerId/sources", QueryConfig.default, data: _*)
  }

  def byId(customerId: String, bankAccountId: String): Future[Either[ResponseError, BankAccount]] = {
    stripe.get[BankAccount](s"customers/$customerId/sources/$bankAccountId", QueryConfig.default)
  }

  def update(customerId: String,
             bankAccountId: String,
             accountHolderName: Option[String] = None,
             accountHolderType: Option[String] = None,
             metadata: Map[String, String] = Map.empty): Future[Either[ResponseError, BankAccount]] = {
    val data = List(
      write("account_holder_name", accountHolderName),
      write("account_holder_type", accountHolderType),
      write("metadata", metadata)
    ).flatten
    stripe.post[BankAccount](s"customers/$customerId/sources/$bankAccountId", QueryConfig.default, data: _*)
  }

  def verify(customerId: String,
             bankAccountId: String,
             amount1: Option[Money] = None,
             amount2: Option[Money] = None,
             verificationMethod: Option[String] = None): Future[Either[ResponseError, BankAccount]] = {
    val data = List(
      amount1.map("amounts[]" -> _.pennies.toString),
      amount2.map("amounts[]" -> _.pennies.toString),
      verificationMethod.map("verification_method" -> _)
    ).flatten
    stripe.post[BankAccount](s"customers/$customerId/sources/$bankAccountId/verify", QueryConfig.default, data: _*)
  }

  def delete(customerId: String, bankAccountId: String): Future[Either[ResponseError, Deleted]] = {
    stripe.delete[Deleted](s"customers/$customerId/sources/$bankAccountId", QueryConfig.default)
  }

  def list(customerId: String, config: QueryConfig = QueryConfig.default): Future[Either[ResponseError, StripeList[BankAccount]]] = {
    stripe.get[StripeList[BankAccount]](s"customers/$customerId/sources", config, "object" -> "bank_account")
  }
}
