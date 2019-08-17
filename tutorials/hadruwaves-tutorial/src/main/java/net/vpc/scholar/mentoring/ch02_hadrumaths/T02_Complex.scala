package net.vpc.scholar.mentoring.ch02_hadrumaths

//these are Main Mandatory Imports
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadruwaves._
import net.vpc.scholar.hadruwaves.Physics._

object T02_Complex extends App {
  var h = 1 + 2 * î
  var y=0*î
  y=h

  h.acos()
  acos(h)

  println(conj(h))
  println(h.conj)
}
