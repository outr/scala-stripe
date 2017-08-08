package com.outr.stripe

import com.outr.stripe.bank.StripeBank
import com.outr.stripe.card.StripeCard
import com.outr.stripe.pii.StripePII

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal
object Stripe extends js.Object {
  def setPublishableKey(key: String): Unit = js.native
  def card: StripeCard = js.native
  def bankAccount: StripeBank = js.native
  def piiData: StripePII = js.native
}