package com.outr.stripe.charge

case class EvidenceDetails(dueBy: Option[Long], hasEvidence: Boolean, pastDue: Boolean, submissionCount: Int)
