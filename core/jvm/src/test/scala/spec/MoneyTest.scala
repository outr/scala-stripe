package spec

import com.outr.stripe.{Implicits, Money}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
class MoneyTest  extends AsyncWordSpec with Matchers with Implicits {

      "moneys should be equal" in {
        Money(5.0) should be(Money("5.0"))
        Money(5,0) should be(Money("5"))
        Money(5,1) should be(Money("5"))
        Money(5.toDouble) should be(Money("5"))
        Money(5) should be(Money("5"))
        Money(5,3) should be(Money("5"))
        Money(0.05) should be(Money("0.05"))
        Money(5.0) should be(Money(500L))
        Money(5) should be(Money(500L))
        Money(0.05) should be(Money(5L))

        Money(5.0).pennies should be(Money("5.00").pennies)
        Money(5,0).pennies should be(Money("5").pennies)
        Money(5,1).pennies should be(Money("5.0").pennies)
        Money(5.toDouble).pennies should be(Money("5.00").pennies)
        Money(5).pennies should be(Money("5.00").pennies)
        Money(5,3).pennies should be(Money("5.000").pennies)
        Money(0.05).pennies should be(Money("0.05").pennies)
        Money(0.05).pennies should be(Money(5L).pennies)
        Money(5).pennies should be(Money(500L).pennies)
        Money(5.0).pennies should be(Money(500L).pennies)

        Money(5.0).toString should be(Money("5.00").toString)
        Money(5,0).toString should be(Money("5").toString)
        Money(5,1).toString should be(Money("5.0").toString)
        Money(5.toDouble).toString should be(Money("5.00").toString)
        Money(5).toString should be(Money("5.00").toString)
        Money(5,3).toString should be(Money("5.000").toString)
        Money(0.05).toString should be(Money("0.05").toString)
        Money(5.0).toString should be(Money(500L).toString)
        Money(5).toString should be(Money(500L).toString)
        Money(0.05).toString should be(Money(5L).toString)


        Money("5.0") should be(Money(500L))
        Money("5.00").pennies should be(Money(500L).pennies)
        Money("5.0").value should be(Money(500L).value)
        Money("5.00").toString should be(Money(500L).toString)

        Money("5").toString should be("5")
        Money("5.0").toString should be("50")
        Money("5.00").toString should be("500")
        Money("5.000").toString should be("5000")

        write("", Money("5.00"))(moneyEncoder) should be(Map(""->"500"))
        write("", Money("5"))(moneyEncoder) should be(Map(""->"5"))
        write("", Money(5.00))(moneyEncoder) should be(Map(""->"500"))
        write("", Money(5))(moneyEncoder) should be(Map(""->"500"))
        write("", Money(BigDecimal(5)setScale(2,BigDecimal.RoundingMode.HALF_EVEN)))(moneyEncoder) should be(Map(""->"500"))
        write("", Money(BigDecimal("5")setScale(2,BigDecimal.RoundingMode.HALF_EVEN)))(moneyEncoder) should be(Map(""->"500"))
        write("", Money(BigDecimal("5.0")setScale(2,BigDecimal.RoundingMode.HALF_EVEN)))(moneyEncoder) should be(Map(""->"500"))
        write("", Money(BigDecimal("5.00")setScale(2,BigDecimal.RoundingMode.HALF_EVEN)))(moneyEncoder) should be(Map(""->"500"))
      }
}
