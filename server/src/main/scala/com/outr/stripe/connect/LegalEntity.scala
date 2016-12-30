package com.outr.stripe.connect

import com.outr.stripe.charge.Address

case class LegalEntity(address: Option[Address],
                       addressKana: Option[AddressKana],
                       addressKanji: Option[AddressKanji],
                       businessName: Option[String],
                       businessNameKana: Option[String],
                       businessNameKanji: Option[String],
                       businessTaxIdProvided: Option[Boolean],
                       businessVatIdProvided: Option[Boolean],
                       dob: Date,
                       firstName: Option[String],
                       firstNameKana: Option[String],
                       firstNameKanji: Option[String],
                       gender: Option[String],
                       lastName: Option[String],
                       lastNameKana: Option[String],
                       lastNameKanji: Option[String],
                       maidenName: Option[String],
                       personalAddress: Option[Address],
                       personalAddressKana: Option[AddressKana],
                       personalAddressKanji: Option[AddressKanji],
                       personalIdNumberProvided: Option[Boolean],
                       phoneNumber: Option[String],
                       ssnLast4Provided: Option[Boolean],
                       `type`: String,
                       verification: Verification)
