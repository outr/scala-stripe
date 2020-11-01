package com.outr.stripe

import scala.math.BigDecimal.RoundingMode

class Money private(val value: BigDecimal) {
  override def equals(obj: scala.Any): Boolean = obj match {
    case m: Money => m.value == value
    case b: BigDecimal => b == value
    case _ => false
  }

  def pennies: Long = (value * Math.pow(10,value.scale)).toLongExact

  override def toString: String =
    value.scale match {
      case 0 => f"$$$value%1f"
      case 1 => f"$$$value%1.1f"
      case 2 => f"$$$value%1.2f"
      case 3 => f"$$$value%1.3f"
      case 4 => f"$$$value%1.4f"
      case _ => f"$$$value%1.2f"
    }
}

object Money {
  def apply(d: BigDecimal): Money = new Money(d)
  def apply(d: Double,fractionDigits:Int=2): Money = apply(BigDecimal(d).setScale(fractionDigits,RoundingMode.HALF_EVEN))
  def apply(pennies: Long): Money = apply(BigDecimal(pennies) / 100.0)
  def apply(s: String): Money = apply(BigDecimal(s))
}