package net.thevpc.scholar.mentoring.ch02_hadrumaths

//these are Main Mandatory Imports
// export MESA_LOADER_DRIVER_OVERRIDE=i965
import net.thevpc.scholar.hadrumaths.Axis
import net.thevpc.scholar.hadrumaths.MathScala._

object T01_HelloHadrumaths extends App{
//  var x=4;
  var π=PI;
  sin(π) //value
  var x=3;
  var h=cos(2*π / 3 * X)*II(0.0->π)  //function
  Plot.plot(h);

  var f=sin(X+Y)*II(0.0->2*PI,0.0->2*PI)

  Plot.title("First").
    domain(II(-10.0->10.0,-10.0->10.0)).
    asMesh().
    plot(f)
  var g=sin(X+Y)
  Plot.title("Second").
    domain(II(-10.0->10.0,-10.0->10.0)).
    asHeatMap().
    plot(g)
}
