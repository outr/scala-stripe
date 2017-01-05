package com.outr.stripe

import com.outr.scribe._
import com.outr.stripe.card.{CardTokenInfo, StripeCardInfo}

import scala.scalajs.js.annotation.JSExportTopLevel

object Test {
  @JSExportTopLevel("testStripe")
  def main(): Unit = {
    Stripe.setPublishableKey("pk_test_6pRNASCoBOKtIshFeQd4XMUh")
    val validCardResult = Stripe.card.validateCardNumber("4242424242424242")
    val invalidCardResult = Stripe.card.validateCardNumber("4242-1111-1111-1111")
    logger.info(s"Valid? $validCardResult, Invalid? $invalidCardResult")
    Stripe.card.createToken(new StripeCardInfo {
      number = "4242424242424242"
      exp_month = 12
      exp_year = 2017
    }, (status: Int, info: CardTokenInfo) => {
      logger.info(s"Validation Result! $status, Info: $info")
    })
  }
}
