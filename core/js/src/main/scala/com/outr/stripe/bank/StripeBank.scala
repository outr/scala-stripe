package com.outr.stripe.bank

import scala.scalajs.js

@js.native
trait StripeBank extends js.Object {
  def createToken(info: StripeBankInfo, responseHandler: js.Function2[Int, BankTokenInfo, Unit]): Unit = js.native
  def validateRoutingNumber(number: String, country: String): Boolean = js.native
  def validateAccountNumber(number: String, country: String): Boolean = js.native
}