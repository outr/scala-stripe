package com.outr.stripe.support

import com.outr.stripe.connect.ApplicationFee
import com.outr.stripe.{Implicits, QueryConfig, ResponseError, Stripe, StripeList, TimestampFilter}

import scala.concurrent.Future

class ApplicationFeesSupport(stripe: Stripe) extends Implicits {
  def byId(feeId: String): Future[Either[ResponseError, ApplicationFee]] = {
    stripe.get[ApplicationFee](s"application_fees/$feeId", QueryConfig.default)
  }

  def list(charge: Option[String] = None,
           created: Option[TimestampFilter] = None,
           config: QueryConfig = QueryConfig.default): Future[Either[ResponseError, StripeList[ApplicationFee]]] = {
    stripe.get[StripeList[ApplicationFee]]("application_fees", config)
  }

  lazy val refunds: ApplicationFeeRefundsSupport = new ApplicationFeeRefundsSupport(stripe)
}
