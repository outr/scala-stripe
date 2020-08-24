package com.outr.stripe.price

case class Tier(flatAmount: Option[Int],
                flatAmountDecimal: Option[BigDecimal],
                unitAmount: Option[Int],
                unitAmountDecimal: Option[BigDecimal],
                upTo: Option[Int])