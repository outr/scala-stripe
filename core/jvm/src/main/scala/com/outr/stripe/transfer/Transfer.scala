package com.outr.stripe.transfer

import com.outr.stripe.balance.Reversal
import com.outr.stripe.{Money, StripeList}

case class Transfer(id: String,
                    `object`: String,
                    amount: Money,
                    amountReversed: Money,
                    applicationFee: Option[String],
                    balanceTransaction: String,
                    created: Long,
                    currency: String,
                    date: Long,
                    description: String,
                    destination: String,
                    destinationPayment: Option[String],
                    failureCode: Option[String],
                    failureMessage: Option[String],
                    livemode: Boolean,
                    metadata: Map[String, String],
                    method: String,
                    recipient: String,
                    reversals: StripeList[Reversal],
                    reversed: Boolean,
                    sourceTransaction: Option[String],
                    sourceType: String,
                    statementDescriptor: Option[String],
                    status: String,
                    `type`: String)