package com.outr.stripe

import com.outr.stripe.balance.{Balance, BalanceEntry, BalanceTransaction, FeeDetail, Reversal, SourceType}
import com.outr.stripe.charge.{Address, BankAccount, Card, Charge, FraudDetails, Outcome, PII, Rule, Shipping}
import com.outr.stripe.connect.{Acceptance, Account, ApplicationFee, CountrySpec, DeclineChargesOn, FeeRefund, LegalEntity, TransferSchedule}
import com.outr.stripe.customer.{Customer, Discount}
import com.outr.stripe.dispute.{Dispute, DisputeEvidence, EvidenceDetails}
import com.outr.stripe.event.{Event, EventData}
import com.outr.stripe.refund.Refund
import com.outr.stripe.subscription.{Coupon, Invoice, InvoiceItem, InvoiceLine, Plan, Subscription}
import com.outr.stripe.token.Token
import com.outr.stripe.transfer.{SourcedTransfers, Transfer, TransferReversal}
import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.semiauto._
import io.circe.generic.auto._

trait Implicits {
  // Decoders

  implicit val moneyDecoder = new Decoder[Money] {
    override def apply(c: HCursor): Result[Money] = {
      Decoder.decodeLong(c).map(l => Money(l))
    }
  }
  implicit val transferDecoder: Decoder[Transfer] = deriveDecoder[Transfer]
  implicit val reversalListDecoder: Decoder[StripeList[Reversal]] = deriveDecoder[StripeList[Reversal]]
  implicit val balanceDecoder: Decoder[Balance] = deriveDecoder[Balance]
  implicit val balanceTransactionDecoder: Decoder[BalanceTransaction] = deriveDecoder[BalanceTransaction]
  implicit val balanceEntryDecoder: Decoder[BalanceEntry] = deriveDecoder[BalanceEntry]
  implicit val sourceTypeDecoder: Decoder[SourceType] = deriveDecoder[SourceType]
  implicit val feeDetailDecoder: Decoder[FeeDetail] = deriveDecoder[FeeDetail]
  implicit val sourcedTransfersDecoder: Decoder[SourcedTransfers] = deriveDecoder[SourcedTransfers]
  implicit val reversalDecoder: Decoder[Reversal] = deriveDecoder[Reversal]
  implicit val eventDecoder: Decoder[Event] = deriveDecoder[Event]
  implicit val eventDataDecoder: Decoder[EventData] = deriveDecoder[EventData]
  implicit val balanceTransactionListDecoder: Decoder[StripeList[BalanceTransaction]] = deriveDecoder[StripeList[BalanceTransaction]]
  implicit val transferListDecoder: Decoder[StripeList[Transfer]] = deriveDecoder[StripeList[Transfer]]
  implicit val tokenDecoder: Decoder[Token] = deriveDecoder[Token]
  implicit val refundListDecoder: Decoder[StripeList[Refund]] = deriveDecoder[StripeList[Refund]]
  implicit val refundDecoder: Decoder[Refund] = deriveDecoder[Refund]
  implicit val eventListDecoder: Decoder[StripeList[Event]] = deriveDecoder[StripeList[Event]]
  implicit val disputeListDecoder: Decoder[StripeList[Dispute]] = deriveDecoder[StripeList[Dispute]]
  implicit val disputeDecoder: Decoder[Dispute] = deriveDecoder[Dispute]
  implicit val customerListDecoder: Decoder[StripeList[Customer]] = deriveDecoder[StripeList[Customer]]
  implicit val customerDecoder: Decoder[Customer] = deriveDecoder[Customer]
  implicit val deletedDecoder: Decoder[Deleted] = deriveDecoder[Deleted]
  implicit val chargeListDecoder: Decoder[StripeList[Charge]] = deriveDecoder[StripeList[Charge]]
  implicit val chargeDecoder: Decoder[Charge] = deriveDecoder[Charge]
  implicit val fraudDetailsDecoder: Decoder[FraudDetails] = deriveDecoder[FraudDetails]
  implicit val shippingDecoder: Decoder[Shipping] = deriveDecoder[Shipping]
  implicit val addressDecoder: Decoder[Address] = deriveDecoder[Address]
  implicit val outcomeDecoder: Decoder[Outcome] = deriveDecoder[Outcome]
  implicit val ruleDecoder: Decoder[Rule] = deriveDecoder[Rule]
  implicit val disputeEvidenceDecoder: Decoder[DisputeEvidence] = deriveDecoder[DisputeEvidence]
  implicit val evidenceDetailsDecoder: Decoder[EvidenceDetails] = deriveDecoder[EvidenceDetails]
  implicit val discountDecoder: Decoder[Discount] = deriveDecoder[Discount]
  implicit val couponDecoder: Decoder[Coupon] = deriveDecoder[Coupon]
  implicit val couponListDecoder: Decoder[StripeList[Coupon]] = deriveDecoder[StripeList[Coupon]]
  implicit val cardDecoder: Decoder[Card] = deriveDecoder[Card]
  implicit val cardListDecoder: Decoder[StripeList[Card]] = deriveDecoder[StripeList[Card]]
  implicit val bankAccountDecoder: Decoder[BankAccount] = deriveDecoder[BankAccount]
  implicit val bankAccountListDecoder: Decoder[StripeList[BankAccount]] = deriveDecoder[StripeList[BankAccount]]
  implicit val subscriptionListDecoder: Decoder[StripeList[Subscription]] = deriveDecoder[StripeList[Subscription]]
  implicit val planDecoder: Decoder[Plan] = deriveDecoder[Plan]
  implicit val planListDecoder: Decoder[StripeList[Plan]] = deriveDecoder[StripeList[Plan]]
  implicit val transferReversalDecoder: Decoder[TransferReversal] = deriveDecoder[TransferReversal]
  implicit val transferReversalListDecoder: Decoder[StripeList[TransferReversal]] = deriveDecoder[StripeList[TransferReversal]]
  implicit val accountDecoder: Decoder[Account] = deriveDecoder[Account]
  implicit val accountListDecoder: Decoder[StripeList[Account]] = deriveDecoder[StripeList[Account]]
  implicit val applicationFeeDecoder: Decoder[ApplicationFee] = deriveDecoder[ApplicationFee]
  implicit val applicationFeeListDecoder: Decoder[StripeList[ApplicationFee]] = deriveDecoder[StripeList[ApplicationFee]]
  implicit val feeRefundDecoder: Decoder[FeeRefund] = deriveDecoder[FeeRefund]
  implicit val feeRefundListDecoder: Decoder[StripeList[FeeRefund]] = deriveDecoder[StripeList[FeeRefund]]
  implicit val countrySpecDecoder: Decoder[CountrySpec] = deriveDecoder[CountrySpec]
  implicit val countrySpecListDecoder: Decoder[StripeList[CountrySpec]] = deriveDecoder[StripeList[CountrySpec]]
  implicit val invoiceDecoder: Decoder[Invoice] = deriveDecoder[Invoice]
  implicit val invoiceListDecoder: Decoder[StripeList[Invoice]] = deriveDecoder[StripeList[Invoice]]
  implicit val invoiceLineDecoder: Decoder[InvoiceLine] = deriveDecoder[InvoiceLine]
  implicit val invoiceLineListDecoder: Decoder[StripeList[InvoiceLine]] = deriveDecoder[StripeList[InvoiceLine]]
  implicit val invoiceItemDecoder: Decoder[InvoiceItem] = deriveDecoder[InvoiceItem]
  implicit val invoiceItemListDecoder: Decoder[StripeList[InvoiceItem]] = deriveDecoder[StripeList[InvoiceItem]]

