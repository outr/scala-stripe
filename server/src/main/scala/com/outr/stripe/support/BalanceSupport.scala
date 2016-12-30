package com.outr.stripe.support

import com.outr.stripe.{Implicits, Pickler, QueryConfig, Stripe, StripeList, TimestampFilter}
import com.outr.stripe.balance.{Balance, BalanceTransaction}

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

class BalanceSupport(stripe: Stripe) extends Implicits {
  def apply(): Future[Balance] = stripe.get("balance", QueryConfig.default).map { response =>
    Pickler.read[Balance](response.body)
  }

  def byId(id: String, config: QueryConfig = QueryConfig.default): Future[BalanceTransaction] = {
    stripe.get(s"balance/history/$id", QueryConfig.default).map { response =>
      Pickler.read[BalanceTransaction](response.body)
    }
  }

  def list(availableOn: Option[TimestampFilter] = None,
           created: Option[TimestampFilter] = None,
           currency: Option[String] = None,
           source: Option[String] = None,
           transfer: Option[String] = None,
           `type`: Option[String] = None,
           config: QueryConfig = QueryConfig.default): Future[StripeList[BalanceTransaction]] = {
    val data = List(
      availableOn.map("available_on" -> Pickler.write[TimestampFilter](_)),
      created.map("created" -> Pickler.write[TimestampFilter](_)),
      currency.map("currency" -> _),
      source.map("source" -> _),
      transfer.map("transfer" -> _),
      `type`.map("type" -> _)
    ).flatten
    stripe.get("balance/history", config, data: _*).map { response =>
      Pickler.read[StripeList[BalanceTransaction]](response.body)
    }
  }
}
