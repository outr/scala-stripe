package com.outr.stripe.subscription

import com.outr.stripe.Money

case class Plan(id: String,
                `object`: String,
                amount: Money,
                created: Long,
                currency: String,
                interval: String,
                intervalCount: Int,
                livemode: Boolean,
                metadata: Map[String, String],
                name: String,
                statementDescriptor: Option[String],
                trialPeriodDays: Option[Int])
