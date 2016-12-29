package com.outr.stripe.charge

import com.outr.stripe.Money
import com.outr.stripe.balance.BalanceTransaction

case class Dispute(id: String,
                   `object`: String,
                   amount: Money,
                   balanceTransactions: List[BalanceTransaction],
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
