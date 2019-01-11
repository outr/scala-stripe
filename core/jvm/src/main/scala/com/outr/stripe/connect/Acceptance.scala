package com.outr.stripe.connect

case class Acceptance(date: Long, ip: String, userAgent: Option[String], iovationBlackbox: Option[String])
