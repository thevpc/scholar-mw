/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.mentoring.ch0x.s3

import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadrumaths.plot._
import net.vpc.scholar.hadrumaths.Maths._

object ExampleFDTD extends App{
  //solve g= -u''

    var N = 50
    var d = 1
    var t = 0
    var sleep = 200
    var xvalues = dsteps(0, 10, d)
    var yvalues = Array.ofDim[Double](xvalues.length)
    var zero: Array[Array[Double]] = Array.ofDim[Array[Double]](0)
    var all: Array[Array[Double]] = zero
    var plot : PlotComponent =null;
    def yval(ii: Int): Double = {
      if (ii < 0 || ii >= yvalues.length) {
        0
      } else {
        yvalues(ii)
      }
    }
    var pc = Plot.addPlotContainer("Current")

    var ii = 0
    while (ii < yvalues.length) {
      var x = xvalues(ii)
      yvalues(ii) = g(t, x, N)
      ii += 1
    }
    while (t < N) {
      //last values
      var y = Array.ofDim[Double](yvalues.length)
      //current iteration
      var i = 0
      while (i < yvalues.length) {
        var x = xvalues(i)
        y(i) = g(t, x, N)
        y(i) += (yval(i + 1) + yval(i - 1) - 2*yval(t))/(d*d)
        i += 1
      }
      //boundary conditions
      y(0) = 0
      y(y.length - 1) = 0

      //update values
      i = 0
      while (i < yvalues.length) {
        yvalues(i)=y(i)
        i += 1
      }
      all = zero :+ yvalues
//      all = concatArr(all, yvalues);

      //update time
      t += 1
//      pc.removeAllPlotComponents();
      plot=Plot.title("t=" + t).update(plot).display(pc).nodisplay().asCurve.plot(all)
      println("t=" + t + " : " + yvalues.mkString(","))
      Thread.sleep(sleep)
      //c.
    }
    println("end.")


  def g(t: Double, x: Double, N: Double): Double = {
    var a1= 1
    var a2= 1
    var a3= 1
    var a4= 1
    var N2= N / 2.0
    var g = a1 + a2 * cos(PI * (t - N2) / N2) + a3 * cos(2 * PI * (t - N2) / N2) + a3 * cos(3 * PI * (t - N2) / N2)
    if (x >= 0 && x <= 5) {
      g
    } else {
      0
    }
  }

}
