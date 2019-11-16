package net.vpc.scholar.mentoring.ch02_hadrumaths

//these are Main Mandatory Imports
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadruwaves._
import net.vpc.scholar.hadruwaves.Physics._

object T06_Iter extends App{
  val a1: Array[Int] = isteps(5)
  println("-----------------------------------------------")
  println("println(a1):")
  println(a1)

  println("-----------------------------------------------")
  println("println(dump(a1)):")
  println(dump(a1))

  println("-----------------------------------------------")
  println("println(dump(isteps(1,4))):")
  println(dump(isteps(1,4)))

  println("-----------------------------------------------")
  println("println(dump(isteps(1,4,2))):")
  println(dump(isteps(1,4,2)))

  println("-----------------------------------------------")
  println("println(dump(dsteps(1,4,2))):")
  println(dump(dsteps(1,4,2)))

  println("-----------------------------------------------")
  println("println(dump(itimes(1,10,3))):")
  println(dump(itimes(1,10,3)))

  println("-----------------------------------------------")
  println("println(dump(dtimes(1,10,3))):")
  println(dump(dtimes(1,10,3)))


  println("-----------------------------------------------")
  println("dtimes(1,5,10).forEach(i=>println(i)):")
  dtimes(1,5,10).forEach(i=>println(i))

  println("-----------------------------------------------")
  println("(1::5).foreach(i=>println(i)):")
  (1::5).foreach(i=>println(i))

  println("-----------------------------------------------")
  println("(1::0.5::5).foreach(i=>println(i)):")
  (1::0.5::5).foreach(i=>println(i))
}
