/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.mentoring.ch0x.s3

import net.vpc.scholar.mentoring.ch0x.s3.ExampleFDTD.{all, pc, plot}
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadruplot._
import net.vpc.scholar.hadrumaths.util.dump._

object ExampleFDTDYee {
  //solve g= -u''
  def main(args: Array[String]) {
    var SIZE = 200
    var ez = Array.ofDim[Double](SIZE)
    var hy = Array.ofDim[Double](SIZE)

    var imp0 = 377.0
    var t = 0
    var N = 200
    var mm = 0
    var sleep = 100
    var zero: Array[Array[Double]] = Array.ofDim[Array[Double]](0)
    var all: Array[Array[Double]] = zero
    var plot : PlotComponent =null;

    var pc = Plot.addPlotContainer("Current")
    t = 0
    while (t < N) {
      /* update magnetic field */
      mm = 0
      while (mm < SIZE - 1) {
        hy(mm) = hy(mm) + (ez(mm + 1) - ez(mm)) / imp0
        mm += 1
      }
      /* update electric field */
      mm = 1
      while (mm < SIZE) {
        ez(mm) = ez(mm) + (hy(mm) - hy(mm - 1)) * imp0
        mm += 1
      }

      /* use additive source at node 50 */
      ez(50) += Math.exp(-(t - 30.0) * (t - 30.0) / 100.0)
      all = zero :+ hy
      all = all :+ ez
      t += 1


      plot=Plot.title("Result").update(plot).display(pc).nodisplay().asCurve.plot(all)
      Thread.sleep(sleep)
      //c.
    }
    println("end.")
  }

  def g(t: Double, x: Double, N: Double): Double = {
    var a1 = 1
    var a2 = 1
    var a3 = 1
    var a4 = 1
    var N2 = N / 2.0
    var g = a1 + a2 * cos(PI * (t - N2) / N2) + a3 * cos(2 * PI * (t - N2) / N2) + a3 * cos(3 * PI * (t - N2) / N2)
    if (x >= 0 && x <= 5) {
      g
    } else {
      0
    }
  }

}
