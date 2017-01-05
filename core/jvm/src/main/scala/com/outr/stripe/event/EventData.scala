package com.outr.stripe.event

import io.circe.Json

case class EventData(`object`: Json, previousAttributes: Map[String, String])
