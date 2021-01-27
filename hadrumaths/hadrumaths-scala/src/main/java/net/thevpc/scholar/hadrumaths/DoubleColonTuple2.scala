package net.thevpc.scholar.hadrumaths

/**
  * Created by vpc on 7/16/17.
  */
class DoubleColonTuple2(val _1: Double, val _2: Double) extends ToDoubleArrayAware with scala.Iterable[Double] {
  def ::(v: Double): DoubleColonTuple3 = new DoubleColonTuple3(v, _2, _1)

  val left = _2
  val right = _1

  override def toDoubleArray(): Array[Double] = Maths.dsteps(left, right)

  override def toString = _1 + "::" + _2;

  override def iterator: Iterator[Double] = toDoubleArray().iterator

  def length: Double = Maths.dstepsLength(left, right, 1)

  def apply(i: Int): Double = Maths.dstepsElement(left, right, 1, i)

  def foreachIndex[U](f: (Int, Double) => U) {
    val doubles = toDoubleArray()
    var i = 0;
    val max = doubles.length
    while (i<max) {
      f(i, doubles(i));
      i = i + 1
    }
  }
}
