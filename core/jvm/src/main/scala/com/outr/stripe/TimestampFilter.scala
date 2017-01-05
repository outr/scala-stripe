package com.outr.stripe

case class TimestampFilter(gt: Option[Long] = None,
                           gte: Option[Long] = None,
                           lt: Option[Long] = None,
                           lte: Option[Long] = None)
