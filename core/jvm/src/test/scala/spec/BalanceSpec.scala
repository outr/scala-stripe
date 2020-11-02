package spec

import com.outr.stripe.QueryConfig
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec

class BalanceSpec extends AsyncWordSpec with Matchers {
  "Balance" should {
    "list a test balance" in {
      TestStripe().balance().map {
        case Left(failure) => fail(s"Receive error response: ${failure.text} (${failure.code})")
        case Right(balance) => {
          balance.`object` should be("balance")
          balance.available.length should be(1)
          balance.available.head.currency should be("usd")
          balance.available.head.amount should not be null
          balance.available.head.sourceTypes.card should not be null
          balance.livemode should be(false)
          balance.pending.length should be(1)
          balance.pending.last.currency should be("usd")
        }
      }
    }
    "list most recent balance transaction history" in {
      TestStripe().balance.list(config = QueryConfig(limit = 1)).map {
        case Left(failure) => fail(s"Receive error response: ${failure.text} (${failure.code})")
        case Right(list) => {
          list.`object` should be("list")
          list.url should be("/v1/balance_transactions")
          list.hasMore should be(true)
          list.data.length should be(1)
        }
      }
    }
  }
}
