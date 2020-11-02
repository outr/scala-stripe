package spec

import java.nio.file.Paths

import com.outr.stripe.Stripe
import profig.Profig

object TestStripe {
  private lazy val stripe: Stripe = {
    Profig.initConfigurationBlocking(startPath = Paths.get("../.."))
    val p = Profig("stripe.apiKey")
    val apiKey = if (p.exists()) {
      p.as[String]
    } else {
      throw new RuntimeException("Configuration not defined for Stripe API key. Define an a config.json or environment variable for stripe.apiKey")
    }
    new Stripe(apiKey)
  }

  def apply(): Stripe = stripe
}