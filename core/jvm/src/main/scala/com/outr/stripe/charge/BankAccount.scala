package com.outr.stripe.charge

case class BankAccount(id: String,
                       `object`: String,
                       number: Option[String] = None,
                       accountHolderName: Option[String],
                       accountHolderType: Option[String],
                       bankName: String,
                       country: String,
                       currency: String,
                       fingerprint: String,
                       last4: String,
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
    number = Some(number),
    accountHolderName = accountHolderName,
    accountHolderType = accountHolderType,
    bankName = "",
    country = country,
    currency = currency,
    fingerprint = "",
    last4 = "",
    routingNumber = routingNumber,
    status = "uncreated"
  )
}