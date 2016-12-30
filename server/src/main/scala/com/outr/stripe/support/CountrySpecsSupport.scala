package com.outr.stripe.support

import com.outr.stripe.connect.CountrySpec
import com.outr.stripe.{Implicits, Pickler, QueryConfig, Stripe, StripeList}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CountrySpecsSupport(stripe: Stripe) extends Implicits {
  def list(config: QueryConfig = QueryConfig.default): Future[StripeList[CountrySpec]] = {
    stripe.get("country_specs", config).map { response =>
      Pickler.read[StripeList[CountrySpec]](response.body)
    }
  }

  def byId(countryCode: String): Future[CountrySpec] = {
    stripe.get(s"country_specs/$countryCode", QueryConfig.default).map { response =>
      Pickler.read[CountrySpec](response.body)
    }
  }
}
