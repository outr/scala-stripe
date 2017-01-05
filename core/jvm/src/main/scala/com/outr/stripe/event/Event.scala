package com.outr.stripe.event

case class Event(id: String,
                 `object`: String,
                 apiVersion: Option[String],
                 created: Long,
                 data: EventData,
                 livemode: Boolean,
                 pendingWebhooks: Int,
                 request: Option[String],
                 `type`: String)
