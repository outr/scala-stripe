package com.outr.stripe.refund

import com.outr.stripe.Money

case class Refund(id: String,
                  `object`: String,
                  amount: Money,
                  balanceTransaction: Option[String],
                  charge: Option[String],
                  created: Long,
                  currency: String,
                  metadata: Map[String, String],
                  paymentIntent: Option[String],
                  reason: Option[String],
                  receiptNumber: Option[String],
                  sourceTransferReversal: Option[String],
                  status: String,
                  transfer_reversal: Option[String])
