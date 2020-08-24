package com.outr.stripe.customer

import com.outr.stripe.charge.{Address, Card, Shipping}
import com.outr.stripe.subscription.Subscription
import com.outr.stripe.{Money, StripeList}

case class Customer(id: String,
                    `object`: String,
                    accountBalance: Money,
                    address: Option[Address],
                    balance: Option[Int],
                    created: Long,
                    currency: Option[String],
                    defaultSource: Option[String],
                    delinquent: Boolean,
                    description: Option[String],
                    discount: Option[Discount],
                    email: Option[String],
                    invoicePrefix: Option[String],
                    livemode: Boolean,
                    metadata: Map[String, String],
                    name: Option[String],
                    nextInvoiceSequence: Option[Int],
                    phone: Option[String],
                    shipping: Option[Shipping],
                    sources: StripeList[Card],
                    subscriptions: StripeList[Subscription],
                    taxExempt: Option[String])
