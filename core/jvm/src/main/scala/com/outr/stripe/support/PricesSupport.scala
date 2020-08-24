package com.outr.stripe.support

import com.outr.stripe._
import com.outr.stripe.price.{Price, Recurring, Tier, TransformQuantity}

import scala.concurrent.Future

class PricesSupport(stripe: Stripe) extends Implicits {
  def create(currency: String,
             active: Option[Boolean] = None,
             billingScheme: Option[String] = None,
             lookupKey: Option[String] = None,
             metadata: Map[String, String] = Map.empty,
             nickname: Option[String] = None,
             recurring: Option[Recurring] = None,
             tiers: List[Tier] = List(),
             tiersMode: Option[String] = None,
             transferLookupKey: Option[Boolean] = None,
             transformQuantity: Option[TransformQuantity] = None,
             unitAmount: Option[Int] = None,
             unitAmountDecimal: Option[BigDecimal] = None): Future[Either[ResponseError, Price]] = {
    val data = List(
      write("active", active),
      write("billing_scheme", billingScheme),
      write("currency", currency),
      write("lookup_key", lookupKey),
      write("metadata", metadata),
      write("nickname", nickname),
      write("recurring", recurring),
      write("tiers", tiers),
      write("tiers_mode", tiersMode),
      write("transfer_lookup_key", transferLookupKey),
      write("transform_quantity", transformQuantity),
      write("unit_amount", unitAmount),
      write("unit_amount_decimal", unitAmountDecimal)
    ).flatten
    stripe.post[Price]("prices", QueryConfig.default, data: _*)
  }

  def byId(priceId: String): Future[Either[ResponseError, Price]] = {
    stripe.get[Price](s"prices/$priceId", QueryConfig.default)
  }

  def update(priceId: String,
             active: Option[Boolean] = None,
             lookupKey: Option[String] = None,
             metadata: Map[String, String] = Map.empty,
             nickname: Option[String] = None,
             transferLookupKey: Option[Boolean] = None): Future[Either[ResponseError, Price]] = {
    val data = List(
      write("active", active),
      write("lookup_key", lookupKey),
      write("metadata", metadata),
      write("nickname", nickname),
      write("transfer_lookup_key", transferLookupKey)
    ).flatten
    stripe.post[Price](s"prices/$priceId", QueryConfig.default, data: _*)
  }

  def delete(priceId: String): Future[Either[ResponseError, Deleted]] = {
    stripe.delete[Deleted](s"prices/$priceId", QueryConfig.default)
  }

  def list(active: Option[Boolean] = None,
           currency: Option[String] = None,
           created: Option[TimestampFilter] = None,
           config: QueryConfig = QueryConfig.default,
           endingBefore: Option[String] = None,
           limit: Option[Int] = None,
           productId: Option[String] = None,
           `type`: Option[String] = None): Future[Either[ResponseError, StripeList[Price]]] = {
    val data = List(
      write("active", active),
      write("created", created),
      write("currency", currency),
      write("ending_before", endingBefore),
      write("limit", limit),
      write("product", productId),
      write("type", `type`)
    ).flatten
    stripe.get[StripeList[Price]]("prices", config, data: _*)
  }
}
