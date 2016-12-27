package com.outr.stripe

import scala.scalajs.js

@js.native
trait TokenError extends js.Object {
  def `type`: String = js.native
  def code: String = js.native
  def message: String = js.native
  def param: String = js.native
}
