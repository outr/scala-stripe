package com.outr.stripe.balance

import com.outr.stripe.Money

case class SourceType(card: Money,
                      bankAccount: Option[Money],
                      bitcoinReceiver: Option[Money])
