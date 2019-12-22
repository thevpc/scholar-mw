package net.vpc.scholar.mentoring.ch03_hadruwaves.b_fem

import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadrumaths._
object ExampleFEM extends App {
  Config.setCacheExpressionPropertiesEnabled(false);
  //solve g= -u''
  //    var formalSolution=(-0.5)*(X*X)+10*X;
  var d = domain(0.0 -> 10.0)
  var points = (d / 100).xvalues
  var p = 2
  var w = elist(isteps(points.length - p - 2).map(i => bspline(i, p, points)): _ *)
  Plot.title("splines "+p).plot(w)
  if(true) {
    var g = 1 * II(0.0 -> 5.0); //excitation

    var B = columnMatrix(w.length, (i: Int) => complex(g ** w(i)))
    var A = matrix(w.length, (r: Int, c: Int) => {
      val wr = bspline_der(r, p, points)
      val wc = bspline_der(c, p, points)
      complex(integrate(wr * wc))
    })
    //A U = G
    // ==> U = inv(A)*G
    var Ui = inv(A) * B

    Plot.update("A").title("A").asMatrix().plot(A)
    Plot.update("G").title("G").asMatrix().plot(B)
    Plot.update("inv(A)").title("inv(A)").asMatrix().plot(inv(A))
    Plot.update("Ui").title("Ui").asMatrix().plot(Ui)

    var U = w ** Ui
    //Plot.title("U samples").samples(100,100).plot(w.map((i,wi)=>{wi*Ui(i)}))
    Plot.update("U").title("U").xsamples(1000).plot(U * d)
  }

  /**
    * i : spline index
    * p : spline order
    * v : control values
    */
  def bspline(i: Int, p: Int, v: Array[Double]): Expr = {
    if (p == 0) {
      1 * II(v(i) -> v(i + 1))
    } else if (i >= 1 && i <= (v.length - 1 - p - 1)) {
      (X - v(i)) / (v(i + p) - v(i)) * bspline(i, p - 1, v) + (v(i + p + 1) - X) / (v(i + p + 1) - v(i + 1)) * bspline(i + 1, p - 1, v)
    } else {
      0
    }
  }

  def bspline_der(i: Int, p: Int, v: Array[Double]): Expr = {
    if (p == 0) {
      0 * î
    } else {
      ((p / (v(i + 1) - v(i))) * bspline(i, p - 1, v)) - ((p / (v(i + p + 1) - v(i + 1))) * bspline(i + 1, p - 1, v))
    }
  }
}
