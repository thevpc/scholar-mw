package net.thevpc.scholar.mentoring.extra

import net.thevpc.scholar.hadrumaths.Expr
import net.thevpc.scholar.hadrumaths.MathScala._
import net.thevpc.scholar.hadruwaves.Physics._

object test2 extends App {
  var m = param("n")
  var n = param("m")
  var a = 1.0
  var b = 1.0
  private val f: Expr = sin(n * PI * X / a+m * PI * Y / b) * domain(0.0 -> a, 0.0 -> b)
  Plot.cd("a/10").title("n=1 m=0").plot(f(n->1.0)(m->0.0))
  Plot.cd("a/11").title("n=1 m=1").plot(f(n->1.0)(m->1.0))
  Plot.cd("a/12").title("n=1 m=2").plot(f(n->1.0)(m->2.0))

//  val f = 3.5 * GHZ
//  val λ = lambda(f)
//  var S = vector(1 * domain(0.0 -> λ / 10, -λ / 20 -> λ / 20), 0 * ê)
//  var m = param("n")
//  var n = param("m")
//  var L = λ / 5
//  var W = λ / 10
//  var gp = vector(
//    cos((2 * n + 1) / 2 * PI / L * X) * cos(m * PI / W * (Y + W / 2)),
//    sin((2 * n + 1) / 2 * PI / L * X) * sin(m * PI / W * (Y + W / 2)),
//  ) *
//    domain(0.0 -> L, -W / 2 -> W / 2)
//  var gps = gp.inflate(n.in(0, 3).cross(m.in(0, 3)));
//  //var PS = columnVector(gps.length(), (i: Int) => complex(gps(i) ** S));
//  var S2 = columnEVector(gps.length(), (i: Int) => (gps(i) ** S) * gps(i)).sum();
//  //Plot.plot(PS)
//  Plot.domain(domain(0.0 -> a, -b / 2 -> b / 2)).plot(gps)
//  Plot.domain(domain(0.0 -> a, -b / 2 -> b / 2)).plot()
//  Plot.domain(domain(0.0 -> a, -b / 2 -> b / 2)).plot(S, S2)
}
