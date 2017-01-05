package com.outr.stripe.card

import scala.scalajs.js
import scala.scalajs.js.|

@js.native
trait StripeCard extends js.Object {
  def createToken(info: StripeCardInfo, responseHandler: js.Function2[Int, CardTokenInfo, Unit]): Unit = js.native
  def validateCardNumber(number: String): Boolean = js.native
  def validateExpiry(month: Int | String, year: Int | String): Boolean = js.native
  def validateCVC(cvc: String): Boolean = js.native
  def cardType(number: String): String = js.native
}
