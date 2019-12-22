package net.vpc.scholar.mentoring.ch02_hadrumaths

//these are Main Mandatory Imports
import net.vpc.scholar.hadrumaths.MathScala._

object T02_Complex extends App {
  var h = 1 + 2 * î
  var y=0*î
  y=h
  println(h)
  println(y)
  println(h.acos())
  println(acos(h))

  println(conj(h))
  println(h.conj())
  println(h.conj)
  println(h conj)
}
