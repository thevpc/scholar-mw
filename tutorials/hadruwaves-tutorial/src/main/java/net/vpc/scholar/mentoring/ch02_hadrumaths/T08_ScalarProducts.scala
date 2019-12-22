package net.vpc.scholar.mentoring.ch02_hadrumaths

//these are Main Mandatory Imports
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadrumaths.MathScala._

object T08_ScalarProducts extends App{
  var n=param("n")
  var f = (n * X).inflate(n.in(1, 5)) * II(0.0->10.0)
  var g = sin(n * X).inflate(n.in(1, 5))* II(0.0->10.0)
  var h = cos(n * X).inflate(n.in(1, 5))* II(0.0->10.0)
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
