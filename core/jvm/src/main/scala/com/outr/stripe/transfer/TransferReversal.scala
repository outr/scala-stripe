package com.outr.stripe.transfer

import com.outr.stripe.Money

case class TransferReversal(id: String,
                            `object`: String,
                            amount: Money,
                            balanceTransaction: Option[String],
                            created: Long,
                            currency: String,
                            metadata: Map[String, String],
                            transfer: String)