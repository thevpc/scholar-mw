package net.vpc.scholar.mentoring.ch02_hadrumaths

//these are Main Mandatory Imports
import net.vpc.scholar.hadrumaths.MathScala._

object T03_Dump extends App {
  var a=1->3
  var b=Array(1,3)
  var c=Array(Array(1,3),Array(4,5))
  var d=Array(Array(1+î,3),Array(4,5))

  println(a)
  println(b)
  println(c)
  println(d)

  println(dump(a))
  println(dump(b))
  println(dump(c))
  println(dump(d))
}
