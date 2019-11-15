package net.vpc.scholar.mentoring.ch02_hadrumaths

//these are Main Mandatory Imports
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths.MathScala._

object T09_Params extends App{
  var n=param("n")

  //one param function
  var f=(n+1)*X

  var g=sin(n*X)
  var f0=f(n->0.0)
  var f1=f(n->1.0)
  Plot.title("f0").plot(f0)
  Plot.title("f1").plot(f1)
  var all=seq(f,n,dsteps(5,20))
  var f5=all(0)
  all=all:+seq(g,n,dsteps(1,10))
  Plot.title("f*").plot(all)

  var m = param("m")
  var t = param("p")

  var all_f = seq(f, n, 1, 5)

  var all_f_but_zero = seq(f, n, 5, (n:Int) => n > 0)

  //three param function
  var f3 = (n * X + m * Y + t * Z) * II(1.3 -> 2.0)
  var all_f3_but_zero_zeror3 = seq(f3, m, 3, n, 3, t, 3, (m, n, t) => (m != 0 && n != 0 && t != 0));
  var all_f3_but_zero_zeror2 = seq(f3, m, 3, n, 3, (m, n) => (m != 0 && n != 0));
  var all_f3_but_zero_zeror1 = seq(f3, m, 3, (mm:Int) => (mm != 0));

}
