package net.thevpc.scholar.mentoring.ch02_hadrumaths

import net.thevpc.scholar.hadrumaths.MathScala._
object Mehdi extends App {
  var n=param("n")
  var fn=2*(X-n*0.5) * II(0.0->0.5)+
    (-2*(X-n*0.5) + 2) * II(0.5->1.0)
  Plot.plot(fn.inflate(n.in(0,5)))
}
