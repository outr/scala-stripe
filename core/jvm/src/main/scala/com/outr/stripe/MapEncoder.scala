package com.outr.stripe

trait MapEncoder[T] {
  def encode(key: String, value: T): Map[String, String]
}

object MapEncoder {
  def singleValue[T](converter: T => String): MapEncoder[T] = new MapEncoder[T] {
    override def encode(key: String, value: T) = Map(key -> converter(value))
  }
}