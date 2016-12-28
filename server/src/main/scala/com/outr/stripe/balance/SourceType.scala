package com.outr.stripe.balance

import com.outr.stripe.Money

case class SourceType(card: Money,
                      bankAccount: Money = Money(0),
                      bitcoinReceiver: Money = Money(0))
