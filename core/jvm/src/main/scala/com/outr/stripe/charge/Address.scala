package com.outr.stripe.charge

case class Address(city: Option[String],
                   country: String,
                   line1: Option[String],
                   line2: Option[String],
                   postalCode: Option[String],
                   state: Option[String])
