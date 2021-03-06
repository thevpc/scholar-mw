package net.thevpc.scholar.mentoring.ch03_hadruwaves.d_mom

import net.thevpc.scholar.hadrumaths.MathScala._
import net.thevpc.scholar.hadrumaths._
import net.thevpc.scholar.hadruwaves.Material
import net.thevpc.scholar.hadruwaves.Physics._
import net.thevpc.scholar.hadruwaves.mom._

/**
  * Created by vpc on 5/27/17.
  */
object MicrostripLineMoMExampleLayer extends App {
  var N = 2000        // number of modes
  var Gn = 4        // number of modes
  var a = 3 * CM // box length along x
  var b = 2 * CM  // box length along y
  var v = 1 * MM   // width (along x) of the source
  var l = 5 * MM   // length of the strip line
  var w = 1 * MM   // width (along y) of the strip line
  var f = 3 * GHZ     // frequency
  val space = matchedLoadBoxSpace(Material.VACUUM) // layer descr for the open componentVectorSpace, 1 refers to the espilon_r of the air/void
  val mass = shortCircuitBoxSpace(Material.substrate(2.2), 1 * CM) // layer descr for 1cm height of substrate, with espilon_r 2.2

  var lineDomain = domain(0.0 -> l * 1.5, -w -> w) // line domain
  var box = domain(0.0 -> a, -b / 2 -> b / 2) // box domain

  var m = param("m")  // m and n parameters
  var n = param("n")

  // defines a parametrized test function
  var gp = cos((2 * m + 1) * PI / 2 * X / l) * cos(n * PI / w * (Y + w / 2)
  ) * lineDomain

  //if(true) System.exit(0);
  private val console = Plot.console().setGlobal()
//  Plot.setDefaultWindowManager(console)
  println(gp)

  println(gp(m -> 3)) // replaces m by 3 in gp, remember m is a parameter using (->) operator
  println(gp(m -> 3) !!) // replaces m by 3 in gp and then simplifies it with the !! operator

  var g = gp.inflate(m.steps(0, Gn).cross(n.steps(0, Gn)))

  var P = g.length()     // test functions count
  var e0 = 1 * domain(0.0 -> v, -w / 2 -> w / 2)  // source
  var str=MomStructure.EEEE(box,f,N,mass,space).sources(e0).testFunctions(g).monitorFactory(console)
//  str.setScalarSurfaceImpedance(impedance(î*1E-3)) //serial
  str.setLayers(new StrLayer(1*MM,1*î)) //parallel
  Plot.title("fn").plot(str.modeFunctions.fn())
  var xprec=100
  var yprec=50
  Plot.title("schema").xsamples(1000).plot(lineDomain+lineDomain.scale(Align.CENTER,1.2)+box)
  var plotDomain=(lineDomain+lineDomain.scale(Align.CENTER,1.2)+box).domain();
  private val JJ: Expr = str.current().expr()
  private val EE: Expr = str.electricField().cartesian().expr()
  Plot.title("JJ").asHeatMap().domain(plotDomain).plot(JJ)
  Plot.title("EE").asHeatMap().domain(plotDomain).plot(EE)
}
