package net.thevpc.scholar.mentoring.ch02_hadrumaths

import net.thevpc.scholar.hadrumaths.MathScala._
import net.thevpc.scholar.hadrumaths._
import net.thevpc.scholar.hadruplot.Plot

object Tall_Examples {
  def main(args: Array[String]): Unit = {
    var n=param("n")
    var fx=sin(n*X) *domain(0.0 -> 2 * PI, 0.0 -> 2 * PI) + (n>1)*X;
    var allFx = fx.inflate(n.in(1, 5))
    Plot.plot(fx(n->2.0))
    Plot.plot(allFx)
    Plot.plot(deriveX(fx)(n->2.0))
    var m = matrix(
      3, (i: Int, j: Int) => i + î * j + i - j
    )
    var v = columnVector(10, (i: Int) => 0 * î + 3 * i)
    println(det(m) * inv(m));
    plot(v*m)
  }
}
