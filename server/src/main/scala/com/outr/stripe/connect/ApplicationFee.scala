package com.outr.stripe.connect

import com.outr.stripe.{Money, StripeList}

case class ApplicationFee(id: String,
                          `object`: String,
                          account: String,
                          amount: Money,
                          amountRefunded: Money,
                          application: String,
                          balanceTransaction: String,
                          charge: String,
                          created: Long,
                          currency: String,
                          livemode: Boolean,
                          originatingTransaction: Option[String],
                          refunded: Boolean,
                          refunds: StripeList[FeeRefund])