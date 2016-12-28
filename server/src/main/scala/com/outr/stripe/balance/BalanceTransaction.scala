package com.outr.stripe.balance

import com.outr.stripe.Money

case class BalanceTransaction(id: String,
                              `object`: String,
                              amount: Money,
                              availableOn: Long,
                              created: Long,
                              currency: String,
                              description: String,
                              fee: Money,
                              feeDetails: List[FeeDetail],
                              net: Money,
                              source: String,
                              sourcedTransfers: SourcedTransfers,
                              status: String,
                              `type`: String)
