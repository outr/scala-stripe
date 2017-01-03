package com.outr.stripe.subscription

import com.outr.stripe.Money

case class InvoiceLine(id: String,
                       `object`: String,
                       amount: Money,
                       currency: String,
                       description: String,
                       livemode: Boolean,
                       metadata: Map[String, String],
                       period: Period,
                       plan: Plan,
                       proration: Boolean,
                       quantity: Option[Int],
                       subscription: Option[String],
                       `type`: String)