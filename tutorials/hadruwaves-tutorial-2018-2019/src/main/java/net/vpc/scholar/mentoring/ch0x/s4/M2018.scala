package net.vpc.scholar.mentoring.ch0x.s4

import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadruplot.Plot
import net.vpc.scholar.hadruwaves._
object M2018 extends App{
  var a=100 * MM
  var b=40 * MM
  var f=3 * GHZ
  var L=15 * MM
  var w=5 * MM
  var s=2 * MM
  var h=3 * MM
  var esp=2.2
  var P=6
  var p=param("p")
  private val dom = domain(0.0 -> a, -w / 2 -> w / 2)
  var gp=cos((2.0*p+1.0)/2.0*PI*X/a) * dom
  var g3=gp(p->3)
  var g=seq(gp,p,0,P)
  var expr2=cos(X/a)
  val complexes: Array[Complex] = expr2.computeComplex((dom/5).xvalues())
  Plot.title(cos(X/a).toString).domain(dom).plot(cos(X/a))
  Plot.title("g").domain(dom).plot(g)
  Plot.plot(cos(X)*domain(0.0 -> 2*PI))
}
