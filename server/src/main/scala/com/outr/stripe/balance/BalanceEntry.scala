package com.outr.stripe.balance

import com.outr.stripe.Money

case class BalanceEntry(currency: String, amount: Money, sourceTypes: SourceType)
