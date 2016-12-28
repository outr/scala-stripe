package com.outr.stripe

case class QueryConfig(idempotencyKey: Option[String] = None,
                       limit: Int = 10,
                       startingAfter: Option[String] = None,
                       endingBefore: Option[String] = None)

object QueryConfig {
  val default: QueryConfig = QueryConfig()
}