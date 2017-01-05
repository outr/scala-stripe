//package com.outr.stripe.support
//
//import com.outr.stripe.connect.FeeRefund
//import com.outr.stripe.{Implicits, Money, Pickler, QueryConfig, Stripe, StripeList}
//
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.Future
//
//class ApplicationFeeRefundsSupport(stripe: Stripe) extends Implicits {
//  def create(feeId: String,
//             amount: Option[Money] = None,
//             metadata: Map[String, String] = Map.empty): Future[FeeRefund] = {
//    val data = List(
//      amount.map("amount" -> Pickler.write(_)),
//      if (metadata.nonEmpty) Some("metadata" -> Pickler.write(metadata)) else None
//    ).flatten
//    stripe.post(s"application_fees/$feeId/refunds", QueryConfig.default, data: _*).map { response =>
//      Pickler.read[FeeRefund](response.body)
//    }
//  }
//
//  def byId(feeId: String, refundId: String): Future[FeeRefund] = {
//    stripe.get(s"/application_fees/$feeId/refunds/$refundId", QueryConfig.default).map { response =>
//      Pickler.read[FeeRefund](response.body)
//    }
//  }
//
//  def update(feeId: String, refundId: String, metadata: Map[String, String] = Map.empty): Future[FeeRefund] = {
//    val data = List(
//      if (metadata.nonEmpty) Some("metadata" -> Pickler.write(metadata)) else None
//    ).flatten
//    stripe.post(s"application_fees/$feeId/refunds/$refundId", QueryConfig.default, data: _*).map { response =>
//      Pickler.read[FeeRefund](response.body)
//    }
//  }
//
//  def list(feeId: String, config: QueryConfig = QueryConfig.default): Future[StripeList[FeeRefund]] = {
//    stripe.get(s"application_fees/$feeId/refunds", config).map { response =>
//      Pickler.read[StripeList[FeeRefund]](response.body)
//    }
//  }
//}