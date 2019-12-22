package net.vpc.scholar.mentoring.ch03_hadruwaves.d_mom

import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadruwaves.Physics._
import net.vpc.scholar.hadruwaves.mom._

/**
  * Created by vpc on 5/27/17.
  */
object MicrostripLineMoMExample2 extends App {
  var N = 2000        // number of modes
  var Gn = 4        // number of modes
  var a = 3 * CM // box length along x
  var b = 2 * CM  // box length along y
  var v = 1 * MM   // width (along x) of the source
  var l = 5 * MM   // length of the strip line
  var w = 1 * MM   // width (along y) of the strip line
  var f = 3 * GHZ     // frequency
  val space = matchedLoadBoxSpace(1) // layer descr for the open componentVectorSpace, 1 refers to the espilon_r of the air/void
  val mass = shortCircuitBoxSpace(2.2, 1 * CM) // layer descr for 1cm height of substrate, with espilon_r 2.2

  var lineDomain = domain(0.0 -> l * 1.5, -w -> w) // line domain
  var box = domain(0.0 -> a, -b / 2 -> b / 2) // box domain

  var m = param("m")  // m and n parameters
  var n = param("n")

  // defines a parametrized test function
  var gp = cos((2 * m + 1) * PI / 2 * X / l) * cos(n * PI / w * (Y + w / 2)
  ) * lineDomain

  //if(true) System.exit(0);
  private val console = Plot.console()
  println(gp)

  println(gp(m -> 3)) // replaces m by 3 in gp, remember m is a parameter using (->) operator
  println(gp(m -> 3) !!) // replaces m by 3 in gp and then simplifies it with the !! operator

  // evaluates a sequence/list of all values of gp test functions where
  // m varies from 0 to Gn (inclusive) an so does n
  var g = gp.inflate(
    m.in(0, Gn)
      .cross(n.in(0, Gn))
  )

  var P = g.length()     // test functions count
  var e0 = 1 * domain(0.0 -> v, -w / 2 -> w / 2)  // source
  var str=MomStructure.PPPP(box,f,N,mass,space).sources(e0)
//    .setProjectType(ProjectType.WAVE_GUIDE)
//    .setSources(new CutOffModalSourcesSingleMode(1))
    .testFunctions(g).monitor(console)
  str.setSerialZs(impedance(î*1E-3)) //serial
  str.setLayers(Array(new StrLayer(1*MM,1*î))) //parallel
  console.Plot.title("fn").plot(str.getModeFunctions.fn())
  var xprec=100
  var yprec=50
  console.Plot.title("schema").xsamples(1000).plot(lineDomain+lineDomain.scale(Align.CENTER,1.2)+box)
  var plotDomain=(lineDomain+lineDomain.scale(Align.CENTER,1.2)+box).domain();
//  private val J1: Matrix = str.current().computeMatrix(Axis.X, lineDomain.scale(Align.CENTER,1.3).times(xprec, yprec))
//  private val J2: Matrix = str.current().computeMatrix(Axis.X, lineDomain.times(xprec, yprec))
//  private val J3: Matrix = str.current().computeMatrix(Axis.X, box.times(xprec*5, yprec*3))
//  private val J4: Matrix = str.current().computeMatrix(Axis.X, plotDomain.times(xprec, yprec))
//  private val J: Matrix = str.current().monitor(console.taskMonitor).computeMatrix(Axis.X, lineDomain.scale(2).times(100, 30))
//  Plot.title("Current 1").asHeatMap().domain(plotDomain).plot(J1)
//  Plot.title("Current 2").asHeatMap().domain(plotDomain).plot(J2)
//  Plot.title("Current 3").asHeatMap().domain(plotDomain).plot(J3)
//  Plot.title("Current 4").asHeatMap().domain(plotDomain).plot(J4)
  private val JJ: Expr = str.current().expr()
  private val EE: Expr = str.electricField().expr()
  console.Plot.title("str.current().expr()*plotDomain").asHeatMap().domain(plotDomain).plot(JJ*plotDomain)
  console.Plot.title("str.current().expr()*plotDomain+1E-4*lineDomain").asHeatMap().domain(plotDomain).plot(JJ*plotDomain+1E-4*lineDomain)
  console.Plot.title("str.current().expr()1E-4*lineDomain").asHeatMap().domain(plotDomain).plot(JJ+1E-4*lineDomain)
  console.Plot.title("str.current().expr()").asHeatMap().domain(plotDomain).plot(JJ)
}
