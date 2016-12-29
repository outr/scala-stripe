package com.outr.stripe

import com.outr.stripe.balance.{Balance, BalanceEntry, BalanceTransaction, FeeDetail, Reversal, SourceType, SourcedTransfers, Transfer}
import com.outr.stripe.event.{Event, EventData}
import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.semiauto._

trait Implicits {
  // Decoders

  implicit val moneyDecoder = new Decoder[Money] {
    override def apply(c: HCursor): Result[Money] = {
      Decoder.decodeLong(c).map(l => Money(l))
    }
  }
  implicit val balanceDecoder: Decoder[Balance] = deriveDecoder[Balance]
  implicit val balanceTransactionDecoder: Decoder[BalanceTransaction] = deriveDecoder[BalanceTransaction]
  implicit val balanceEntryDecoder: Decoder[BalanceEntry] = deriveDecoder[BalanceEntry]
  implicit val sourceTypeDecoder: Decoder[SourceType] = deriveDecoder[SourceType]
  implicit val feeDetailDecoder: Decoder[FeeDetail] = deriveDecoder[FeeDetail]
  implicit val sourcedTransfersDecoder: Decoder[SourcedTransfers] = deriveDecoder[SourcedTransfers]
  implicit val transferDecoder: Decoder[Transfer] = deriveDecoder[Transfer]
  implicit val reversalDecoder: Decoder[Reversal] = deriveDecoder[Reversal]
  implicit val eventDecoder: Decoder[Event] = deriveDecoder[Event]
  implicit val eventDataDecoder: Decoder[EventData] = deriveDecoder[EventData]

  // Encoders

  implicit val moneyEncoder: Encoder[Money] = new Encoder[Money] {
    override def apply(a: Money): Json = Encoder.encodeLong(a.pennies)
  }
}
