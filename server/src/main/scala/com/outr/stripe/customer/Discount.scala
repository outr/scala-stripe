package com.outr.stripe.customer

import com.outr.stripe.subscription.Coupon

case class Discount(`object`: String,
                    coupon: Coupon,
                    customer: String,
                    end: Option[Long],
                    start: Long,
                    subscription: Option[String])
