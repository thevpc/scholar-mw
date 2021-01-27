package net.thevpc.scholar.hadrumaths

/**
  * Created by vpc on 7/16/17.
  */
class ToDoubleArrayAwareConstant(value:Array[Double]) extends ToDoubleArrayAware{
  override def toDoubleArray(): Array[Double] = value
}
