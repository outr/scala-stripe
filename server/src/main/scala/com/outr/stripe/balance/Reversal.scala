package com.outr.stripe.balance

case class Reversal(id: String,
                    `object`: String,
                    amount: BigDecimal,
                    balanceTransaction: String,
                    created: Long,
                    currency: String,
                    metadata: Map[String, String],
                    transfer: String)
