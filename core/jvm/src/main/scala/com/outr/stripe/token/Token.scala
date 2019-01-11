package com.outr.stripe.token

import com.outr.stripe.charge.{BankAccount, Card}

case class Token(id: String,
                 `object`: String,
                 card: Option[Card],
                 bankAccount: Option[BankAccount],
                 clientIp: Option[String],
                 created: Long,
                 livemode: Boolean,
                 `type`: String,
                 used: Boolean)