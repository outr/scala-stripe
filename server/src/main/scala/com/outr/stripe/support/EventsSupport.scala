//package com.outr.stripe.support
//
//import com.outr.stripe.event.Event
//import com.outr.stripe.{Implicits, Pickler, QueryConfig, Stripe, StripeList, TimestampFilter}
//
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.Future
//
//class EventsSupport(stripe: Stripe) extends Implicits {
//  def byId(eventId: String): Future[Event] = {
//    stripe.get(s"events/$eventId", QueryConfig.default).map { response =>
//      Pickler.read[Event](response.body)
//    }
//  }
//
//  def list(created: Option[TimestampFilter] = None,
//           `type`: Option[String] = None,
//           types: List[String] = Nil,
//           config: QueryConfig = QueryConfig.default): Future[StripeList[Event]] = {
//    val data = List(
//      created.map("created" -> Pickler.write[TimestampFilter](_)),
//      `type`.map("type" -> _),
//      if (types.nonEmpty) Some("types" -> Pickler.write[List[String]](types)) else None
//    ).flatten
//    stripe.get("events", config, data: _*).map { response =>
//      Pickler.read[StripeList[Event]](response.body)
//    }
//  }
//}
