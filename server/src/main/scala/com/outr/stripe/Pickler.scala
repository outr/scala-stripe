package com.outr.stripe

import upickle.Js
import upickle.Js.{Arr, Value}

import scala.math.BigDecimal.RoundingMode

object Pickler extends upickle.AttributeTagged {
  def camelToSnake(s: String): String = {
    val res = s.split("(?=[A-Z])", -1).map(_.toLowerCase).mkString("_")
    res
  }

  override protected[this] def mapToArray(o: Js.Obj, names: Array[String], defaults: Array[Js.Value]): Arr = {
    val accumulated = new Array[Js.Value](names.length)
    val map = o.value.toMap
    var i = 0
    val l = names.length
    while(i < l) {
      if (map.contains(names(i))) accumulated(i) = map(names(i))
      else if (defaults(i) != null) accumulated(i) = defaults(i)
      else accumulated(i) = Js.Null
      i += 1
    }
    Js.Arr(accumulated: _*)
  }

  override def CaseR[T: Reader, V](f: T => V,
                                        names: Array[String],
                                        defaults: Array[Js.Value]): Reader[V] = {
    super.CaseR[T, V](f, names.map(camelToSnake), defaults)
  }

  override def CaseW[T: Writer, V](f: V => Option[T],
                                        names: Array[String],
                                        defaults: Array[Js.Value]): Writer[V] = {
    super.CaseW[T, V](f, names.map(camelToSnake), defaults)
  }

  override implicit val LongRW: Writer[Long] with Reader[Long] = new Writer[Long] with Reader[Long] {
    override def write0: (Long) => Value = l => Js.Num(l.toDouble)

    override def read0: PartialFunction[Value, Long] = {
      case Js.Num(d) => d.toLong
      case Js.Str(s) => s.toLong
    }
  }

  override implicit val BigIntRW: Writer[BigInt] with Reader[BigInt] = new Writer[BigInt] with Reader[BigInt] {
    override def write0: (BigInt) => Value = d => Js.Num(d.toDouble)

    override def read0: PartialFunction[Value, BigInt] = {
      case Js.Num(d) => BigInt(d.toLong)
      case Js.Str(s) => BigInt(s)
    }
  }

  // Converts from pennies to BigDecimal
  override implicit val BigDecimalRW: Writer[BigDecimal] with Reader[BigDecimal] = new Writer[BigDecimal] with Reader[BigDecimal] {
    override def write0: (BigDecimal) => Value = d => Js.Str((d * 100.0).setScale(2, RoundingMode.HALF_EVEN).toLongExact.toString)

    override def read0: PartialFunction[Value, BigDecimal] = {
      case Js.Num(d) => BigDecimal(d) / 100.0
      case Js.Str(s) => BigDecimal(s) / 100.0
    }
  }

  override implicit def OptionW[T: Writer]: Writer[Option[T]] = Writer {
    case None    => Js.Null
    case Some(s) => implicitly[Writer[T]].write(s)
  }

  override implicit def OptionR[T: Reader]: Reader[Option[T]] = Reader {
    case Js.Null     => None
    case v: Js.Value => Some(implicitly[Reader[T]].read.apply(v))
  }
}
