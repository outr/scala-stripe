package com.outr.stripe.subscription

case class CreateSubscriptionItem(billingThresholds: Map[String, String],
                                  metadata: Map[String, String],
                                  price: Option[String],
                                  quantity: Option[Int],
                                  taxRates: List[String])