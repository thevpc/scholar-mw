package net.vpc.scholar.mentoring.ch02_hadrumaths

//these are Main Mandatory Imports
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadruplot.Plot
import net.vpc.scholar.hadruwaves._
import net.vpc.scholar.hadruwaves.Physics._

object T01_HelloHadrumaths extends App{
  var x=4;
  var f=sin(X+Y)*II(0.0->2*PI,0.0->2*PI)
  Plot.title("First").
    domain(II(-10.0->10.0,-10.0->10.0)).
    asHeatMap().
    plot(f)
  var g=sin(X+Y)
  Plot.title("Second").
    domain(II(-10.0->10.0,-10.0->10.0)).
    asHeatMap().
    plot(g)
}
