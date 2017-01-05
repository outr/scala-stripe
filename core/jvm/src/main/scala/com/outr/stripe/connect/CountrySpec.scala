package com.outr.stripe.connect

case class CountrySpec(id: String,
                       `object`: String,
                       defaultCurrency: String,
                       supportedBankAccountCurrencies: Map[String, List[String]],
                       supportedPaymentCurrencies: List[String],
                       supportedPaymentMethods: List[String],
                       verificationFields: VerificationFields)