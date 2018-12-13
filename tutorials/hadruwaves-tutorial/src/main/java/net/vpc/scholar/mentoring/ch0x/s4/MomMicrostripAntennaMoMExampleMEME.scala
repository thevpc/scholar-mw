package net.vpc.scholar.mentoring.ch0x.s4

import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadruwaves.Physics._
import net.vpc.scholar.hadruwaves.mom.MomStructure

/**
  * Created by vpc on 4/25/17.
  */
object MomMicrostripAntennaMoMExample2MEME {
  def main(args: Array[String]): Unit = {
    val N = 1000
    var l = 12.5 * MM
    var s = 1 * MM
    var L = 18.75 * MM
    var a = 47.55 * MM
    var b = 4 * MM //22.15 * MM
    var w = 0.31 * MM
    var W = 1 * MM
    var h = 1 * MM //substrate thikness
    var espr = 2.2; //substrate permettivity
    var fr = 4 * GHZ
    var g = elist()
    var k = param("k")
    var k2 = param("k2")
    var k3 = param("k3")
    //var p = param("p");
    var Natt = 1.0
    var A = 2 * l / 3

    val boxDomain = domain(0.0 -> a, -b / 2 -> b / 2)
    val lineDomain = domain(0.0 -> l, -w / 2 -> w / 2)
    val srcDomain = domain(0.0 -> s, -w / 2 -> w / 2)
    val patchDomain = domain(l -> (L + l), (-W) / 2 -> W / 2)

    g :+= seq(cos(((2 * k + 1) * PI) * X / (2 * l)) * lineDomain, k, dsteps(0, 5))
    g :+= seq(sin((k2 * PI) * (X - l) / L) * cos((k3 * PI / W) * (Y + W / 2)) * patchDomain, k2, dsteps(1, 4), k3, dsteps(0, 4))
    g :+= (
      Natt * (X - l + A / 2) / A / 2 * domain(l - (A / 2) -> l, (-w) / 2 -> w / 2)
        +
        Natt * (l + A / 2 - X) / A / 2 * domain(l -> (l + (A / 2)), (-w) / 2 -> w / 2)
      )
    val P = g.size() // number of test functions

    var src = 1 * srcDomain
    val substrateSpace = shortCircuitBoxSpace(espr, h)
    val superstrateSpace = matchedLoadBoxSpace(1)

    //use optimized (fast) Mom implementation
    var mm = MomStructure.EMEM(boxDomain, fr, N, substrateSpace, superstrateSpace)
    mm.setTestFunctions(g)
    //    mm.setSources(src,50)
    mm.setSources(src)
    mm.getModeFunctions.setHintEnableFunctionProperties(true)

    val fn = mm.getModeFunctions.fn()//.slice(1, 100)
    Plot.Config.setDefaultWindowTitle("v57")
    Plot.title("fn").domain(boxDomain).plot(fn)
    Plot.title("g").domain(boxDomain).plot(g)
    Plot.title("A ref").plot(mm.inputImpedance().computeComplex())
    Plot.title("A ref").plot(mm.matrixA().computeMatrix())
    Plot.title("B ref").plot(mm.matrixB().computeMatrix())
    Plot.title("X ref").plot(mm.matrixX().computeMatrix())
    Plot.title("J test").domain(boxDomain).plot(mm.testField().computeMatrix(Axis.X, Samples.relative(100, 100)))
    Plot.title("J modes").domain(boxDomain).plot(mm.current().computeMatrix(Axis.X, Samples.relative(100, 100)))
    Plot.title("E").domain(boxDomain).plot(mm.electricField().computeMatrix(Axis.X, Samples.relative(100, 100)))
  }
}
