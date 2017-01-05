package com.outr.stripe.connect

case class TransferSchedule(delayDays: Int, interval: String, monthlyAnchor: Option[Int], weeklyAnchor: Option[String])
