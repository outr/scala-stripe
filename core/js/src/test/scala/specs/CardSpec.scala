package specs

import com.outr.stripe.Stripe
import org.scalajs.dom._
import org.scalatest.Assertion
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec

import scala.concurrent.{Future, Promise}

//import scala.concurrent.ExecutionContext.Implicits.global

class CardSpec extends AsyncWordSpec with Matchers {
  implicit override def executionContext = scala.concurrent.ExecutionContext.Implicits.global

  "Stripe Card API" should {
    "load Stripe.js" in {
      println("Loading script...")
      loadScript()
    }
    "set the publishable key" in {
      Stripe.setPublishableKey("pk_test_6pRNASCoBOKtIshFeQd4XMUh")
      Future(true should be(true))
    }
    "validate a valid card number" in Future {
      val result = Stripe.card.validateCardNumber("4242424242424242")
      result should be(true)
    }
    "fail validation for an invalid card number" in Future {
      val result = Stripe.card.validateCardNumber("4242111111111111")
      result should be(false)
    }
//    "create a valid card token" in {
//      val promise = Promise[Assertion]
//      Stripe.card.createToken(new StripeCardInfo {
//        number = "4242424242424242"
//        exp_month = 12
//        exp_year = 2017
//      }, (status: Int, info: CardTokenInfo) => {
//        promise.success(status should be(200))
//      })
//      promise.future
//    }
  }

  def loadScript(): Future[Assertion] = {
    val head = document.getElementsByTagName("head")(0)
    val script = document.createElement("script").asInstanceOf[html.Script]
    script.`type` = "text/javascript"
    script.src = "https://js.stripe.com/v2/"
    val promise = Promise[Assertion]()
    val callback = (evt: Event) => {
      println("Callback!")
      promise.success(true should be(true))
    }
    script.onreadystatechange = callback
    script.onload = callback
    head.appendChild(script)

    println("Configured...just waiting...")
    promise.future
  }
}