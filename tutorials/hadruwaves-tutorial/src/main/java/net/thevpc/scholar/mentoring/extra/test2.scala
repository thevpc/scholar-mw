package net.thevpc.scholar.mentoring.extra

import net.thevpc.scholar.hadrumaths.MathScala._
import net.thevpc.scholar.hadruwaves.Physics._

object test2 extends App {
  val f = 3.5 * GHZ
  val λ = lambda(f)
  var S = vector(1 * domain(0.0 -> λ / 10, -λ / 20 -> λ / 20), 0 * ê)
  var m = param("n")
  var n = param("m")
  var L = λ / 5
  var W = λ / 10
  var a = λ / 2
  var b = λ / 2
  var gp = vector(
    cos((2 * n + 1) / 2 * PI / L * X) * cos(m * PI / W * (Y + W / 2)),
    sin((2 * n + 1) / 2 * PI / L * X) * sin(m * PI / W * (Y + W / 2)),
  ) *
    domain(0.0 -> L, -W / 2 -> W / 2)
  var gps = gp.inflate(n.in(0, 3).cross(m.in(0, 3)));
  //var PS = columnVector(gps.length(), (i: Int) => complex(gps(i) ** S));
  var S2 = columnEVector(gps.length(), (i: Int) => (gps(i) ** S) * gps(i)).sum();
  //Plot.plot(PS)
  Plot.domain(domain(0.0 -> a, -b / 2 -> b / 2)).plot(gps)
  Plot.domain(domain(0.0 -> a, -b / 2 -> b / 2)).plot()
  Plot.domain(domain(0.0 -> a, -b / 2 -> b / 2)).plot(S, S2)
}
