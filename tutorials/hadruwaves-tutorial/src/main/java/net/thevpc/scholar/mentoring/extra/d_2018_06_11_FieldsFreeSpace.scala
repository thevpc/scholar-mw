package net.thevpc.scholar.mentoring.extra

import net.thevpc.scholar.hadrumaths.MathScala._
import net.thevpc.scholar.hadruwaves.Physics._
import net.thevpc.scholar.hadrumaths.{Axis, InverseStrategy, Maths, ScalarProductOperatorFactory}
import net.thevpc.scholar.hadruwaves.Material
import net.thevpc.scholar.hadruwaves.mom.BoxSpace.{matchedLoad, shortCircuit}
import net.thevpc.scholar.hadruwaves.mom.MomStructure

object d_2018_06_11_FieldsFreeSpace {

  Maths.Config.setSimplifierCacheSize(1000000);
  Maths.Config.setRootCachePath("d_2018_06_10")
  Maths.Config.setAppCacheName("fields")
  var MN = 200000
  var PPatch = 13
  var PLine = 9
  var π = PI
  // Parameters
  var freq = 5.0 * GHZ;
  var wfreq = 2 * π * freq;
  var lambda = C / freq;
  val epsr = 4.32;
  val substrate = Material.substrate(epsr);
  val epsilon = EPS0
  val mu = U0
  val Z0 = sqrt(U0 / EPS0)
  var k0 = 2 * π / lambda
  var a = (100.0 * MM);
  var b = (40.0 * MM);
  var ep = 1.5 * MM;
  var dBox = domain(0.0 -> a, -b / 2 -> b / 2)
  var ap = 60.0 * MM
  var bp = 40.0 * MM
  var dPlot = domain(0.0 -> ap, -bp -> bp)
  var d = 2.860 * MM;
  var l = 18.815 * MM;
  var L = 13.227 * MM;
  var W = 26.688 * MM;
  var dLine = domain(0.0 -> (l + L / 1.2), -d / 2 -> d / 2)
  var dPatch = domain(l -> (l + L), -W / 2 -> W / 2)
  var s = 4.0 * MM;
  var dSource = domain(0.0 -> s, -d / 2 -> d / 2)

  var p = param("p")
  var q = param("q")

  var st: MomStructure = null;

  def main(args: Array[String]): Unit = {
    Maths.Config.setDefaultMatrixInverseStrategy(InverseStrategy.SOLVE)
    Maths.Config.setScalarProductOperator(ScalarProductOperatorFactory.hardFormal()); // !=quad
    var cr = chrono()
    val console = Plot.console()
    val monitor = console.logger("Total")
    //=========================================== Test Functions ========================================
    var gp = elist()
    var testX = ((cos((2 * p + 1) * π * X / (2 * (l + L / 1.2))) * cos(q * π / d * (Y + d / 2))) * dLine)
    var testY = ((sin((2 * p + 1) * π * X / (2 * (l + L / 1.2))) * sin(q * π / d * (Y + d / 2))) * dLine)
    var linetest = vector(testX, testY)
    var essaiPatchX = ((sin(p * π * (X - l) / L) * cos(q * π * (Y + W / 2) / W)) * dPatch);
    var essaiPatchY = ((cos(p * π * (X - l) / L) * sin(q * π * (Y + W / 2) / W)) * dPatch);
    var patchtest = vector(essaiPatchX, essaiPatchY)
    gp :+= /*normalize*/ (linetest.inflate(p.in(0, PLine - 1).cross(q.in(0, PLine - 1))));
    gp :+= /*normalize*/ (patchtest.inflate(p.in(0, PPatch - 1).cross(q.in(0, PPatch - 1)).where((pp: Double, qq: Double) => (pp != 0 || qq != 0))))
    Plot.title("test functions ").domain(dPlot).plot(gp)
    //=================================== structure definition ===========================================
    var E0 = vector(normalize(dSource), 0)
    st = MomStructure.EEEE(dBox, freq, MN, shortCircuit(substrate, ep), matchedLoad())
    st.setSources(E0, 50)
    st.setTestFunctions(gp)
    //=========================================== Fields Evaluation ======================================
    var Jx = st.current().monitor(console)
      .evalMatrix(Axis.X, dtimes(0, a, 200), dtimes(-b / 2, b / 2, 100), 0)
    Plot.title(" Jx").domain(dPlot).plot(Jx)

    var Jy = st.current().monitor(console)
      .evalMatrix(Axis.Y, dtimes(0, a, 200), dtimes(-b / 2, b / 2, 100), 0)
    Plot.title(" Jy").domain(dPlot).plot(Jy)

    var Ex = st.electricField().monitor(console).cartesian()
      .evalMatrix(Axis.X, dtimes(0, a, 200), dtimes(-b / 2, b / 2, 100), 0)
    Plot.title(" Ex").domain(dPlot).plot(Ex)

    var Ey = st.electricField().monitor(console).cartesian()
      .evalMatrix(Axis.Y, dtimes(0, a, 200), dtimes(-b / 2, b / 2, 100), 0)
    Plot.title(" Ey").domain(dPlot).plot(Ey)

    var ExV = st.electricField().monitor(console).cartesian()
      .evalVector(Axis.X, dtimes(0, a, 200), 0, 0)
    Plot.title(" Ex(x) ").domain(dPlot).plot(ExV.transpose())

    var EyV = st.electricField().monitor(console).cartesian()
      .evalVector(Axis.Y, dtimes(0, a, 200), 0.0, 0.0)
    Plot.title(" Ey(x) ").domain(dPlot).plot(EyV.transpose())


    //println(Ex)

  }
}
