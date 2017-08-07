package net.vpc.scholar.hadrumaths

/**
  * Created by vpc on 7/16/17.
  */
class DoubleColonTuple3(val _1: Double, val _2: Double, val _3: Double) extends ToDoubleArrayAware{
  override def toDoubleArray(): Array[Double] = Maths.dsteps(_1,_3,_2)

  override def toString = _1+"::"+_2+"::"+_3;
}
