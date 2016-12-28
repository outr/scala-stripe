package com.outr.stripe

import io.circe.Decoder.Result
import io.circe._
import io.circe.parser._

object Pickler {
  private val entryRegex = """"(.+)": (.+)""".r
  private val snakeRegex = """_([a-z])""".r

  implicit val moneyDecoder = new Decoder[Money] {
    override def apply(c: HCursor): Result[Money] = {
      Decoder.decodeLong(c).map(l => Money(l))
    }
  }

  def read[T](jsonString: String)(implicit decoder: Decoder[T]): T = {
    // Snake to Camel
    val json = entryRegex.replaceAllIn(jsonString, (regexMatch) => {
      val key = snakeRegex.replaceAllIn(regexMatch.group(1), (snakeMatch) => {
        snakeMatch.group(1).toUpperCase
      })
      s""""$key": ${regexMatch.group(2)}"""
    })
    // Use Circe to decode the JSON into a case class
    decode[T](json).getOrElse(throw new PicklerException(s"Unable to decode $jsonString"))
  }
}

class PicklerException(message: String) extends RuntimeException(message)