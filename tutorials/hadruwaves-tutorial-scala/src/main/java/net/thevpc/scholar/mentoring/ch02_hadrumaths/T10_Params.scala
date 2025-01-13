package net.thevpc.scholar.mentoring.ch02_hadrumaths

//these are Main Mandatory Imports

import net.thevpc.scholar.hadrumaths._
import net.thevpc.scholar.hadrumaths.MathScala._

object T10_Params extends App {
  var n = param("n")

  //one param function
  var f = (n + 1) * X

  var g = sin(n * X)
  var f0 = f(n -> 0.0)
  var f1 = f(n -> 1.0)
  Plot.title("f0").plot(f0)
  Plot.title("f1").plot(f1)
  var all = f inflate (n in (5, 20))
  var f5 = all(0)
  all = all :+ g.inflate(n.in(1, 10))
  Plot.title("f*").plot(all)

  var m = param("m")
  var t = param("p")

  var all_f = f.inflate(n.in(1, 5))

  var all_f_but_zero = f.inflate(n.in(1,5))

  //three param function
  var f3 = (n * X + m * Y + t * Z) * II(1.3 -> 2.0)
  var all_f3_but_zero_zeror3 = f3.inflate(m.in(1,3).and(n.in(1,3)).and(t.in(1,3)));
  var all_f3_but_zero_zeror2 = f3.inflate(m.in(1,3).and(n.in(1,3)));
  var all_f3_but_zero_zeror1 = f3.inflate(m.in(1,3));

}
