package com.outr.stripe.price

case class Price(id: String,
                 `object`: String,
                 active: Boolean,
                 billingScheme: Option[String],
                 created: Long,
                 currency: String,
                 livemode: Boolean,
                 lookupKey: Option[String],
                 metadata: Map[String, String],
                 nickname: String,
                 product: String,
                 recurring: Option[Recurring],
                 tiers: List[Tier],
                 tiersMode: Option[String],
                 transformQuantity: Option[TransformQuantity],
                 unitAmount: Int,
                 unitAmountDecimal: Option[BigDecimal])