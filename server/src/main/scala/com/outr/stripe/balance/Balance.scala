package com.outr.stripe.balance

case class Balance(`object`: String, available: List[BalanceEntry], livemode: Boolean, pending: List[BalanceEntry])
