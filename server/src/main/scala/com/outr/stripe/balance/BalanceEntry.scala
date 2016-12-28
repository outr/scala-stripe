package com.outr.stripe.balance

case class BalanceEntry(currency: String, amount: BigDecimal, sourceTypes: SourceType)
