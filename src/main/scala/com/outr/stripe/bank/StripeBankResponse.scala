package com.outr.stripe.bank

import scala.scalajs.js

@js.native
trait StripeBankResponse extends js.Object {
  def country: String = js.native
  def bank_name: String = js.native
  def last4: String = js.native
  def validated: Boolean = js.native
  def `object`: String = js.native
}
