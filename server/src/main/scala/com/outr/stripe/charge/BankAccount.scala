package com.outr.stripe.charge

case class BankAccount(id: String,
                       `object`: String,
                       account: String,
                       accountHolderName: String,
                       accountHolderType: String,
                       bankName: String,
                       country: String,
                       currency: String,
                       defaultForCurrency: Boolean,
                       fingerprint: String,
                       last4: String,
                       metadata: Map[String, String],
                       routingNumber: String,
                       status: String)