package com.outr.stripe.charge

import java.util.Currency

import com.outr.stripe.dispute.Dispute
import com.outr.stripe.refund.Refund
import com.outr.stripe.{Money, StripeList}

case class Charge(id: String,
                  `object`: String,
                  amount: Money,
                  amountRefunded: Money,
                  application: Option[String],
                  applicationFee: Option[String],
                  balanceTransaction: String,
                  captured: Boolean,
                  created: Long,
                  currency: String,
                  customer: Option[String],
                  description: Option[String],
                  destination: Option[String],
                  dispute: Option[Dispute],
                  failureCode: Option[String],
                  failureMessage: Option[String],
                  fraudDetails: Option[FraudDetails],
                  invoice: Option[String],
                  livemode: Boolean,
                  metadata: Map[String, String],
                  order: Option[String],
                  outcome: Option[Outcome],
                  paid: Boolean,
                  receiptEmail: Option[String],
                  receiptNumber: Option[String],
                  refunded: Boolean,
                  refunds: StripeList[Refund],
                  review: Option[String],
                  shipping: Option[Shipping],
                  source: Card,
                  sourceTransfer: Option[String],
                  statementDescriptor: Option[String],
                  status: String,
                  transfer: Option[String]){
  val fixAmount=Money(amount.pennies,Currency.getInstance(currency.toUpperCase()).getDefaultFractionDigits)
  val fixAmountRefunded=Money(amountRefunded.pennies,Currency.getInstance(currency.toUpperCase()).getDefaultFractionDigits)
}
