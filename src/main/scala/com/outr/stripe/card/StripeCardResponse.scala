package com.outr.stripe.card

import scala.scalajs.js

@js.native
trait StripeCardResponse extends js.Object {
  def name: String = js.native
  def address_line1: String = js.native
  def address_line2: String = js.native
  def address_city: String = js.native
  def address_state: String = js.native
  def address_zip: String = js.native
  def address_country: String = js.native
  def country: String = js.native
  def exp_month: Int = js.native
  def exp_year: Int = js.native
  def last4: String = js.native
  def `object`: String = js.native
  def brand: String = js.native
  def funding: String = js.native
}
