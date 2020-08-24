package com.outr.stripe.price

case class Recurring(aggregateUsage: Option[String],
                     interval: String,
                     intervalCount: Int,
                     usageType: String)