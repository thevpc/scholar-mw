package net.vpc.scholar.mentoring.ch02_hadrumaths

//these are Main Mandatory Imports
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadruwaves._
import net.vpc.scholar.hadruwaves.Physics._

object T06_ScalarProducts extends App{
  var n=param("n")
  var f = seq(n * X, n, 1, 5) * II(0.0->10.0)
  var g = seq(sin(n * X), n, 1, 5)* II(0.0->10.0)
  var h = seq(cos(n * X), n, 1, 5)* II(0.0->10.0)
  var m =matrix(3,(i:Int,j:Int)=>complex(i*j))
  var cval =complex(f(0)**g(0))

  //function as sum of f(i)*g(i)
  var sumval : Expr=f**g
  println(sumval)

  //matrix m(i,j)=f(i)**f(j)
  var matrixVal=f:**g
  println(matrixVal)

  var sumMulVal=esum(f.size, (i:Int)=>f(i)*g(i)*h(i))
  println(sumMulVal)

}
