package net.vpc.scholar.hadrumaths

/**
  * Created by vpc on 7/16/17.
  */
class DoubleColonTuple2(val _1: Double, val _2: Double)  extends ToDoubleArrayAware{
 def :: (v : Double) : DoubleColonTuple3 = new DoubleColonTuple3(v,_2,_1)
 val left=_2
 val right=_1

 override def toDoubleArray(): Array[Double] = Maths.dsteps(left,right)

 override def toString = _1+"::"+_2;

}
