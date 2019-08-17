package net.vpc.scholar.mentoring.ch02_hadrumaths

//these are Main Mandatory Imports
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadruwaves._
import net.vpc.scholar.hadruwaves.Physics._

object T06_Params {
  var n = param("n")
  var m = param("m")
  var t = param("p")

  //one param function
  var f = n * X
  var all_f = seq(f, n, 1, 5)

  var all_f_but_zero = seq(f, n, 5, (n:Int) => n > 0)

  //three param function
  var f3 = (n * X + m * Y + t * Z) * II(1.3 -> 2.0)
  var all_f3_but_zero_zeror3 = seq(f3, m, 3, n, 3, t, 3, (m, n, t) => (m != 0 && n != 0 && t != 0));
  var all_f3_but_zero_zeror2 = seq(f3, m, 3, n, 3, (m, n) => (m != 0 && n != 0));
  var all_f3_but_zero_zeror1 = seq(f3, m, 3, (mm:Int) => (mm != 0));

}
