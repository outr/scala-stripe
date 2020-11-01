import com.outr.stripe.Money

println(BigDecimal("5").scale)
println(Money(BigDecimal("5")).value.scale)
println(Money(BigDecimal(5)).value.scale)
println(Money("5"))

