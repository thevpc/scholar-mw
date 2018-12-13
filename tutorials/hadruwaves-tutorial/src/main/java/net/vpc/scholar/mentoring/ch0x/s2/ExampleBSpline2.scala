package net.vpc.scholar.mentoring.ch0x.s2

import net.vpc.scholar.hadrumaths.Maths.{X, domain, dtimes, elist}
import net.vpc.scholar.hadrumaths.{Expr, Plot}
import net.vpc.scholar.hadrumaths.MathScala._

/**
  * Created by vpc on 5/27/17.
  */
object ExampleBSpline2 extends App {
  def R(x0: Double, a: Double, c: Double): Expr = {
    c * 2 * (X - x0) / a * domain(x0 -> (x0 + a / 2)) + c * ((-2) * (X - x0) / a + 2) * domain((x0 + a / 2) -> (x0 + a));
  }

  var S = elist();
  dtimes(0, 24.0 / 5, 4).foreach(i => S = S :+ R(i, 16.0 / 5, 5))
  //Plot.asReal().plot(S);

  def N(i: Int, p: Int, xi: Array[Double] = dtimes(0, 1, 10)): Expr = {
    if (p == 0) {
      1 * domain(xi(i) -> xi(i + 1));
    } else if (p > 0) {
      ((X - xi(i)) / (xi(i + p) - xi(i))) * N(i, p - 1, xi) + ((xi(i + p + 1) - X) / (xi(i + p + 1) - xi(i + 1))) * N(i + 1, p - 1, xi);
    }else {
      0
    }

  }
  var xi : Array[Double] = dtimes(0, 1, 10);
  Plot.asReal().plot(N(2,3,xi));//*domain(0.0->8.0)

  // Plot.asReal().plot(3*X*domain(0.0->1.0)+3*(-X+2)*domain(1.0->2.0));


}
