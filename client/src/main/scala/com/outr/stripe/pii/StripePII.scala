package com.outr.stripe.pii

import com.outr.stripe.TokenError

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

@js.native
trait StripePII extends js.Object {
  def createToken(info: StripePIIInfo, responseHandler: js.Function2[Int, PIITokenInfo, Unit]): Unit = js.native
}

@ScalaJSDefined
trait StripePIIInfo extends js.Object {
  var personal_id_number: js.UndefOr[String] = js.undefined
}

@js.native
trait PIITokenInfo extends js.Object {
  def id: String = js.native
  def created: Long = js.native
  def livemode: Boolean = js.native
  def `type`: String = js.native
  def `object`: String = js.native
  def used: Boolean = js.native
  def error: TokenError = js.native
}