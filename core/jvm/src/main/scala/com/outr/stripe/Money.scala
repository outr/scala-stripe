package com.outr.stripe

import scala.math.BigDecimal.RoundingMode

class Money private(val value: BigDecimal) {
  override def equals(obj: scala.Any): Boolean = obj match {
    case m: Money => m.value == value
    case b: BigDecimal => b == value
    case _ => false
  }

  def pennies: Long = (value * Math.pow(10,value.scale)).toLongExact

  override def toString: String = pennies.toString

//  amount
//  positive integer or zero
//  Amount intended to be collected by this payment.
//  A positive integer representing how much to charge in the smallest currency unit
//  (e.g., 100 cents to charge $1.00 or 100 to charge ¥100, a zero-decimal currency).
//  The minimum amount is $0.50 US or equivalent in charge currency.
//  The amount value supports up to eight digits (e.g., a value of 99999999 for a USD charge of $999,999.99).
}

object Money {
  def apply(d: BigDecimal): Money = new Money(d)

  def apply(number:AnyVal,fractionDigits:Int=2): Money =
  number match {
    case i:Int=>apply(BigDecimal(i.toDouble).setScale(fractionDigits,RoundingMode.HALF_EVEN))
    case d:Double=>apply(BigDecimal(d).setScale(fractionDigits,RoundingMode.HALF_EVEN))
    case pennies:Long =>apply((BigDecimal(pennies) / Math.pow(10,fractionDigits)).setScale(fractionDigits,RoundingMode.HALF_EVEN))
    case _ => throw new NumberFormatException(s"The only permitted types are Int, Double or Long(as fractions).")
  }
  def apply(s: String): Money = apply(BigDecimal(s))
}