package net.vpc.scholar

import net.vpc.scholar.d_15_12_2017_relativeError_SRCTestFunctions.PLine
import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadrumaths.Maths.expr
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadruplot.{Plot, PlotLines}
import net.vpc.scholar.hadruwaves.mom.BoxSpace._
import net.vpc.scholar.hadruwaves.mom._

object d_15_12_2017_relativeError_SRCTestFunctions {
  Config.setSimplifierCacheSize(1000000)
  .setScalarProductOperator(FORMAL_SP)
  .setCacheEnabled(false)
  // length variation with initial box:
//  Maths.Config.setRootCachePath("D:\\cache\\d_15_12_2017")

//  var π = PI
  // Parameters
  var freq = 4.79 * GHZ; // to change
  var wfreq = 2 * π * freq;
  var lambda = C / freq;
  val epsr = 2.2;
  val epsilon = EPS0
  val mu = U0
  val Z0 = sqrt(U0 / EPS0)
  var k0 = 2 * π / lambda
  var a = (100.567 * MM)//*2;
  var b = (30 * MM)//*2;
  var ep = 1.59 * MM;
  var dBox = domain(0.0 -> a, -b / 2 -> b / 2)
  var ap = 50 * MM
  var bp = 5 * MM
  var dPlot = domain(0.0 -> ap, -bp -> bp)
  var d = 2.812 * MM;
  var l = 5.69 * MM;
  var L = 22.760 * MM;
  var W = 5.989 * MM;
  var dLine = domain(0.0 -> (l + l / 1.2), -d / 2 -> d / 2)
  var dPatch = domain(l -> (l + L), -W / 2 -> W / 2)
  var s = 0.786 * MM;
  var dSource = domain(0.0 -> s, -d / 2 -> d / 2)
  var PPatch = 5
  var PLine = 3
  var p = param("p")
  var q = param("q")

  var st: MomStructure = null;

  def main(args: Array[String]): Unit = {
//    var pp = ((p <> expr(0.0)) || (p <> expr(0.0)));
//    var aa = pp(p->0);
//    var bb = pp(p->2);
//    println(aa)
//    println(bb)
//    println(aa.hashCode())
//    println(bb.hashCode())
//    println(aa.getChild(0).hashCode())
//    println(bb.getChild(0).hashCode())
//    if(true){
//      return
//    }


    Maths.Config.setDefaultMatrixInverseStrategy(InverseStrategy.SOLVE)
    Maths.Config.setMaxMemoryThreshold(0)
    var relatErr=new PlotLines
    var count=0
    var E0 = normalize(dSource)
    Plot.asCurve().title("Src").plot(E0)
    while (count<40) {
      PLine+=1;
      //PPatch+=2
      var gp = elist();
      var testX = (cos((2 * p + 1) * π * X / (2 * ((l + l / 1.2)))) * cos(q * π / d * (Y + d / 2))) * dLine;
      var testY = (sin((2 * p + 1) * π * X / (2 * ((l + l / 1.2)))) * sin(q * π / d * (Y + d / 2))) * dLine;
      var linetest = vector(testX, testY)
      var essaiPatchX = (sin(p * π * (X - l) / L) * cos(q * π * (Y + W / 2) / W)) * dPatch;
      var essaiPatchY = (cos(p * π * (X - l) / L) * sin(q * π * (Y + W / 2) / W)) * dPatch;
      var patchtest = vector(essaiPatchX, essaiPatchY)
      gp :+= normalize(linetest.inflate(p.in(0,PLine - 1).cross(q.in(0,PLine - 1))));
      gp :+= normalize(patchtest.inflate(p.in(0,PPatch - 1).cross(q.in(0,PPatch - 1)).where(p<>0 || q<>0)));
      val Eappr = esum(gp.length(), (i: Int) => (E0 ** gp(i)) * gp(i))
      var err = norm(Eappr - E0) / norm(E0)
      relatErr.addValue("Error", gp.length(), err);
      Plot.update("Error").asCurve().title("Relative Error VS Test Functions").plot(relatErr)
      Plot.asCurve().title("E:"+PLine).plot(Eappr)

      count+=1;
    }
  }
}
