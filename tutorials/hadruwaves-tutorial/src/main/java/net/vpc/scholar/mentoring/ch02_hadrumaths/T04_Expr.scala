package net.vpc.scholar.mentoring.ch02_hadrumaths

import java.util

import net.vpc.scholar.hadruplot.Plot

//these are Main Mandatory Imports
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadruwaves._
import net.vpc.scholar.hadruwaves.Physics._

object T04_Expr extends App{

  var f=sin(X)
  Plot.plot(f)

//  var fx=X+î
////  derive(fx,X,Y)
//  var fxc=DC(fx) //Double To Complex
//  println(fxc(3.5))
//  println(fxc(3.5,3))
//
//  var fxy=X+Y
//  var fxyd=DD(fx) //Double To Double
////  println(fxyd(3.5))
////  println(fxyd(3.5,3))
//
//  Plot.title("f").plot(fx)
}
