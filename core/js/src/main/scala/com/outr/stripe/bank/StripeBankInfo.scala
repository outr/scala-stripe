package com.outr.stripe.bank

import scala.scalajs.js

trait StripeBankInfo extends js.Object {
  var country: js.UndefOr[String] = js.undefined
  var currency: js.UndefOr[String] = js.undefined
  var routing_number: js.UndefOr[String] = js.undefined
  var account_number: js.UndefOr[String] = js.undefined
  var account_holder_name: js.UndefOr[String] = js.undefined
  var account_holder_type: js.UndefOr[String] = js.undefined
}
