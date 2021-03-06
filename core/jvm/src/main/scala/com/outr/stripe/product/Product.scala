package com.outr.stripe.product

case class Product(id: String,
                   `object`: String,
                   active: Boolean,
                   attributes: List[String],
                   caption: Option[String],
                   created: Long,
                   deactivateOn: List[String],
                   description: Option[String],
                   images: List[String],
                   liveMode: Boolean,
                   metadata: Map[String, String],
                   name: String,
                   packageDimensions: Option[PackageDimensions],
                   shippable: Boolean,
                   statementDescriptor: Option[String],
                   `type`: String,
                   unitLabel: Option[String],
                   updated: Long,
                   url: Option[String])