package net.vpc.scholar.mentoring.ch02_hadrumaths

import java.util

//these are Main Mandatory Imports
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadruwaves._
import net.vpc.scholar.hadruwaves.Physics._

object T04_Expr extends App{
//  println(java.util.Arrays.toString(dsteps(1,1,0.1)))
//  if (true) System.exit(0)
  var n=param("n")
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
}