  // Encoders

  implicit val moneyEncoder: Encoder[Money] = new Encoder[Money] {
    override def apply(a: Money): Json = Encoder.encodeLong(a.pennies)
  }
  implicit val timestampFilterEncoder: Encoder[TimestampFilter] = deriveEncoder[TimestampFilter]
  implicit val piiEncoder: Encoder[PII] = deriveEncoder[PII]
  implicit val bankAccountEncoder: Encoder[BankAccount] = deriveEncoder[BankAccount]
  implicit val cardEncoder: Encoder[Card] = deriveEncoder[Card]
  implicit val disputeEvidenceEncoder: Encoder[DisputeEvidence] = deriveEncoder[DisputeEvidence]
  implicit val shippingEncoder: Encoder[Shipping] = deriveEncoder[Shipping]
  implicit val fraudDetailsEncoder: Encoder[FraudDetails] = deriveEncoder[FraudDetails]
  implicit val addressEncoder: Encoder[Address] = deriveEncoder[Address]
  implicit val transferScheduleEncoder: Encoder[TransferSchedule] = deriveEncoder[TransferSchedule]
  implicit val acceptanceEncoder: Encoder[Acceptance] = deriveEncoder[Acceptance]
  implicit val legalEntityEncoder: Encoder[LegalEntity] = deriveEncoder[LegalEntity]
  implicit val declineChargesOnEncoder: Encoder[DeclineChargesOn] = deriveEncoder[DeclineChargesOn]
}