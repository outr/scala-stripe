package com.outr.stripe

import com.outr.scribe.Logging
import com.outr.stripe.support._

class Stripe(val apiKey: String) extends Restful with Logging {
  private val baseURL = "https://api.stripe.com/v1"
  override protected def url(endPoint: String): String = s"$baseURL/$endPoint"

  lazy val balance: BalanceSupport = new BalanceSupport(this)
  lazy val charges: ChargesSupport = new ChargesSupport(this)
  lazy val customers: CustomersSupport = new CustomersSupport(this)
  lazy val disputes: DisputesSupport = new DisputesSupport(this)
  lazy val events: EventsSupport = new EventsSupport(this)
  lazy val refunds: RefundsSupport = new RefundsSupport(this)
  lazy val tokens: TokensSupport = new TokensSupport(this)
  lazy val transfers: TransfersSupport = new TransfersSupport(this)
  lazy val accounts: AccountsSupport = new AccountsSupport(this)
  lazy val applicationFees: ApplicationFeesSupport = new ApplicationFeesSupport(this)
  lazy val countrySpecs: CountrySpecsSupport = new CountrySpecsSupport(this)
  lazy val coupons: CouponsSupport = new CouponsSupport(this)
  lazy val discounts: DiscountSupport = new DiscountSupport(this)
  lazy val invoices: InvoicesSupport = new InvoicesSupport(this)
  lazy val invoiceItems: InvoiceItemsSupport = new InvoiceItemsSupport(this)
//  lazy val plans: PlansSupport = new PlansSupport(this)
//  lazy val subscriptions: SubscriptionsSupport = new SubscriptionsSupport(this)
}

object Stripe {
  val Version = "2016-07-06"
}