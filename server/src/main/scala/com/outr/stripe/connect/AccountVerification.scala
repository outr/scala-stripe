package com.outr.stripe.connect

case class AccountVerification(disabledReason: Option[String], dueBy: Option[Long], fieldsNeeded: List[String])
