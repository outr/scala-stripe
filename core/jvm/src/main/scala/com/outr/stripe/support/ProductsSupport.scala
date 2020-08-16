package com.outr.stripe.support

import com.outr.stripe._
import com.outr.stripe.product.PackageDimensions
import com.outr.stripe.product.{Product => StripeProduct}
import scala.concurrent.Future

class ProductsSupport(stripe: Stripe) extends Implicits {
  def create(productId: String,
             name: String,
             active: Option[Boolean] = None,
             attributes: List[String] = List.empty,
             caption: Option[String] = None,
             deactivateOn: List[String] = List.empty,
             description: Option[String] = None,
             images: List[String] = List.empty,
             liveMode: Option[Boolean] = None,
             metadata: Map[String, String] = Map.empty,
             packageDimensions: Option[PackageDimensions] = None,
             shippable: Option[Boolean] = None,
             statementDescriptor: Option[String] = None,
             `type`: Option[String] = None,
             unitLabel: Option[String] = None,
             url: Option[String] = None): Future[Either[ResponseError, Product]] = {
    val data = List(
      write("id", productId),
      write("name", name),
      write("active", active),
      write("attributes", attributes),
      write("caption", caption),
      write("deactivate_on", deactivateOn),
      write("description", description),
      write("images", images),
      write("livemode", liveMode),
      write("metadata", metadata),
      write("package_dimensions", packageDimensions),
      write("shippable", shippable),
      write("statement_descriptor", statementDescriptor),
      write("type", `type`),
      write("unit_label", unitLabel),
      write("url", url),
    ).flatten
    stripe.post[StripeProduct]("products", QueryConfig.default, data: _*)
  }

  def byId(productId: String): Future[Either[ResponseError, StripeProduct]] = {
    stripe.get[StripeProduct](s"products/$productId", QueryConfig.default)
  }

  def update(productId: String,
             active: Option[Boolean] = None,
             attributes: List[String] = List.empty,
             caption: Option[String] = None,
             deactivateOn: List[String] = List.empty,
             description: Option[String] = None,
             images: List[String] = List.empty,
             liveMode: Option[Boolean] = None,
             metadata: Map[String, String] = Map.empty,
             name: Option[String] = None,
             packageDimensions: Option[PackageDimensions] = None,
             shippable: Option[Boolean] = None,
             statementDescriptor: Option[String] = None,
             `type`: Option[String] = None,
             unitLabel: Option[String] = None,
             url: Option[String] = None): Future[Either[ResponseError, StripeProduct]] = {
    val data = List(
      write("id", productId),
      write("name", name),
      write("active", active),
      write("attributes", attributes),
      write("caption", caption),
      write("deactivate_on", deactivateOn),
      write("description", description),
      write("images", images),
      write("livemode", liveMode),
      write("metadata", metadata),
      write("package_dimensions", packageDimensions),
      write("shippable", shippable),
      write("statement_descriptor", statementDescriptor),
      write("type", `type`),
      write("unit_label", unitLabel),
      write("url", url)
    ).flatten
    stripe.post[StripeProduct](s"products/$productId", QueryConfig.default, data: _*)
  }

  def delete(productId: String): Future[Either[ResponseError, Deleted]] = {
    stripe.delete[Deleted](s"products/$productId", QueryConfig.default)
  }

  def list(active: Option[Boolean] = None,
           created: Option[TimestampFilter] = None,
           config: QueryConfig = QueryConfig.default,
           ids: List[String] = Nil,
           shippable: Option[Boolean] = None,
           `type`: Option[String] = None,
           url: Option[String] = None): Future[Either[ResponseError, StripeList[StripeProduct]]] = {
    val data = List(
      write("active", active),
      write("created", created),
      write("ids", ids),
      write("shippable", shippable),
      write("type", `type`),
      write("url", url)
    ).flatten
    stripe.get[StripeList[StripeProduct]]("products", config, data: _*)
  }
}
