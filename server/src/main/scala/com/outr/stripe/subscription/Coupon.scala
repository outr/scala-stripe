package com.outr.stripe.subscription

import com.outr.stripe.Money

case class Coupon(id: String,
                  `object`: String,
                  amountOff: Option[Money],
                  created: Long,
                  currency: String,
                  duration: String,
                  durationInMonths: Option[Int],
                  livemode: Boolean,
                  maxRedemptions: Option[Int],
                  metadata: Map[String, String],
                  percentOff: BigDecimal,
                  redeemBy: Option[Long],
                  timesRedeemed: Int,
                  valid: Boolean)
