package com.outr.stripe.balance

import com.outr.stripe.Money

case class Transfer(id: String,
                    `object`: String,
                    amount: Money,
                    amountReversed: Money,
                    applicationFee: String,
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
                    reversals: List[Reversal],
                    reversed: Boolean,
                    sourceTransaction: Option[String],
                    sourceType: String,
                    statementDescriptor: Option[String],
                    status: String,
                    `type`: String)
