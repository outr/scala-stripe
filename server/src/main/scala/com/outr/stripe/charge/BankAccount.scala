package com.outr.stripe.charge

case class BankAccount(id: String,
                       `object`: String,
                       accountHolderName: String,
                       accountHolderType: String,
                       bankName: String,
                       country: String,
                       currency: String,
                       fingerprint: String,
                       last4: String,
                       routingNumber: String,
                       status: String)