package spec

import com.outr.stripe.Money
import com.outr.stripe.charge.Card
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import java.time.Year

class PurchaseWorkflowSpec extends AsyncWordSpec with Matchers {
  "Purchase Workflow" should {
    var customerId: Option[String] = None
    var creditCardTokenId: Option[String] = None
    var creditCardId: Option[String] = None

    "create a test customer" in {
      TestStripe.customers.create(email = Some("test@test.com"), description = Some("test user")).map {
        case Left(failure) => fail(s"Receive error response: ${failure.text} (${failure.code})")
        case Right(customer) => {
          customer.email should be(Some("test@test.com"))
          customer.livemode should be(false)
          customerId = Option(customer.id)
          customerId shouldNot be(None)
        }
      }
    }
    "lookup the test customer" in {
      TestStripe.customers.byId(customerId.get).map {
        case Left(failure) => fail(s"Receive error response: ${failure.text} (${failure.code})")
        case Right(customer) => {
          customer.id should be(customerId.get)
          customer.email should be(Some("test@test.com"))
        }
      }
    }
    "fail to create a credit card token" in {
      val card = Card.create("4242111111111111", 1, Year.now.getValue - 1)
      TestStripe.tokens.create(card = Some(card)).map {
        case Left(failure) => {
          failure.code should be(402)
          failure.text should be("") //("Payment Required")
          failure.error.`type` should be("card_error")
          failure.error.code should be(Some("incorrect_number"))
          failure.error.message should be("Your card number is incorrect.")
          failure.error.param should be(Some("number"))
        }
        case Right(_) => fail("Was supposed to fail, but did not!")
      }
    }
    "create a credit card token" in {
      val card = Card.create("4242424242424242", 12, Year.now.getValue + 1)
      TestStripe.tokens.create(card = Some(card)).map {
        case Left(failure) => fail(s"Receive error response: ${failure.text} (${failure.code})")
        case Right(token) => {
          token.`type` should be("card")
          token.card shouldNot be(None)
          creditCardTokenId = Option(token.id)
          creditCardTokenId shouldNot be(None)
        }
      }
    }
    "store the credit card with the test customer" in {
      TestStripe.customers.sources.cards.create(customerId.get, creditCardTokenId).map {
        case Left(failure) => fail(s"Receive error response: ${failure.text} (${failure.code})")
        case Right(card) => {
          card.number should be(None)
          card.expMonth should be(12)
          card.expYear should be(Year.now.getValue + 1)
          creditCardId = Option(card.id)
          creditCardId shouldNot be(None)
        }
      }
    }
    "make a purchase with the test customer USD no pennies" in {
      TestStripe.charges.create(Money(5), "USD", customer = customerId).map {
        case Left(failure) => fail(s"Receive error response: ${failure.text} - ${failure.error} (${failure.code})")
        case Right(charge) => {
          charge.amount.pennies should be(Money(500L).pennies)
          charge.captured should be(true)
          charge.failureCode should be(None)
          charge.failureMessage should be(None)
          charge.source.id should be(creditCardId.get)
          charge.status should be("succeeded")
        }
      }
    }
    "make a purchase with the test customer USD pennies" in {
      TestStripe.charges.create(Money(5.00), "USD", customer = customerId).map {
        case Left(failure) => fail(s"Receive error response: ${failure.text} - ${failure.error} (${failure.code})")
        case Right(charge) => {
          charge.amount.pennies  should be(Money(500L).pennies )
          charge.captured should be(true)
          charge.failureCode should be(None)
          charge.failureMessage should be(None)
          charge.source.id should be(creditCardId.get)
          charge.status should be("succeeded")
        }
      }
    }
    "make a purchase with the test customer USD pennies String" in {
      TestStripe.charges.create(Money("5.00"), "USD", customer = customerId).map {
        case Left(failure) => fail(s"Receive error response: ${failure.text} - ${failure.error} (${failure.code})")
        case Right(charge) => {
          charge.amount.pennies should be(Money(500L).pennies )
          charge.captured should be(true)
          charge.failureCode should be(None)
          charge.failureMessage should be(None)
          charge.source.id should be(creditCardId.get)
          charge.status should be("succeeded")
        }
      }
    }
    "make a purchase with the test customer JPY" in {
      TestStripe.charges.create(Money("5000"), "JPY", customer = customerId).map {
        case Left(failure) => fail(s"Receive error response: ${failure.text} - ${failure.error} (${failure.code})")
        case Right(charge) => {
          charge.amount.pennies  should be(Money(5000L).pennies )
          charge.captured should be(true)
          charge.failureCode should be(None)
          charge.failureMessage should be(None)
          charge.source.id should be(creditCardId.get)
          charge.status should be("succeeded")
        }
      }
    }
    "make a purchase with the test customer JPY 500" in {
      TestStripe.charges.create(Money("500"), "JPY", customer = customerId).map {
        case Left(failure) => fail(s"Receive error response: ${failure.text} - ${failure.error} (${failure.code})")
        case Right(charge) => {
          charge.amount.pennies  should be(Money(500L).pennies )
          charge.captured should be(true)
          charge.failureCode should be(None)
          charge.failureMessage should be(None)
          charge.source.id should be(creditCardId.get)
          charge.status should be("succeeded")
        }
      }
    }
    "make a purchase with the test customer JPY 5" in {
      TestStripe.charges.create(Money("5"), "JPY", customer = customerId).map {
        case Left(failure) =>
          failure.error.code should be("amount_too_small")
        case Right(charge) => fail("Was supposed to fail, but did not!")
      }
    }
    "delete a credit card" in {
      TestStripe.customers.sources.cards.delete(customerId.get, creditCardId.get).map {
        case Left(failure) => fail(s"Receive error response: ${failure.text} (${failure.code})")
        case Right(deleted) => deleted.deleted should be(true)
      }
    }
    "delete the test customer" in {
      TestStripe.customers.delete(customerId.get).map {
        case Left(failure) => fail(s"Receive error response: ${failure.text} (${failure.code})")
        case Right(deleted) => deleted.deleted should be(true)
      }
    }
  }
}
