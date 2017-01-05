package com.outr.stripe.support

import com.outr.stripe.balance.{Balance, BalanceTransaction}
import com.outr.stripe.{Implicits, QueryConfig, ResponseError, Stripe, StripeList, TimestampFilter}

import scala.concurrent.Future

class BalanceSupport(stripe: Stripe) extends Implicits {
  def apply(): Future[Either[ResponseError, Balance]] = {
    stripe.get[Balance]("balance", QueryConfig.default)
  }

  def byId(id: String, config: QueryConfig = QueryConfig.default): Future[Either[ResponseError, BalanceTransaction]] = {
    stripe.get[BalanceTransaction](s"balance/history/$id", QueryConfig.default)
  }

  def list(availableOn: Option[TimestampFilter] = None,
           created: Option[TimestampFilter] = None,
           currency: Option[String] = None,
           source: Option[String] = None,
           transfer: Option[String] = None,
           `type`: Option[String] = None,
           config: QueryConfig = QueryConfig.default): Future[Either[ResponseError, StripeList[BalanceTransaction]]] = {
    val data = List(
      write("available_on", availableOn),
      write("created", created),
      write("currency", currency),
      write("source", source),
      write("transfer", transfer),
      write("type", `type`)
    ).flatten
    stripe.get[StripeList[BalanceTransaction]]("balance/history", config, data: _*)
  }
}
