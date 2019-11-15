/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.mentoring.ch0x.s2

import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadruplot.Plot
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
      fct.setTitle("B" + p + "(" + i + ")=" + fct)
    }
  }
}
