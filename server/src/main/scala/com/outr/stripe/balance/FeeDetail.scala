package com.outr.stripe.balance

case class FeeDetail(amount: BigDecimal,
                     application: Option[String],
                     currency: String,
                     description: String,
                     `type`: String)
