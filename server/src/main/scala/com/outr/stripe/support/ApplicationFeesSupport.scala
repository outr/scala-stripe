//package com.outr.stripe.support
//
//import com.outr.stripe.connect.ApplicationFee
//import com.outr.stripe.{Implicits, Pickler, QueryConfig, Stripe, StripeList, TimestampFilter}
//
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.Future
//
//class ApplicationFeesSupport(stripe: Stripe) extends Implicits {
//  def byId(feeId: String): Future[ApplicationFee] = {
//    stripe.get(s"application_fees/$feeId", QueryConfig.default).map { response =>
//      Pickler.read[ApplicationFee](response.body)
//    }
//  }
//
//  def list(charge: Option[String] = None,
//           created: Option[TimestampFilter] = None,
//           config: QueryConfig = QueryConfig.default): Future[StripeList[ApplicationFee]] = {
//    stripe.get("application_fees", config).map { response =>
//      Pickler.read[StripeList[ApplicationFee]](response.body)
//    }
//  }
//
//  lazy val refunds: ApplicationFeeRefundsSupport = new ApplicationFeeRefundsSupport(stripe)
//}
