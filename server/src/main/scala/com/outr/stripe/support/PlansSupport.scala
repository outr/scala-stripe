package com.outr.stripe.support

import com.outr.stripe.subscription.Plan
import com.outr.stripe.{Deleted, Implicits, Money, Pickler, QueryConfig, Stripe, StripeList, TimestampFilter}

import scala.concurrent.ExecutionContext.Implicits.global
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
             trialPeriodDays: Option[Int] = None): Future[Plan] = {
    val data = List(
      Some("id" -> planId),
      Some("amount" -> amount.pennies.toString),
      Some("currency" -> currency),
      Some("interval" -> interval),
      Some("name" -> name),
      Some("intervalCount" -> intervalCount.toString),
      if (metadata.nonEmpty) Some("metadata" -> Pickler.write(metadata)) else None,
      statementDescriptor.map("statement_descriptor" -> _),
      trialPeriodDays.map("trial_period_days" -> _.toString)
    ).flatten
    stripe.post("plans", QueryConfig.default, data: _*).map { response =>
      Pickler.read[Plan](response.body)
    }
  }

  def byId(planId: String): Future[Plan] = {
    stripe.get(s"plans/$planId", QueryConfig.default).map { response =>
      Pickler.read[Plan](response.body)
    }
  }

  def update(planId: String,
             metadata: Map[String, String] = Map.empty,
             name: Option[String] = None,
             statementDescriptor: Option[String] = None,
             trialPeriodDays: Option[Int] = None): Future[Plan] = {
    val data = List(
      if (metadata.nonEmpty) Some("metadata" -> Pickler.write(metadata)) else None,
      name.map("name" -> _),
      statementDescriptor.map("statement_descriptor" -> _),
      trialPeriodDays.map("trial_period_days" -> _.toString)
    ).flatten
    stripe.post(s"plans/$planId", QueryConfig.default, data: _*).map { response =>
      Pickler.read[Plan](response.body)
    }
  }

  def delete(planId: String): Future[Deleted] = {
    stripe.delete(s"plans/$planId", QueryConfig.default).map { response =>
      Pickler.read[Deleted](response.body)
    }
  }

  def list(created: Option[TimestampFilter] = None,
           config: QueryConfig = QueryConfig.default): Future[StripeList[Plan]] = {
    val data = List(
      created.map("created" -> Pickler.write(_))
    ).flatten
    stripe.get("plans", config, data: _*).map { response =>
      Pickler.read[StripeList[Plan]](response.body)
    }
  }
}
