package net.thevpc.scholar.mentoring.ch03_hadruwaves.a_testfunctions

import net.thevpc.scholar.hadrumaths.Expr
import net.thevpc.scholar.hadrumaths.MathScala._

object ExampleBSpline extends App{
  var d = domain(0.0 -> 10.0)
  var ksi = d.xtimes(10)
  isteps(5).foreach(p => {
    var splines = isteps(ksi.length - 2 - p).map(i => N(i, p))
    Plot.title("splines " + p).plot(splines)
  })

  /**
   * i : spline index
   * p : spline order
   */
  def N(i: Int, p: Int): Expr = {
    if (p == 0) {
      var fct = II(ksi(i) -> ksi(i + 1))
      fct.setTitle("B" + p + "(" + i + ")=" + fct)
    }else {
      var fct = ((X - ksi(i))
        / (ksi(i + p) - ksi(i))
        * N(i, p - 1)
        + (ksi(i + p + 1) - X)
        / (ksi(i + p + 1) - ksi(i + 1))
        * N(i + 1, p - 1))
      fct.setTitle("B" + p + "(" + i + ")")
    }
  }
}
