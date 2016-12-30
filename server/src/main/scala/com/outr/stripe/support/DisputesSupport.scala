package com.outr.stripe.support

import com.outr.stripe.dispute.{Dispute, DisputeEvidence}
import com.outr.stripe.{Implicits, Pickler, QueryConfig, Stripe, StripeList, TimestampFilter}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DisputesSupport(stripe: Stripe) extends Implicits {
  def byId(disputeId: String): Future[Dispute] = {
    stripe.get(s"disputes/$disputeId", QueryConfig.default).map { response =>
      Pickler.read[Dispute](response.body)
    }
  }

  def update(disputeId: String,
             evidence: Option[DisputeEvidence] = None,
             metadata: Map[String, String]): Future[Dispute] = {
    val data = List(
      evidence.map("evidence" -> Pickler.write[DisputeEvidence](_)),
      if (metadata.nonEmpty) Some("metadata" -> Pickler.write[Map[String, String]](metadata)) else None
    ).flatten
    stripe.post(s"disputes/$disputeId", QueryConfig.default, data: _*).map { response =>
      Pickler.read[Dispute](response.body)
    }
  }

  def close(disputeId: String): Future[Dispute] = {
    stripe.post(s"disputes/$disputeId/close", QueryConfig.default).map { response =>
      Pickler.read[Dispute](response.body)
    }
  }

  def list(created: Option[TimestampFilter] = None,
           config: QueryConfig = QueryConfig.default): Future[StripeList[Dispute]] = {
    val data = List(
      created.map("created" -> Pickler.write[TimestampFilter](_))
    ).flatten
    stripe.get("disputes", config, data: _*).map { response =>
      Pickler.read[StripeList[Dispute]](response.body)
    }
  }
}
