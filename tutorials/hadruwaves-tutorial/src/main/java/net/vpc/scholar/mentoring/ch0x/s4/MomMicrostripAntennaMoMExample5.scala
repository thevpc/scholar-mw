/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.mentoring.ch0x.s4

import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadrumaths.meshalgo.rect._
import net.vpc.scholar.hadrumaths.plot.console.PlotData
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector
import net.vpc.scholar.hadruwaves.Physics._
import net.vpc.scholar.hadruwaves.mom._;import net.vpc.common.util.Chronometer;import net.vpc.common.util.mon.{ProgressMonitor, ProgressMonitorFactory}

object MomMicrostripAntennaMoMExample5 {

  def main(args: Array[String]): Unit = {

    var f = 5 * GHZ
    println(lambda(f))
    //box
    var a = 100 * MM
    var b = 100 * MM
    var d = domain(0.0 -> a, -b/2 -> b/2)

    //line (relative to box)
    var ll = 60 * MM
    var lw =  10 * MM
    var ld = domain(0.0 -> ll, (-lw/2) -> (lw/2))
    //source
    var sl = 2 * MM
    var sw = lw
    var sd = domain(0.0 -> sl, -sw / 2 -> sw / 2)

    val nbBaseFunctions: Int = 1000
    val nbTestFunctions: Int = 8



    var m: MomStructure = MomStructure.EEEE(d, f, nbBaseFunctions
      , BoxSpaceFactory.shortCircuit(1*MM, 2.2)
      , BoxSpaceFactory.shortCircuit(100*MM, 1)
    )

    m.setSources(sd*1, 50)
    m.setTestFunctions(
      TestFunctionsFactory.createBuilder()
        .setBounds(d)
        .addGeometry(ld)
        .setComplexity(nbTestFunctions)
      .buildBoxModes()
    )
//    m.setTestFunctionsCount(4);
    val arr: Array[DoubleToVector] = m.getTestFunctions.arr()
    println(arr(0).computeMatrix(0.01,0.5))
    var c = Plot.console()
    c.run(
      new PlotData()
        .setStructure(m)
        .addY(MomParamFactory.axis.structureDefinition())
    )
    c.createPlot()
        .setStructure(m)
        .setX(xyParamSet(100, 100))
        .addY(MomParamFactory.axis.testFunctions())
        .addY(MomParamFactory.axis.modeFunctions())
        .addY(MomParamFactory.axis.current2D(Axis.X))
        .addY(MomParamFactory.axis.current3D())
        .addY(MomParamFactory.axis.electricField3D())
      .plot()

  }
}
