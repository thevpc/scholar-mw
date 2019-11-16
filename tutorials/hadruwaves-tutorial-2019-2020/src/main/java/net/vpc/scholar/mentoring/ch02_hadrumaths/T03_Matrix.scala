package net.vpc.scholar.mentoring.ch02_hadrumaths

//these are Main Mandatory Imports

import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadruplot.Plot
import net.vpc.scholar.hadruwaves._
import net.vpc.scholar.hadruwaves.Physics._

object T03_Matrix extends App {
  var m1 = matrix(
    3, (i: Int, j: Int) => i + î * j + i - j
  )
  var m2 = symmetricMatrix(
    3, (i: Int, j: Int) => i + î * j + i - j
  )
  var m3 = hermitianMatrix(
    3, (i: Int, j: Int) => i + î * j + i * j
  )
  var m4 = diagonalMatrix(
    3, (i: Int, j: Int) => i + î * j + j
  )
  var m5 = matrix("[2 -3 ; 4 -7]")
  println("m1", m1)
  println("m2", m2)
  println("m3", m3)
  println("m4", m4)
  Plot.title("m1").plot(m1)
  Plot.title("m2").plot(m2)
  Plot.title("m3").plot(m3)
  Plot.title("m4").plot(m4)
  Plot.title("m5").plot(m5)
  println(m1(1, 2))
  println(m1 det)
  println(det(m5))
  println(inv(m5))
  println(m1 * m1)
  //  println(m**2)

}
