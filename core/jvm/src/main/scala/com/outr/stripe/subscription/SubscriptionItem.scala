package com.outr.stripe.subscription

case class SubscriptionItem(id: String,
                            `object`: String,
                            billingThresholds: Map[String, String],
                            created: Long,
                            metadata: Map[String, String],
                            paymentBehavior: Option[String],
                            price: Option[String],
                            prorationBehavior: Option[String],
                            prorationDate: Option[Long],
                            quantity: Option[Int],
                            taxRates: List[String])