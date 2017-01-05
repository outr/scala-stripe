package com.outr.stripe.dispute

import com.outr.stripe.balance.BalanceTransaction
import com.outr.stripe.{Money, StripeList}

case class Dispute(id: String,
                   `object`: String,
                   amount: Money,
                   balanceTransactions: StripeList[BalanceTransaction],
                   charge: String,
                   created: Long,
                   currency: String,
                   evidence: DisputeEvidence,
                   evidenceDetails: EvidenceDetails,
                   isChargeRefundable: Boolean,
                   livemode: Boolean,
                   metadata: Map[String, String],
                   reason: String,
                   status: String)
