package net.vpc.scholar.hadrumaths.examples

import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadrumaths.MathScala._

/**
  * Created by vpc on 7/17/17.
  */
object PlotExamples extends App{
  val d1 = domain(-5 * PI -> 5 * PI, -5 * PI -> 5 * PI)
  val d2 = domain(0.0 -> 2 * PI, -(2 * PI) -> 2 * PI)
//  var f=sin(2*X)*cos(3*PI/5*(Y-5))*d2
  var f=tan(X)*domain(0.0 -> 2*PI)

  Plot.update("Titi").samples(adaptiveSamples(100,1000)).titles().asReal().plot(f)


}
