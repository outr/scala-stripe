package com.outr.stripe.bank

import com.outr.stripe.TokenError

import scala.scalajs.js

@js.native
trait BankTokenInfo extends js.Object {
  def id: String = js.native
  def bank_account: StripeBankResponse = js.native
  def created: Long = js.native
  def livemode: Boolean = js.native
  def `type`: String = js.native
  def `object`: String = js.native
  def used: Boolean = js.native
  def error: TokenError = js.native
}
