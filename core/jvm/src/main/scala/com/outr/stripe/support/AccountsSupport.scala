package com.outr.stripe.support

import com.outr.stripe.connect.{Acceptance, Account, DeclineChargeOn, LegalEntity, TransferSchedule}
import com.outr.stripe.{Deleted, Implicits, QueryConfig, ResponseError, Stripe, StripeList}

import scala.concurrent.Future

class AccountsSupport(stripe: Stripe) extends Implicits {
  def create(country: Option[String] = None,
             email: Option[String] = None,
             custom: Boolean = false,
             accountToken: Option[String] = None,
             businessLogo: Option[String] = None,
             businessName: Option[String] = None,
             businessPrimaryColor: Option[String] = None,
             businessURL: Option[String] = None,
             legalEntity: Option[LegalEntity] = None,
             tosAcceptance: Option[Acceptance] = None): Future[Either[ResponseError, Account]] = {
    val data = List(
      write("type", if (custom) "custom" else "standard"),
      write("country", country),
      write("email", email),
      write("account_token", accountToken),
      write("business_logo", businessLogo),
      write("business_name", businessName),
      write("business_primary_color", businessPrimaryColor),
      write("business_url", businessURL),
      write("legal_entity", legalEntity),
      write("tos_acceptance", tosAcceptance)
    ).flatten
    stripe.post[Account]("accounts", QueryConfig.default, data: _*)
  }

  def byId(accountId: String): Future[Either[ResponseError, Account]] = {
    stripe.get[Account](s"accounts/$accountId", QueryConfig.default)
  }

  def update(accountId: String,
             businessLogo: Option[String] = None,
             businessName: Option[String] = None,
             businessPrimaryColor: Option[String] = None,
             businessUrl: Option[String] = None,
             debitNegativeBalances: Option[Boolean] = None,
             declineChargeOn: Option[DeclineChargeOn] = None,
             defaultCurrency: Option[String] = None,
             email: Option[String] = None,
             externalAccount: Option[String] = None,
             legalEntity: Option[LegalEntity] = None,
             metadata: Map[String, String] = Map.empty,
             productDescription: Option[String] = None,
             statementDescriptor: Option[String] = None,
             supportEmail: Option[String] = None,
             supportPhone: Option[String] = None,
             supportUrl: Option[String] = None,
             tosAcceptance: Option[Acceptance] = None,
             transferSchedule: Option[TransferSchedule] = None,
             transferStatementDescriptor: Option[String] = None): Future[Either[ResponseError, Account]] = {
    val data = List(
      write("business_logo", businessLogo),
      write("business_name", businessName),
      write("business_primary_color", businessPrimaryColor),
      write("business_url", businessUrl),
      write("debit_negative_balances", debitNegativeBalances),
      write("decline_charges_on", declineChargeOn),
      write("default_currency", defaultCurrency),
      write("email", email),
      write("external_account", externalAccount),
      write("legal_entity", legalEntity),
      write("metadata", metadata),
      write("product_description", productDescription),
      write("statement_descriptor", statementDescriptor),
      write("support_email", supportEmail),
      write("support_phone", supportPhone),
      write("support_url", supportUrl),
      write("tos_acceptance", tosAcceptance),
      write("transfer_schedule", transferSchedule),
      write("transfer_statement_descriptor", transferStatementDescriptor)
    ).flatten
    stripe.post[Account](s"accounts/$accountId", QueryConfig.default, data: _*)
  }

  def delete(accountId: String): Future[Either[ResponseError, Deleted]] = {
    stripe.delete[Deleted](s"accounts/$accountId", QueryConfig.default)
  }

  def reject(accountId: String, reason: String): Future[Either[ResponseError, Account]] = {
    stripe.post[Account](s"accounts/$accountId/reject", QueryConfig.default, "reason" -> reason)
  }

  def list(config: QueryConfig = QueryConfig.default): Future[Either[ResponseError, StripeList[Account]]] = {
    stripe.get[StripeList[Account]]("accounts", config)
  }

  object external {
    lazy val bankAccounts = new ExternalBankAccountsSupport(stripe)
    lazy val cards = new ExternalCreditCardsSupport(stripe)
  }
}
