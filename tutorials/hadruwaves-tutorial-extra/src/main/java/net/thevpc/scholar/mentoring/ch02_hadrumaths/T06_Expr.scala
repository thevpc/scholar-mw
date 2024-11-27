package net.thevpc.scholar.mentoring.ch02_hadrumaths

import net.thevpc.scholar.hadrumaths.Axis

import java.util
import net.thevpc.scholar.hadruplot.Plot

//these are Main Mandatory Imports
import net.thevpc.scholar.hadrumaths.MathScala._

object T06_Expr extends App{

  var f=sin(X)
  var g=sin(X)

  Plot.xsamples(1000).plot(f)

  var fx=X+î
  deriveX(fx)
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
