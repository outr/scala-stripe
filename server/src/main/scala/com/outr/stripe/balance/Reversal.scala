package com.outr.stripe.balance

import com.outr.stripe.Money

case class Reversal(id: String,
                    `object`: String,
                    amount: Money,
                    balanceTransaction: String,
                    created: Long,
                    currency: String,
                    metadata: Map[String, String],
                    transfer: String)
