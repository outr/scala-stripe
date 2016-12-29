package com.outr.stripe.charge

import com.outr.stripe.Money

case class Refund(id: String,
                  `object`: String,
                  amount: Money,
                  balanceTransaction: Option[String],
                  charge: Option[String],
                  created: Long,
                  currency: String,
                  metadata: Map[String, String],
                  reason: Option[String],
                  receiptNumber: String,
                  status: String)
