package com.outr.stripe.support

import com.outr.stripe.charge.{BankAccount, Card, PII}
import com.outr.stripe.token.Token
import com.outr.stripe.{Implicits, QueryConfig, ResponseError, Stripe}

import scala.concurrent.Future

class TokensSupport(stripe: Stripe) extends Implicits {
  def create(card: Option[Card] = None,
             bankAccount: Option[BankAccount] = None,
             pii: Option[PII] = None,
             customerId: Option[String] = None): Future[Either[ResponseError, Token]] = {
    val data = List(
      write("card", card),
      write("bank_account", bankAccount),
      write("pii", pii),
      write("customer", customerId)
    ).flatten
    stripe.post[Token]("tokens", QueryConfig.default, data: _*)
  }

  def byId(tokenId: String): Future[Either[ResponseError, Token]] = {
    stripe.get[Token](s"tokens/$tokenId", QueryConfig.default)
  }
}
