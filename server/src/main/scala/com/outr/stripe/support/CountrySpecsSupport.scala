package com.outr.stripe.support

import com.outr.stripe.connect.CountrySpec
import com.outr.stripe.{Implicits, QueryConfig, ResponseError, Stripe, StripeList}

import scala.concurrent.Future

class CountrySpecsSupport(stripe: Stripe) extends Implicits {
  def list(config: QueryConfig = QueryConfig.default): Future[Either[ResponseError, StripeList[CountrySpec]]] = {
    stripe.get[StripeList[CountrySpec]]("country_specs", config)
  }

  def byId(countryCode: String): Future[Either[ResponseError, CountrySpec]] = {
    stripe.get[CountrySpec](s"country_specs/$countryCode", QueryConfig.default)
  }
}
