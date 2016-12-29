package com.outr.stripe.customer

import com.outr.stripe.charge.{Card, Shipping}
import com.outr.stripe.subscription.Subscription
import com.outr.stripe.{Money, StripeList}

case class Customer(id: String,
                    `object`: String,
                    accountBalance: Money,
                    created: Long,
                    currency: String,
                    defaultSource: Option[String],
                    delinquent: Boolean,
                    description: Option[String],
                    discount: Option[Discount],
                    email: Option[String],
                    livemode: Boolean,
                    metadata: Map[String, String],
                    shipping: Option[Shipping],
                    sources: StripeList[Card],
                    subscriptions: StripeList[Subscription])
