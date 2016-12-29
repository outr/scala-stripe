package com.outr.stripe.dispute

case class EvidenceDetails(dueBy: Option[Long], hasEvidence: Boolean, pastDue: Boolean, submissionCount: Int)
