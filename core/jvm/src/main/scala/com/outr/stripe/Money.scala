package com.outr.stripe

import scala.math.BigDecimal.RoundingMode

class Money private(val value: BigDecimal) {
  override def equals(obj: scala.Any): Boolean = obj match {
    case m: Money => m.value == value
    case b: BigDecimal => b == value
    case _ => false
  }

  def pennies: Long = (value * 100.0).toLongExact

  override def toString: String = f"$$$value%1.2f"
}

object Money {
  def apply(d: BigDecimal): Money = new Money(d.setScale(2, RoundingMode.HALF_EVEN))
  def apply(d: Double): Money = apply(BigDecimal(d))
  def apply(pennies: Long): Money = apply(BigDecimal(pennies) / 100.0)
  def apply(s: String): Money = apply(BigDecimal(s))
}