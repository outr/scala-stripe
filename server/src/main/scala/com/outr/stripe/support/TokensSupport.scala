package com.outr.stripe.support

import com.outr.stripe.charge.{BankAccount, Card, PII}
import com.outr.stripe.token.Token
import com.outr.stripe.{Implicits, Pickler, QueryConfig, Stripe}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TokensSupport(stripe: Stripe) extends Implicits {
  def create(card: Option[Card] = None,
             bankAccount: Option[BankAccount] = None,
             pii: Option[PII] = None,
             customerId: Option[String] = None): Future[Token] = {
    val data = List(
      card.map("card" -> Pickler.write(_)),
      bankAccount.map("bank_account" -> Pickler.write(_)),
      pii.map("pii" -> Pickler.write(_)),
      customerId.map("customer" -> _)
    ).flatten
    stripe.post("tokens", QueryConfig.default, data: _*).map { response =>
      Pickler.read[Token](response.body)
    }
  }

  def byId(tokenId: String): Future[Token] = {
    stripe.get(s"tokens/$tokenId", QueryConfig.default).map { response =>
      Pickler.read[Token](response.body)
    }
  }
}
