package com.outr.stripe.balance

case class BalanceTransaction(id: String,
                              `object`: String,
                              amount: BigDecimal,
                              availableOn: Long,
                              created: Long,
                              currency: String,
                              description: String,
                              fee: BigDecimal,
                              feeDetails: List[FeeDetail],
                              net: BigDecimal,
                              source: String,
                              sourcedTransfers: SourcedTransfers,
                              status: String,
                              `type`: String)
