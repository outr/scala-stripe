package com.outr.stripe

case class StripeList[T](`object`: String, url: String, hasMore: Boolean, data: List[T])
