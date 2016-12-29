package spec

import com.outr.stripe.{Money, QueryConfig}
import org.scalatest.{AsyncWordSpec, Matchers}

class BalanceSpec extends AsyncWordSpec with Matchers {
  "Balance" should {
    "list a test balance" in {
      TestStripe.balance().map { balance =>
        balance.`object` should be("balance")
        balance.available.length should be(3)
        balance.available.head.currency should be("aud")
        balance.available.head.amount should be(Money(-0.88))
        balance.available.head.sourceTypes.card should be(Money(-0.88))
        balance.livemode should be(false)
        balance.pending.length should be(3)
        balance.pending.last.currency should be("usd")
      }
    }
    "list most recent balance transaction history" in {
      TestStripe.balance.list(config = QueryConfig(limit = 1)).map { list =>
        list.`object` should be("list")
        list.url should be("/v1/balance/history")
        list.hasMore should be(true)
        list.data.length should be(1)
      }
    }
  }
}
