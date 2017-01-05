package com.outr.stripe.support

import com.outr.stripe.dispute.{Dispute, DisputeEvidence}
import com.outr.stripe.{Implicits, QueryConfig, ResponseError, Stripe, StripeList, TimestampFilter}

import scala.concurrent.Future

class DisputesSupport(stripe: Stripe) extends Implicits {
  def byId(disputeId: String): Future[Either[ResponseError, Dispute]] = {
    stripe.get[Dispute](s"disputes/$disputeId", QueryConfig.default)
  }

  def update(disputeId: String,
             evidence: Option[DisputeEvidence] = None,
             metadata: Map[String, String]): Future[Either[ResponseError, Dispute]] = {
    val data = List(
      write("evidence", evidence),
      write("metadata", metadata)
    ).flatten
    stripe.post[Dispute](s"disputes/$disputeId", QueryConfig.default, data: _*)
  }

  def close(disputeId: String): Future[Either[ResponseError, Dispute]] = {
    stripe.post[Dispute](s"disputes/$disputeId/close", QueryConfig.default)
  }

  def list(created: Option[TimestampFilter] = None,
           config: QueryConfig = QueryConfig.default): Future[Either[ResponseError, StripeList[Dispute]]] = {
    val data = List(
      write("created", created)
    ).flatten
    stripe.get[StripeList[Dispute]]("disputes", config, data: _*)
  }
}
