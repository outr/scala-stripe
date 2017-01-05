package com.outr.stripe.subscription

import com.outr.stripe.Money

case class InvoiceItem(id: String,
                       `object`: String,
                       amount: Money,
                       currency: String,
                       customer: String,
                       date: Long,
                       description: String,
                       discountable: Boolean,
                       invoice: Option[String],
                       livemode: Boolean,
                       metadata: Map[String, String],
                       period: Period,
                       plan: Option[Plan],
                       proration: Boolean,
                       quantity: Option[Int],
                       subscription: Option[String])