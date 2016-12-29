package com.outr.stripe.charge

case class Shipping(address: Option[Address],
                    carrier: Option[String],
                    name: Option[String],
                    phone: Option[String],
                    trackingNumber: Option[String])
