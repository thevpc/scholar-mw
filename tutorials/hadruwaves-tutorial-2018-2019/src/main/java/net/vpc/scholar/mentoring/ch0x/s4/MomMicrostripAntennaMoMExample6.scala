package net.vpc.scholar.mentoring.ch0x.s4

import net.vpc.scholar.hadrumaths.Domain._
import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadruplot.PlotType._
import net.vpc.scholar.hadruwaves.mom.BoxSpaceFactory._
import net.vpc.scholar.hadruwaves.mom.MomParamFactory.axis._
import net.vpc.scholar.hadruwaves.mom.SourceFactory.createPlanarSource
import net.vpc.scholar.hadruwaves.mom._
import net.vpc.scholar.hadruplot.{Plot, Samples}

object MomMicrostripAntennaMoMExample6 {
  def main(args: Array[String]): Unit = {
    val a = 100.567 * MM ; val b = 30 * MM ; val s = 0.786 * MM
    val d = 2.812 * MM ; val l = 5.69 * MM ; val L = 22.760 * MM
    val W = 5.989 * MM ; val att = 2 * l / 1.2 ;  val box = forWidth(0, a, -b / 2, b)
    val f = 4.79 * GHZ ; val modes = 1000 ;val substrateEpsr = 2.2;val superstrateEpsr = 1
    val lineBox = forWidth(0, l, -d / 2, d)
    val patchBox = forWidth(l, L, -W / 2, W)
    val attachBox = forWidth(l - att / 2, att, -d / 2, d)
    val str = MomStructure.EEEE(box, f, modes, shortCircuit(substrateEpsr, 1.59 * MM)
                , matchedLoad(superstrateEpsr))
    str.setSources(createPlanarSource(1, 50, Axis.X, forWidth(0, s, -d / 2, d)))
    str.setTestFunctions(new TestFunctionsBuilder()
            .addGeometry(lineBox).setComplexity(6).applyBoxModes // line test functions
            .addGeometry(patchBox).setComplexity(20).applyBoxModes //patch test functions
            .addGeometry(attachBox).setComplexity(1).applyBoxModes //attach test function
      .build
    )
    Plot.plot(str.current().computeVector(Axis.X,Samples.relative(100,1)))
    Plot.console.createPlot.setStructure(str)
      .setX(xyParamSet(100, 50)) // precision (100 samples on X axis, ...)
      .addY(testFunctions()) //plot test functions
      .addY(current3D().setPlotType(HEATMAP)) // plot current density
      .addY(electricField3D()) // plot surface electric field
      .plot()
  }
}
