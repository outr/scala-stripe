# scala-stripe

[![Build Status](https://travis-ci.org/outr/scala-stripe.svg?branch=master)](https://travis-ci.org/outr/scala-stripe)
[![Stories in Ready](https://badge.waffle.io/outr/scala-stripe.png?label=ready&title=Ready)](https://waffle.io/outr/scala-stripe)
[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/outr/scala-stripe)
[![Maven Central](https://img.shields.io/maven-central/v/com.outr/scala-stripe_2.12.svg)](https://maven-badges.herokuapp.com/maven-central/com.outr/scala-stripe_2.12)
[![Latest version](https://index.scala-lang.org/com.outr/scala-stripe/scala-stripe/latest.svg)](https://index.scala-lang.org/com.outr/scala-stripe/scala-stripe)

Provides both client (Stripe.js using Scala.js) and server (NIO Scala) functionality for dealing with Stripe.

For more information on the Stripe API see https://stripe.com/docs/api

For more information on Stripe.js see https://stripe.com/docs/stripe.js

## SBT Configuration ##

scala-stripe is published to Sonatype OSS and Maven Central and supports JVM and Scala.js with 2.11 and 2.12:

```
libraryDependencies += "com.outr" %% "scala-stripe" % "1.0.0"   // Scala
libraryDependencies += "com.outr" %%% "scala-stripe" % "1.0.0"  // Scala.js / Cross-Build
```

## Dependencies

It's important to know how much your biting off when you are adding another dependency to your project. As such, we've
endeavored to keep the dependencies to a minimum:

* Scribe for logging (https://github.com/outr/scribe)
* Gigahorse for non-blocking IO (https://github.com/eed3si9n/gigahorse)
* Circe for JSON pickling (https://github.com/circe/circe)

## Getting Started

Though the JVM supports creating credit card tokens, the ideal path is to avoid the server every having any such knowledge.
To that end we will use Scala.js to first create the card token, and then the server (JVM) will use that token to make
a purchase.

### In the Browser

#### Setting up Stripe.js

We must first set our publishable key. Make sure you are using the public key (starts with "pk_"):

```scala
Stripe.setPublishableKey(stripePublicKey)
```

#### Validating the credit card

```scala
val validationResult: Boolean = Stripe.card.validateCardNumber(creditCardNumber)
```

#### Creating a Stripe Token for the credit card

The following example is a basic use-case, and assumes you'll provide implementations of sending the token to the server
as well as showing token failure messages as that information is specific to your implementation.

```scala
Stripe.card.createToken(new StripeCardInfo {
  number = creditCardNumber
  exp_month = cardExpirationMonth
  exp_year = cardExpirationYear
}, (status: Int, info: CardTokenInfo) => {
  if (status == 200) {
    sendCardTokenInfoToServer(info.id, ...other useful information the server might need...)
  } else {
    showCardTokenFailure(info.error)
  }
})
```

### On the Server

#### Creating a Stripe instance

As discussed in the Browser section, we have two keys: public ("pk_") and secret ("sk_"). For the server to work with
Stripe we should use our private key to create an instance of `Stripe`:

```scala
val stripe = new Stripe(stripePrivateKey)
```

#### Charging the Credit Card

Now that we have received the card token on the server, we can use that to make a purchase with Stripe:

```scala
stripe.charges.create(Money(5.0), "USD", source = creditCardTokenId, customer = customerId).map {
  case Left(failure) => // Handle error from Stripe server
  case Right(charge) => // Success! Handle the Charge instance returned
}
```

## Features for 2.0.0 (In-Progress)

* [ ] Stripe API Relay
    * [ ] Orders
    * [ ] Order Items
    * [ ] Returns
    * [ ] Products
    * [ ] SKUs
* [ ] Stripe API Radar
    * [ ] Reviews
* [ ] Stripe API Core Resources
    * [ ] File Uploads
* [ ] Stripe API Payments
    * [ ] Alipay Accounts
    * [ ] Sources
* [ ] Stripe.js
    * [ ] Apple Pay

## Features for 1.0.0 (Released 2016.01.05)

* [X] Stripe.js functionality in Scala.js (Excludes Apple Pay)
    * [X] Card
    * [X] Bank Account
    * [X] Personally Identifiable Information (PII)
* [X] Stripe API Core Resources
    * [X] Balance
    * [X] Charges
    * [X] Customers
    * [X] Disputes
    * [X] Events
    * [X] Refunds
    * [X] Tokens
    * [X] Transfers
    * [X] Transfer Reversals
* [X] Stripe API Connect
    * [X] Account
    * [X] Application Fees
    * [X] Application Fee Refunds
    * [X] Country Specs
    * [X] External Accounts
* [X] Stripe API Payments
    * [X] Bank Accounts
    * [X] Cards
* [X] Stripe API Subscriptions
    * [X] Coupons
    * [X] Discounts
    * [X] Invoices
    * [X] Invoice Items
    * [X] Plans
    * [X] Subscriptions