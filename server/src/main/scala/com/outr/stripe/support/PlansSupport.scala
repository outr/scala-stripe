package com.outr.stripe.support

import com.outr.stripe.subscription.Plan
import com.outr.stripe.{Deleted, Implicits, Money, QueryConfig, ResponseError, Stripe, StripeList, TimestampFilter}

import scala.concurrent.Future

class PlansSupport(stripe: Stripe) extends Implicits {
  def create(planId: String,
             amount: Money,
             currency: String,
             interval: String,
             name: String,
             intervalCount: Int = 1,
             metadata: Map[String, String] = Map.empty,
             statementDescriptor: Option[String] = None,
             trialPeriodDays: Option[Int] = None): Future[Either[ResponseError, Plan]] = {
    val data = List(
      write("id", planId),
      write("amount", amount),
      write("currency", currency),
      write("interval", interval),
      write("name", name),
      write("interval_count", intervalCount),
      write("statement_descriptor", statementDescriptor),
      write("trial_period_days", trialPeriodDays)
    ).flatten
    stripe.post[Plan]("plans", QueryConfig.default, data: _*)
  }

  def byId(planId: String): Future[Either[ResponseError, Plan]] = {
    stripe.get[Plan](s"plans/$planId", QueryConfig.default)
  }

  def update(planId: String,
             metadata: Map[String, String] = Map.empty,
             name: Option[String] = None,
             statementDescriptor: Option[String] = None,
             trialPeriodDays: Option[Int] = None): Future[Either[ResponseError, Plan]] = {
    val data = List(
      write("metadata", metadata),
      write("name", name),
      write("statement_descriptor", statementDescriptor),
      write("trial_period_days", trialPeriodDays)
    ).flatten
    stripe.post[Plan](s"plans/$planId", QueryConfig.default, data: _*)
  }

  def delete(planId: String): Future[Either[ResponseError, Deleted]] = {
    stripe.delete[Deleted](s"plans/$planId", QueryConfig.default)
  }

  def list(created: Option[TimestampFilter] = None,
           config: QueryConfig = QueryConfig.default): Future[Either[ResponseError, StripeList[Plan]]] = {
    val data = List(
      write("created", created)
    ).flatten
    stripe.get[StripeList[Plan]]("plans", config, data: _*)
  }
}
