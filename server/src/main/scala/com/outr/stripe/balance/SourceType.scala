package com.outr.stripe.balance

case class SourceType(card: BigDecimal,
                      bankAccount: BigDecimal = BigDecimal(0),
                      bitcoinReceiver: BigDecimal = BigDecimal(0))
