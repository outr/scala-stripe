package com.outr.stripe.charge

case class BankAccount(id: String,
                       `object`: String,
                       account: String,
                       number: Option[String] = None,
                       accountHolderName: Option[String],
                       accountHolderType: Option[String],
                       bankName: String,
                       country: String,
                       currency: String,
                       defaultForCurrency: Boolean,
                       fingerprint: String,
                       last4: String,
                       metadata: Map[String, String],
                       routingNumber: Option[String],
                       status: String)

object BankAccount {
  def create(number: String,
             country: String,
             currency: String,
             routingNumber: Option[String] = None,
             accountHolderName: Option[String] = None,
             accountHolderType: Option[String] = None): BankAccount = BankAccount(
    id = "",
    `object` = "bank_account",
    account = "",
    number = Some(number),
    accountHolderName = accountHolderName,
    accountHolderType = accountHolderType,
    bankName = "",
    country = country,
    currency = currency,
    defaultForCurrency = false,
    fingerprint = "",
    last4 = "",
    metadata = Map.empty,
    routingNumber = routingNumber,
    status = "uncreated"
  )
}