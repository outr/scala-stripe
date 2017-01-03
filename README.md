# scala-stripe

[![Build Status](https://travis-ci.org/outr/scala-stripe.svg?branch=master)](https://travis-ci.org/outr/scala-stripe)
[![Stories in Ready](https://badge.waffle.io/outr/scala-stripe.png?label=ready&title=Ready)](https://waffle.io/outr/scala-stripe)
[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/outr/scala-stripe)
[![Maven Central](https://img.shields.io/maven-central/v/com.outr/scala-stripe-server_2.12.svg)](https://maven-badges.herokuapp.com/maven-central/com.outr/scala-stripe-server_2.12)
[![Latest version](https://index.scala-lang.org/com.outr/scala-stripe/scala-stripe-server/latest.svg)](https://index.scala-lang.org/com.outr/scala-stripe/scala-stripe-server)

Provides both client (Stripe.js using Scala.js) and server (NIO Scala) functionality for dealing with Stripe.

For more information on the Stripe API see https://stripe.com/docs/api

For more information on Stripe.js see https://stripe.com/docs/stripe.js

## Getting Started

TODO

## Features for 2.0.0 (Future)

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

## Features for 1.0.0 (In-Progress)

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
* [ ] Stripe API Subscriptions
    * [X] Coupons
    * [X] Discounts
    * [ ] Invoices
    * [ ] Invoice Items
    * [ ] Plans
    * [ ] Subscriptions