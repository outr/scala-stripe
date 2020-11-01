package com.outr.stripe

import com.outr.stripe.balance.{Balance, BalanceEntry, BalanceTransaction, FeeDetail, Reversal, SourceType}
import com.outr.stripe.charge.{Address, BankAccount, Card, Charge, FraudDetails, Outcome, PII, Rule, Shipping}
import com.outr.stripe.connect.{Acceptance, Account, AccountVerification, AddressKana, AddressKanji, ApplicationFee, CountrySpec, Date, DeclineChargeOn, FeeRefund, Keys, LegalEntity, TransferSchedule, Verification, VerificationFields}
import com.outr.stripe.customer.{Customer, Discount}
import com.outr.stripe.dispute.{Dispute, DisputeEvidence, EvidenceDetails}
import com.outr.stripe.event.{Event, EventData}
import com.outr.stripe.price.{Price, Recurring, Tier, TransformQuantity}
import com.outr.stripe.product.PackageDimensions
import com.outr.stripe.product.{Product => StripeProduct}
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

  protected implicit val moneyDecoder: Decoder[Money] = new Decoder[Money] {
    override def apply(c: HCursor): Result[Money] = Decoder.decodeLong(c) match {
      case Left(failure) => Left(failure)
      case Right(l) => Right(Money(l))//missing scale information
    }
  }
  protected implicit val transferDecoder: Decoder[Transfer] = deriveDecoder[Transfer]
  protected implicit val reversalListDecoder: Decoder[StripeList[Reversal]] = deriveDecoder[StripeList[Reversal]]
  protected implicit val balanceDecoder: Decoder[Balance] = deriveDecoder[Balance]
  protected implicit val balanceTransactionDecoder: Decoder[BalanceTransaction] = deriveDecoder[BalanceTransaction]
  protected implicit val balanceEntryDecoder: Decoder[BalanceEntry] = deriveDecoder[BalanceEntry]
  protected implicit val sourceTypeDecoder: Decoder[SourceType] = deriveDecoder[SourceType]
  protected implicit val feeDetailDecoder: Decoder[FeeDetail] = deriveDecoder[FeeDetail]
  protected implicit val sourcedTransfersDecoder: Decoder[SourcedTransfers] = deriveDecoder[SourcedTransfers]
  protected implicit val reversalDecoder: Decoder[Reversal] = deriveDecoder[Reversal]
  protected implicit val eventDecoder: Decoder[Event] = deriveDecoder[Event]
  protected implicit val eventDataDecoder: Decoder[EventData] = deriveDecoder[EventData]
  protected implicit val balanceTransactionListDecoder: Decoder[StripeList[BalanceTransaction]] = deriveDecoder[StripeList[BalanceTransaction]]
  protected implicit val transferListDecoder: Decoder[StripeList[Transfer]] = deriveDecoder[StripeList[Transfer]]
  protected implicit val tokenDecoder: Decoder[Token] = deriveDecoder[Token]
  protected implicit val packageDimensionDecoder: Decoder[PackageDimensions] = deriveDecoder[PackageDimensions]
  protected implicit val priceDecoder: Decoder[Price] = deriveDecoder[Price]
  protected implicit val priceListDecoder: Decoder[StripeList[Price]] = deriveDecoder[StripeList[Price]]
  protected implicit val productListDecoder: Decoder[StripeList[StripeProduct]] = deriveDecoder[StripeList[StripeProduct]]
  protected implicit val productDecoder: Decoder[StripeProduct] = deriveDecoder[StripeProduct]
  protected implicit val refundListDecoder: Decoder[StripeList[Refund]] = deriveDecoder[StripeList[Refund]]
  protected implicit val refundDecoder: Decoder[Refund] = deriveDecoder[Refund]
  protected implicit val eventListDecoder: Decoder[StripeList[Event]] = deriveDecoder[StripeList[Event]]
  protected implicit val disputeListDecoder: Decoder[StripeList[Dispute]] = deriveDecoder[StripeList[Dispute]]
  protected implicit val disputeDecoder: Decoder[Dispute] = deriveDecoder[Dispute]
  protected implicit val customerListDecoder: Decoder[StripeList[Customer]] = deriveDecoder[StripeList[Customer]]
  protected implicit val customerDecoder: Decoder[Customer] = deriveDecoder[Customer]
  protected implicit val deletedDecoder: Decoder[Deleted] = deriveDecoder[Deleted]
  protected implicit val chargeListDecoder: Decoder[StripeList[Charge]] = deriveDecoder[StripeList[Charge]]
  protected implicit val chargeDecoder: Decoder[Charge] = deriveDecoder[Charge]
  protected implicit val fraudDetailsDecoder: Decoder[FraudDetails] = deriveDecoder[FraudDetails]
  protected implicit val shippingDecoder: Decoder[Shipping] = deriveDecoder[Shipping]
  protected implicit val addressDecoder: Decoder[Address] = deriveDecoder[Address]
  protected implicit val outcomeDecoder: Decoder[Outcome] = deriveDecoder[Outcome]
  protected implicit val ruleDecoder: Decoder[Rule] = deriveDecoder[Rule]
  protected implicit val disputeEvidenceDecoder: Decoder[DisputeEvidence] = deriveDecoder[DisputeEvidence]
  protected implicit val evidenceDetailsDecoder: Decoder[EvidenceDetails] = deriveDecoder[EvidenceDetails]
  protected implicit val discountDecoder: Decoder[Discount] = deriveDecoder[Discount]
  protected implicit val couponDecoder: Decoder[Coupon] = deriveDecoder[Coupon]
  protected implicit val couponListDecoder: Decoder[StripeList[Coupon]] = deriveDecoder[StripeList[Coupon]]
  protected implicit val cardDecoder: Decoder[Card] = deriveDecoder[Card]
  protected implicit val cardListDecoder: Decoder[StripeList[Card]] = deriveDecoder[StripeList[Card]]
  protected implicit val bankAccountDecoder: Decoder[BankAccount] = deriveDecoder[BankAccount]
  protected implicit val bankAccountListDecoder: Decoder[StripeList[BankAccount]] = deriveDecoder[StripeList[BankAccount]]
  protected implicit val subscriptionDecoder: Decoder[Subscription] = deriveDecoder[Subscription]
  protected implicit val subscriptionListDecoder: Decoder[StripeList[Subscription]] = deriveDecoder[StripeList[Subscription]]
  protected implicit val planDecoder: Decoder[Plan] = deriveDecoder[Plan]
  protected implicit val planListDecoder: Decoder[StripeList[Plan]] = deriveDecoder[StripeList[Plan]]
  protected implicit val transferReversalDecoder: Decoder[TransferReversal] = deriveDecoder[TransferReversal]
  protected implicit val transferReversalListDecoder: Decoder[StripeList[TransferReversal]] = deriveDecoder[StripeList[TransferReversal]]
  protected implicit val accountDecoder: Decoder[Account] = deriveDecoder[Account]
  protected implicit val accountListDecoder: Decoder[StripeList[Account]] = deriveDecoder[StripeList[Account]]
  protected implicit val applicationFeeDecoder: Decoder[ApplicationFee] = deriveDecoder[ApplicationFee]
  protected implicit val applicationFeeListDecoder: Decoder[StripeList[ApplicationFee]] = deriveDecoder[StripeList[ApplicationFee]]
  protected implicit val feeRefundDecoder: Decoder[FeeRefund] = deriveDecoder[FeeRefund]
  protected implicit val feeRefundListDecoder: Decoder[StripeList[FeeRefund]] = deriveDecoder[StripeList[FeeRefund]]
  protected implicit val countrySpecDecoder: Decoder[CountrySpec] = deriveDecoder[CountrySpec]
  protected implicit val countrySpecListDecoder: Decoder[StripeList[CountrySpec]] = deriveDecoder[StripeList[CountrySpec]]
  protected implicit val invoiceDecoder: Decoder[Invoice] = deriveDecoder[Invoice]
  protected implicit val invoiceListDecoder: Decoder[StripeList[Invoice]] = deriveDecoder[StripeList[Invoice]]
  protected implicit val invoiceLineDecoder: Decoder[InvoiceLine] = deriveDecoder[InvoiceLine]
  protected implicit val invoiceLineListDecoder: Decoder[StripeList[InvoiceLine]] = deriveDecoder[StripeList[InvoiceLine]]
  protected implicit val invoiceItemDecoder: Decoder[InvoiceItem] = deriveDecoder[InvoiceItem]
  protected implicit val invoiceItemListDecoder: Decoder[StripeList[InvoiceItem]] = deriveDecoder[StripeList[InvoiceItem]]
  protected implicit val errorMessageWrapperDecoder: Decoder[ErrorMessageWrapper] = deriveDecoder[ErrorMessageWrapper]
  protected implicit val errorMessageDecoder: Decoder[ErrorMessage] = deriveDecoder[ErrorMessage]
  protected implicit val declineChargesOnDecoder: Decoder[DeclineChargeOn] = deriveDecoder[DeclineChargeOn]
  protected implicit val legalEntityDecoder: Decoder[LegalEntity] = deriveDecoder[LegalEntity]
  protected implicit val acceptanceDecoder: Decoder[Acceptance] = deriveDecoder[Acceptance]
  protected implicit val transferScheduleDecoder: Decoder[TransferSchedule] = deriveDecoder[TransferSchedule]
  protected implicit val accountVerificationDecoder: Decoder[AccountVerification] = deriveDecoder[AccountVerification]
  protected implicit val keysDecoder: Decoder[Keys] = deriveDecoder[Keys]
  protected implicit val jsonListDecoder: Decoder[StripeList[Json]] = deriveDecoder[StripeList[Json]]
  protected implicit val verificationFieldsDecoder: Decoder[VerificationFields] = deriveDecoder[VerificationFields]

  // Encoders

  def write[T](key: String, value: Option[T])(implicit encoder: MapEncoder[T]): Map[String, String] = {
    value.map(encoder.encode(key, _)).getOrElse(Map.empty)
  }

  def write[T](key: String, value: T)(implicit encoder: MapEncoder[T]): Map[String, String] = encoder.encode(key, value)

  def write[T](key: String, value: T, default: T)(implicit encoder: MapEncoder[T]): Map[String, String] = {
    if (value != default) encoder.encode(key, value) else Map.empty
  }

  protected implicit val moneyEncoder: MapEncoder[Money] = MapEncoder.singleValue[Money](_.pennies.toString)
  protected implicit val stringEncoder: MapEncoder[String] = MapEncoder.singleValue[String](s => s)
  protected implicit val timestampFilterEncoder: MapEncoder[TimestampFilter] = new MapEncoder[TimestampFilter] {
    override def encode(key: String, value: TimestampFilter): Map[String, String] = List(
      value.gt.map("gt" -> _.toString),
      value.gte.map("gte" -> _.toString),
      value.lt.map("lt" -> _.toString),
      value.lt.map("lte" -> _.toString)
    ).flatten.toMap
  }
  protected implicit val mapEncoder: MapEncoder[Map[String, String]] = new MapEncoder[Map[String, String]] {
    override def encode(key: String, value: Map[String, String]): Map[String, String] = value.map {
      case (k, v) => s"$key[$k]" -> v
    }
  }
  protected implicit val booleanEncoder: MapEncoder[Boolean] = MapEncoder.singleValue[Boolean](_.toString)
  protected implicit val intEncoder: MapEncoder[Int] = MapEncoder.singleValue[Int](_.toString)
  protected implicit val longEncoder: MapEncoder[Long] = MapEncoder.singleValue[Long](_.toString)
  protected implicit val bigDecimalEncoder: MapEncoder[BigDecimal] = MapEncoder.singleValue(_.toString())
  protected implicit val shippingEncoder: MapEncoder[Shipping] = new MapEncoder[Shipping] {
    override def encode(key: String, value: Shipping): Map[String, String] = List(
      write(s"$key[address]", value.address),
      write(s"$key[carrier]", value.carrier),
      write(s"$key[name]", value.name),
      write(s"$key[phone]", value.phone),
      write(s"$key[tracking_number]", value.trackingNumber)
    ).flatten.toMap
  }
  protected implicit val addressEncoder: MapEncoder[Address] = new MapEncoder[Address] {
    override def encode(key: String, value: Address): Map[String, String] = Map(
      s"$key[city]" -> value.city,
      s"$key[country]" -> Option(value.country),
      s"$key[line1]" -> value.line1,
      s"$key[line2]" -> value.line2,
      s"$key[postal_code]" -> value.postalCode,
      s"$key[state]" -> value.state
    ).flatMap(t => t._2.map(t._1 -> _))
  }
  protected implicit val addressKanaEncoder: MapEncoder[AddressKana] = new MapEncoder[AddressKana] {
    override def encode(key: String, value: AddressKana): Map[String, String] = Map(
      s"$key[city]" -> value.city,
      s"$key[country]" -> value.country,
      s"$key[line1]" -> value.line1,
      s"$key[line2]" -> value.line2,
      s"$key[postal_code]" -> value.postalCode,
      s"$key[state]" -> value.state,
      s"$key[town]" -> value.town
    )
  }
  protected implicit val addressKanjiEncoder: MapEncoder[AddressKanji] = new MapEncoder[AddressKanji] {
    override def encode(key: String, value: AddressKanji): Map[String, String] = Map(
      s"$key[city]" -> value.city,
      s"$key[country]" -> value.country,
      s"$key[line1]" -> value.line1,
      s"$key[line2]" -> value.line2,
      s"$key[postal_code]" -> value.postalCode,
      s"$key[state]" -> value.state,
      s"$key[town]" -> value.town
    )
  }
  protected implicit val cardEncoder: MapEncoder[Card] = new MapEncoder[Card] {
    override def encode(key: String, value: Card): Map[String, String] = List(
      write(s"$key[number]", value.number),
      write(s"$key[exp_month]", value.expMonth),
      write(s"$key[exp_year]", value.expYear),
      write(s"$key[cvc]", value.cvc),
      write(s"$key[name]", value.name),
      write(s"$key[address_city]", value.addressCity),
      write(s"$key[address_country]", value.addressCountry),
      write(s"$key[address_line1]", value.addressLine1),
      write(s"$key[address_line2]", value.addressLine2),
      write(s"$key[address_state]", value.addressState),
      write(s"$key[address_zip]", value.addressZip),
      write(s"$key[currency]", value.currency)
    ).flatten.toMap
  }
  protected implicit val bankAccountEncoder: MapEncoder[BankAccount] = new MapEncoder[BankAccount] {
    override def encode(key: String, value: BankAccount): Map[String, String] = List(
      write(s"$key[number]", value.number),
      write(s"$key[country]", value.country),
      write(s"$key[currency]", value.currency),
      write(s"$key[routing_number]", value.routingNumber),
      write(s"$key[account_holder_name]", value.accountHolderName),
      write(s"$key[account_holder_type]", value.accountHolderType)
    ).flatten.toMap
  }
  protected implicit val piiEncoder: MapEncoder[PII] = new MapEncoder[PII] {
    override def encode(key: String, value: PII): Map[String, String] = Map(
      s"$key[personal_id_number]" -> value.personalIdNumber
    )
  }
  protected implicit val declineChargesOnEncoder: MapEncoder[DeclineChargeOn] = new MapEncoder[DeclineChargeOn] {
    override def encode(key: String, value: DeclineChargeOn): Map[String, String] = Map(
      s"$key[avs_failure]" -> value.avsFailure.toString,
      s"$key[cvc_failure]" -> value.cvcFailure.toString
    )
  }
  protected implicit val dateEncoder: MapEncoder[Date] = new MapEncoder[Date] {
    override def encode(key: String, value: Date): Map[String, String] = List(
      write(s"$key[day]", value.day),
      write(s"$key[month]", value.month),
      write(s"$key[year]", value.year)
    ).flatten.toMap
  }
  protected implicit val legalEntityEncoder: MapEncoder[LegalEntity] = new MapEncoder[LegalEntity] {
    override def encode(key: String, value: LegalEntity): Map[String, String] = List(
      write(s"$key[address]", value.address),
      write(s"$key[address_kana]", value.addressKana),
      write(s"$key[address_kanji]", value.addressKanji),
      write(s"$key[business_name]", value.businessName),
      write(s"$key[business_name_kana]", value.businessNameKana),
      write(s"$key[business_name_kanji]", value.businessNameKanji),
      write(s"$key[business_tax_id_provided]", value.businessTaxIdProvided),
      write(s"$key[business_vat_id_provided]", value.businessVatIdProvided),
      write(s"$key[dob]", value.dob),
      write(s"$key[first_name]", value.firstName),
      write(s"$key[first_name_kana]", value.firstNameKana),
      write(s"$key[first_name_kanji]", value.firstNameKanji),
      write(s"$key[gender]", value.gender),
      write(s"$key[last_name]", value.lastName),
      write(s"$key[last_name_kana]", value.lastNameKana),
      write(s"$key[last_name_kanji]", value.lastNameKanji),
      write(s"$key[maiden_name]", value.maidenName),
      write(s"$key[personal_address]", value.personalAddress),
      write(s"$key[personal_address_kana]", value.personalAddressKana),
      write(s"$key[personal_address_kanji]", value.personalAddressKanji),
      write(s"$key[personal_id_number_provided]", value.personalIdNumberProvided),
      write(s"$key[phone_number]", value.phoneNumber),
      write(s"$key[ssn_last_4]", value.ssnLast4),
      write(s"$key[type]", value.`type`),
      write(s"$key[verification]", value.verification)
    ).flatten.toMap
  }
  protected implicit val acceptanceEncoder: MapEncoder[Acceptance] = new MapEncoder[Acceptance] {
    override def encode(key: String, value: Acceptance): Map[String, String] = List(
      write(s"$key[date]", value.date),
      write(s"$key[ip]", value.ip),
      write(s"$key[user_agent]", value.userAgent),
      write(s"$key[iovation_blackbox]", value.iovationBlackbox)
    ).flatten.toMap
  }
  protected implicit val transferScheduleEncoder: MapEncoder[TransferSchedule] = new MapEncoder[TransferSchedule] {
    override def encode(key: String, value: TransferSchedule): Map[String, String] = List(
      write(s"$key[delay_days]", value.delayDays),
      write(s"$key[interval]", value.interval),
      write(s"$key[monthly_anchor]", value.monthlyAnchor),
      write(s"$key[weekly_anchor]", value.weeklyAnchor)
    ).flatten.toMap
  }
  protected implicit val verificationEncoder: MapEncoder[Verification] = new MapEncoder[Verification] {
    override def encode(key: String, value: Verification): Map[String, String] = List(
      write(s"$key[details]", value.details),
      write(s"$key[details_code]", value.detailsCode),
      write(s"$key[document]", value.document),
      write(s"$key[status]", value.status)
    ).flatten.toMap
  }
  protected implicit val fraudDetailsEncoder: MapEncoder[FraudDetails] = new MapEncoder[FraudDetails] {
    override def encode(key: String, value: FraudDetails): Map[String, String] = List(
      write(s"$key[user_report]", value.userReport),
      write(s"$key[safe]", value.safe),
      write(s"$key[fraudulent]", value.fraudulent),
      write(s"$key[stripe_report]", value.stripeReport)
    ).flatten.toMap
  }
  protected implicit val disputeEvidenceEncoder: MapEncoder[DisputeEvidence] = new MapEncoder[DisputeEvidence] {
    override def encode(key: String, value: DisputeEvidence): Map[String, String] = List(
      write(s"$key[access_activity_log]", value.accessActivityLog),
      write(s"$key[billing_address]", value.billingAddress),
      write(s"$key[cancellation_policy]", value.cancellationPolicy),
      write(s"$key[cancellation_policy_disclosure]", value.cancellationPolicyDisclosure),
      write(s"$key[cancellation_rebuttal]", value.cancellationRebuttal),
      write(s"$key[customer_communication]", value.customerCommunication),
      write(s"$key[customer_email_address]", value.customerEmailAddress),
      write(s"$key[customer_name]", value.customerName),
      write(s"$key[customer_purchase_ip]", value.customerPurchaseIp),
      write(s"$key[customer_signature]", value.customerSignature),
      write(s"$key[duplicate_charge_documentation]", value.duplicateChargeDocumentation),
      write(s"$key[duplicate_charge_explanation]", value.duplicateChargeExplanation),
      write(s"$key[duplicate_charge_id]", value.duplicateChargeId),
      write(s"$key[product_description]", value.productDescription),
      write(s"$key[receipt]", value.receipt),
      write(s"$key[refund_policy]", value.refundPolicy),
      write(s"$key[refund_policy_disclosure]", value.refundPolicyDisclosure),
      write(s"$key[refund_refusal_explanation]", value.refundRefusalExplanation),
      write(s"$key[service_date]", value.serviceDate),
      write(s"$key[service_documentation]", value.serviceDocumentation),
      write(s"$key[shipping_address]", value.shippingAddress),
      write(s"$key[shipping_carrier]", value.shippingCarrier),
      write(s"$key[shipping_date]", value.shippingDate),
      write(s"$key[shipping_documentation]", value.shippingDocumentation),
      write(s"$key[shipping_tracking_number]", value.shippingTrackingNumber),
      write(s"$key[uncategorized_file]", value.uncategorizedFile),
      write(s"$key[uncategorized_text]", value.uncategorizedText)
    ).flatten.toMap
  }
  protected implicit val packageDimensionsEncoder: MapEncoder[PackageDimensions] = new MapEncoder[PackageDimensions] {
    override def encode(key: String, value: PackageDimensions): Map[String, String] = List(
      write(s"$key[height]", value.height),
      write(s"$key[length]", value.length),
      write(s"$key[weight]", value.weight),
      write(s"$key[width]", value.width)
     ).flatten.toMap
  }
  protected implicit val recurringEncoder: MapEncoder[Recurring] = new MapEncoder[Recurring] {
    override def encode(key: String, value: Recurring): Map[String, String] = List(
      write(s"$key[interval]", value.interval),
      write(s"$key[aggregate_usage]", value.aggregateUsage),
      write(s"$key[interval_count]", value.intervalCount),
      write(s"$key[usage_type]", value.usageType)
    ).flatten.toMap
  }
  protected implicit val tierEncoder: MapEncoder[List[Tier]] = new MapEncoder[List[Tier]] {
    override def encode(key: String, value: List[Tier]): Map[String, String] = value.zipWithIndex.flatMap {
      case (tier, index) => List(
        write(s"$key[$index][up_to]", tier.upTo),
        write(s"$key[$index][flat_amount]", tier.flatAmount),
        write(s"$key[$index][flat_amount_decimal]", tier.flatAmountDecimal),
        write(s"$key[$index][unit_amount]", tier.unitAmount),
        write(s"$key[$index][unit_amount_decimal]", tier.unitAmountDecimal)
      ).flatten.toMap
    }.toMap
  }
  protected implicit val transformQuantityEncoder: MapEncoder[TransformQuantity] = new MapEncoder[TransformQuantity] {
    override def encode(key: String, value: TransformQuantity): Map[String, String] = List(
      write(s"$key[divide_by]", value.divideBy),
      write(s"$key[round]", value.round)
    ).flatten.toMap
  }
  protected implicit val stringListEncoder: MapEncoder[List[String]] = new MapEncoder[List[String]] {
    override def encode(key: String, value: List[String]): Map[String, String] = value.zipWithIndex.map {
      case (s, index) => s"$key[$index]" -> s
    }.toMap
  }
  protected implicit val mapListEncoder: MapEncoder[List[Map[String, String]]] = new MapEncoder[List[Map[String, String]]] {
    override def encode(key: String, value: List[Map[String, String]]): Map[String, String] = value.zipWithIndex.flatMap {
      case (map, index) => mapEncoder.encode(s"$key[$index]", map)
    }.toMap
  }
}