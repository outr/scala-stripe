package com.outr.stripe.connect

import com.outr.stripe.Money

case class FeeRefund(id: String,
                     `object`: String,
                     amount: Money,
                     balanceTransaction: Option[String],
                     created: Long,
                     currency: String,
                     fee: String,
                     metadata: Map[String, String])