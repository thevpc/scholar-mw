package net.vpc.scholar.hadrumaths

/**
  * Created by vpc on 7/16/17.
  */
class DoubleColonTuple3(val _1: Double, val _2: Double, val _3: Double) extends ToDoubleArrayAware with scala.Iterable[Double] {
  override def toDoubleArray(): Array[Double] = Maths.dsteps(_1, _3, _2)

  override def toString = _1 + "::" + _2 + "::" + _3;

  override def iterator: Iterator[Double] = toDoubleArray().iterator

  def length: Double = Maths.dstepsLength(_1, _3, _2)

  def apply(i: Int): Double = Maths.dstepsElement(_1, _3, _2, i)

}
