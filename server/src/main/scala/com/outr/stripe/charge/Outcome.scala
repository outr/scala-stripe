package com.outr.stripe.charge

case class Outcome(networkStatus: Option[String],
                   reason: Option[String],
                   riskLevel: Option[String],
                   rule: Option[Rule],
                   sellerMessage: Option[String],
                   `type`: Option[String])
