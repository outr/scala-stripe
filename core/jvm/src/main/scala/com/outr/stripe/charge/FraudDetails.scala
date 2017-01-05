package com.outr.stripe.charge

case class FraudDetails(userReport: Option[String],
                        safe: Option[String],
                        fraudulent: Option[String],
                        stripeReport: Option[String])
