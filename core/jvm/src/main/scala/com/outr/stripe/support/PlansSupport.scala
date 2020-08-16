package com.outr.stripe.support

import com.outr.stripe.subscription.Plan
import com.outr.stripe.{Deleted, Implicits, Money, QueryConfig, ResponseError, Stripe, StripeList, TimestampFilter}

import scala.concurrent.Future

class PlansSupport(stripe: Stripe) extends Implicits {
  def create(planId: String,
             amount: Money,
             currency: String,
             interval: String,
             productId: String,
             intervalCount: Int = 1,
             metadata: Map[String, String] = Map.empty,
             nickname: Option[String],
             trialPeriodDays: Option[Int] = None): Future[Either[ResponseError, Plan]] = {
    val data = List(
      write("id", planId),
      write("amount", amount),
      write("currency", currency),
      write("interval", interval),
      write("product", productId),
      write("interval_count", intervalCount),
      write("metadata", metadata),
      write("trial_period_days", trialPeriodDays),
      write("nickname", nickname)
    ).flatten
    stripe.post[Plan]("plans", QueryConfig.default, data: _*)
  }

  def byId(planId: String): Future[Either[ResponseError, Plan]] = {
    stripe.get[Plan](s"plans/$planId", QueryConfig.default)
  }

  def update(planId: String,
             metadata: Map[String, String] = Map.empty,
             name: Option[String] = None,
             productId: Option[String] = None,
             statementDescriptor: Option[String] = None,
             trialPeriodDays: Option[Int] = None): Future[Either[ResponseError, Plan]] = {
    val data = List(
      write("metadata", metadata),
      write("name", name),
      write("product", productId),
      write("statement_descriptor", statementDescriptor),
      write("trial_period_days", trialPeriodDays)
    ).flatten
    stripe.post[Plan](s"plans/$planId", QueryConfig.default, data: _*)
  }

  def delete(planId: String): Future[Either[ResponseError, Deleted]] = {
    stripe.delete[Deleted](s"plans/$planId", QueryConfig.default)
  }

  def list(active: Option[Boolean] = None,
           created: Option[TimestampFilter] = None,
           config: QueryConfig = QueryConfig.default,
           productId: Option[String] = None): Future[Either[ResponseError, StripeList[Plan]]] = {
    val data = List(
      write("active", active),
      write("created", created),
      write("product", productId)
    ).flatten
    stripe.get[StripeList[Plan]]("plans", config, data: _*)
  }
}
