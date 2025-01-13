package net.thevpc.scholar.mentoring.ch02_hadrumaths

//these are Main Mandatory Imports
import net.thevpc.scholar.hadrumaths.MathScala._

object T09_Conditional extends App{
  var n=param("n")

  //one param function
  var f=(n>2)*X*n+2
  Plot.plot(f(n->1))
  Plot.plot(f(n->2))
  Plot.plot(f(n->3))
  Plot.plot(f(n->4))
}
