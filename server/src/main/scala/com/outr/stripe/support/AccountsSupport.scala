package com.outr.stripe.support

import com.outr.stripe.connect.{Acceptance, Account, DeclineChargesOn, LegalEntity, TransferSchedule}
import com.outr.stripe.{Deleted, Implicits, Pickler, QueryConfig, Stripe, StripeList}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AccountsSupport(stripe: Stripe) extends Implicits {
  def create(country: Option[String] = None,
             email: Option[String] = None,
             managed: Boolean = false): Future[Account] = {
    val data = List(
      country.map("country" -> _),
      email.map("email" -> _),
      if (managed) Some("managed" -> "true") else None
    ).flatten
    stripe.post("accounts", QueryConfig.default, data: _*).map { response =>
      Pickler.read[Account](response.body)
    }
  }

  def byId(accountId: String): Future[Account] = {
    stripe.get(s"accounts/$accountId", QueryConfig.default).map { response =>
      Pickler.read[Account](response.body)
    }
  }

  def update(accountId: String,
             businessLogo: Option[String] = None,
             businessName: Option[String] = None,
             businessPrimaryColor: Option[String] = None,
             businessUrl: Option[String] = None,
             debitNegativeBalances: Option[Boolean] = None,
             declineChargeOn: Option[DeclineChargesOn] = None,
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
             transferStatementDescriptor: Option[String] = None): Future[Account] = {
    val data = List(
      businessLogo.map("business_logo" -> _),
      businessName.map("business_name" -> _),
      businessPrimaryColor.map("business_primary_color" -> _),
      businessUrl.map("business_url" -> _),
      debitNegativeBalances.map("debit_negative_balances" -> _.toString),
      declineChargeOn.map("decline_charge_on" -> Pickler.write(_)),
      defaultCurrency.map("default_currency" -> _),
      email.map("email" -> _),
      externalAccount.map("external_account" -> _),
      legalEntity.map("legal_entity" -> Pickler.write(_)),
      if (metadata.nonEmpty) Some("metadata" -> Pickler.write(metadata)) else None,
      productDescription.map("product_description" -> _),
      statementDescriptor.map("statement_descriptor" -> _),
      supportEmail.map("support_email" -> _),
      supportPhone.map("support_phone" -> _),
      supportUrl.map("support_url" -> _),
      tosAcceptance.map("tos_acceptance" -> Pickler.write(_)),
      transferSchedule.map("transfer_schedule" -> Pickler.write(_)),
      transferStatementDescriptor.map("transfer_statement_descriptor" -> _)
    ).flatten
    stripe.post(s"accounts/$accountId", QueryConfig.default, data: _*).map { response =>
      Pickler.read[Account](response.body)
    }
  }

  def delete(accountId: String): Future[Deleted] = {
    stripe.delete(s"accounts/$accountId", QueryConfig.default).map { response =>
      Pickler.read[Deleted](response.body)
    }
  }

  def reject(accountId: String, reason: String): Future[Account] = {
    stripe.post(s"accounts/$accountId/reject", QueryConfig.default, "reason" -> reason).map { response =>
      Pickler.read[Account](response.body)
    }
  }

  def list(config: QueryConfig = QueryConfig.default): Future[StripeList[Account]] = {
    stripe.get("accounts", config).map { response =>
      Pickler.read[StripeList[Account]](response.body)
    }
  }
}
