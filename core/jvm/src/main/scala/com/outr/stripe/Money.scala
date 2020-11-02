package com.outr.stripe

import java.util.{Currency, Locale}

import scala.math.BigDecimal.RoundingMode

class Money private(val value: BigDecimal) {
  override def equals(obj: scala.Any): Boolean = obj match {
    case m: Money => m.value == value
    case b: BigDecimal => b == value
    case _ => false
  }

  def pennies(currency: Currency): Long = pennies(currency.getDefaultFractionDigits)
  def pennies(fractionDigits: Int): Long = value.underlying().movePointRight(fractionDigits).longValue()

  override def toString: String = f"$$$value%1.2f"
}

object Money {
  var defaultCurrency: Currency = Currency.getInstance(Locale.getDefault)

  def apply(d: BigDecimal, currency: Currency): Money = new Money(d.setScale(currency.getDefaultFractionDigits, RoundingMode.HALF_EVEN))
  def apply(d: Double, currency: Currency): Money = apply(BigDecimal(d), currency)
  def apply(pennies: Long, currency: Currency): Money = apply(BigDecimal(pennies).underlying().movePointLeft(currency.getDefaultFractionDigits), currency)
  def apply(s: String, currency: Currency): Money = apply(BigDecimal(s), currency)

  def apply(d: BigDecimal): Money = apply(d, defaultCurrency)
  def apply(d: Double): Money = apply(d, defaultCurrency)
  def apply(pennies: Long): Money = apply(pennies, defaultCurrency)
  def apply(s: String): Money = apply(s, defaultCurrency)
}