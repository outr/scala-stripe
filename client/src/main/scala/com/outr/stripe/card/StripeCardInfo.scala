package com.outr.stripe.card

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

@ScalaJSDefined
trait StripeCardInfo extends js.Object {
  var name: js.UndefOr[String] = js.undefined
  var number: js.UndefOr[String] = js.undefined
  var cvc: js.UndefOr[String] = js.undefined
  var exp_month: js.UndefOr[Int] = js.undefined
  var exp_year: js.UndefOr[Int] = js.undefined
  var address_line1: js.UndefOr[String] = js.undefined
  var address_line2: js.UndefOr[String] = js.undefined
  var address_city: js.UndefOr[String] = js.undefined
  var address_state: js.UndefOr[String] = js.undefined
  var address_zip: js.UndefOr[String] = js.undefined
  var address_country: js.UndefOr[String] = js.undefined
}
