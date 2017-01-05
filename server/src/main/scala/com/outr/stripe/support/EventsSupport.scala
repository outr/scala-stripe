package com.outr.stripe.support

import com.outr.stripe.event.Event
import com.outr.stripe.{Implicits, Pickler, QueryConfig, ResponseError, Stripe, StripeList, TimestampFilter}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class EventsSupport(stripe: Stripe) extends Implicits {
  def byId(eventId: String): Future[Either[ResponseError, Event]] = {
    stripe.get[Event](s"events/$eventId", QueryConfig.default)
  }

  def list(created: Option[TimestampFilter] = None,
           `type`: Option[String] = None,
           types: List[String] = Nil,
           config: QueryConfig = QueryConfig.default): Future[Either[ResponseError, StripeList[Event]]] = {
    val data = List(
      write("created", created),
      write("type", `type`),
      write("types", types)
    ).flatten
    stripe.get[StripeList[Event]]("events", config, data: _*)
  }
}
