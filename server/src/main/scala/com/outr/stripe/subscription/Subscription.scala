package com.outr.stripe.subscription

import com.outr.stripe.customer.Discount

case class Subscription(id: String,
                        `object`: String,
                        applicationFeePercent: Option[BigDecimal],
                        cancelAtPeriodEnd: Boolean,
                        canceledAt: Option[Long],
                        created: Long,
                        currentPeriodEnd: Long,
                        currentPeriodStart: Long,
                        customer: String,
                        discount: Option[Discount],
                        endedAt: Option[Long],
                        livemode: Boolean,
                        metadata: Map[String, String],
                        plan: Plan,
                        quantity: Int,
                        start: Long,
                        status: String,
                        taxPercent: Option[BigDecimal],
                        trialEnd: Option[Long],
                        trialStart: Option[Long])
