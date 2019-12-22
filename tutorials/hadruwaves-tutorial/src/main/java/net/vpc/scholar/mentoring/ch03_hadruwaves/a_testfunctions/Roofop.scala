package net.vpc.scholar.mentoring.ch03_hadruwaves.a_testfunctions

import net.vpc.scholar.hadrumaths.Expr
import net.vpc.scholar.hadrumaths.MathScala._

/**
 * Created by vpc on 6/3/17.
 */
object Roofop extends App {
  val d = domain(0.0 -> 10.0)
  val P = 10
  val xs = (d / P).xvalues
  println(java.util.Arrays.toString(xs))

  val w = 10.0 / P
  //  var tt=isteps(1,8,2) // 1,3,5,7
  //  println(java.util.Arrays.toString(tt))
  //  if (true) System.exit(0);
  //  var tt2=itimes(1,8,4) // 1,3.33,5.66,8
  //  var tt=dsteps
  //  var tt=dtime
  //isteps(min,ila,pas) // isteps(1,3)  == 1,2,3
  // isteps(1,6,2)  == 1,3,5
  //itimes(min,ila,times)   //itimes(1,10,3) == 1,5,10
  //dsteps(min,ila,pas)     //dtimes(1,10,4) == 1.0,4.0,7.0,10.0

  val g1 = isteps(1, 2 * xs.length - 1).map(
    i => if (i % 2 == 0) rooftop(xs(i / 2) - (w / 2), xs(i / 2) + (w / 2)) else rooftop(xs(i / 2), xs(i / 2) + w)
  )

  //  Plot.plot(g1 :_ *)  === Plot.plot(g1(0), g1(1), ... g1(10))
  Plot.plot(g1); //=== Plot.plot(g1)

  def rooftop(a: Double, b: Double): Expr = {
    val c = (a + b) / 2
    (X - a) / (c - a) * domain(a -> c) + (b - X) / (b - c) * domain(c -> b)
  }

}
