package com.outr.stripe.balance

import com.outr.stripe.Money

case class FeeDetail(amount: Money,
                     application: Option[String],
                     currency: String,
                     description: String,
                     `type`: String)
