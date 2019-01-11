package com.outr.stripe.connect

case class Verification(details: Option[String],
                        detailsCode: Option[String],
                        document: Option[String],
                        status: Option[String])
