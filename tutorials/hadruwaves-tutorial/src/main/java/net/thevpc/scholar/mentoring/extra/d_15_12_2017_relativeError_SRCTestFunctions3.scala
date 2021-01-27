package net.thevpc.scholar.mentoring.extra
import net.thevpc.scholar.hadrumaths.MathScala._
import net.thevpc.scholar.hadrumaths.transform.ExprRewriteCounter
import net.thevpc.scholar.hadrumaths.{InverseStrategy, Maths, ScalarProductOperatorFactory}
import net.thevpc.scholar.hadruplot.PlotLines
import net.thevpc.scholar.hadruwaves.Physics._
import net.thevpc.scholar.hadruwaves.mom.MomStructure

object d_15_12_2017_relativeError_SRCTestFunctions3 {
  Maths.Config.setCacheEnabled(true);
  Maths.Config.setSimplifierCacheSize(1000000);
  Maths.Config.setScalarProductOperator(ScalarProductOperatorFactory.hardFormal())
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
  var a = (100.567 * MM) //*2;
  var b = (30 * MM) //*2;
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
    Maths.Config.setDefaultMatrixInverseStrategy(InverseStrategy.SOLVE)
    Maths.Config.setMaxMemoryThreshold(0)
    Maths.Config.setCacheEnabled(false)

    //        val e6=((((expr(2)*0)+1)*3.14159265358979*X)/0.0208633333333333);
    //        println(e6.simplify())
    //        if(true) return;

    val rewriteCounter = new ExprRewriteCounter()
    Maths.Config.getScalarProductSimplifier.addRewriteListener(rewriteCounter)
    var relatErr = new PlotLines
    var timePlot = new PlotLines
    var simplificationPlot = new PlotLines
    var count = 0
    var E0 = normalize(dSource)
    //    Plot.asCurve().title("Src").plot(E0)
    //    if(true){
    //      return ;
    //    }
    while (count < 400) {
      val c = chrono()
      rewriteCounter.reset()
      PLine += 1;
      //PPatch+=2
      var gp = elist();
      var testX = (cos((2 * p + 1) * π * X / (2 * ((l + l / 1.2))))) * dLine;
      //      var testY = (sin((2 * p + 1) * π * X / (2 * ((l + l / 1.2)))) * sin(q * π / d * (Y + d / 2))) * dLine;
      //      var linetest = vector(testX, testY)
      var linetest = testX
      var essaiPatchX = (sin(p * π * (X - l) / L) * cos(q * π * (Y + W / 2) / W)) * dPatch;
      //      var essaiPatchY = (cos(p * π * (X - l) / L) * sin(q * π * (Y + W / 2) / W)) * dPatch;
      //      var patchtest = vector(essaiPatchX, essaiPatchY)
      var patchtest = essaiPatchX
      linetest.inflate(p.in(0, PLine - 1)).toString
      gp :+= normalize(linetest.inflate(p.in(0, PLine - 1)));
      gp :+= normalize(patchtest.inflate(p.in(0, PPatch - 1).cross(q.in(0, PPatch - 1)).where(p <> 0 && q <> 0)));

      val Eappr = esum(gp.length(), (i: Int) => (E0 ** gp(i)) * gp(i))
      val Ev = E0 ** gp
      var err = norm(Eappr - E0) / norm(E0)
      println(count + " : " + PLine + " : " + c.stop() + " :: error=" + err)
      relatErr.addValue("Error", gp.length(), err);
      timePlot.addValue("Time", gp.length(), c.getTime / 1000000);
      Plot.update("Error").asCurve().title("Relative Error VS Test Functions").plot(relatErr)
      Plot.update("gp").asCurve().title("gp").asCurve().plot(gp)
      Plot.update("<E0,gp>").asCurve().title("<E0,gp>").plot(Ev)
      Plot.update("E0").asCurve().title("E0") /*.domain(domain(0.0 -> 2*s))*/ .plot(E0)
      Plot.update("E0appr").asCurve().title("E0appr") /*.domain(domain(0.0 -> 2*s))*/ .plot(Eappr)
      //      isteps(0,3).foreach(r=>{
      //        Plot.update("E0appr-"+r).asCurve().title("E0appr-"+r)/*.domain(domain(0.0 -> 2*s))*/.plot(Eappr.getChild(r))
      //        Plot.update("E0appr2-"+r).asCurve().title("E0appr2-"+r)/*.domain(domain(0.0 -> 2*s))*/.plot(Eappr_simp.getChild(r))
      //      })
      //      val e2=50.68156081388659*cos((-0.06554409472939507*X))*domain(0.0->0.010431666666666669,-0.001406->0.001406);
      //      val e2=50.68156081388659*cos((-0.06554409472939507*X))*domain(0.0->100.0,-0.001406->0.001406);
      //      e2.toDD.computeDouble(0.009,0)
      //      Plot.update("e2").asCurve().title("e2").plot(e2)
      //      val e3=(672.6374890636885*domain(0.0->7.86E-4,(-0.001406)->0.001406)**(domain(0.0->0.010431666666666669,(-0.001406)->0.001406)*cos((((2*0)+1)*3.141592653589793*X)/0.020863333333333338)*184.63563530034224))*domain(0.0->0.010431666666666669,(-0.001406)->0.001406)*cos((((2*0)+1)*3.141592653589793*X)/0.020863333333333338)*184.63563530034224
      //      Plot.update("e3").asCurve().title("e3").plot(e3)
      Plot.update("time").asCurve().title("Time").plot(timePlot)

      simplificationPlot.addValue("BestEffort", gp.length(), rewriteCounter.getBestEffortModificationInvocationCount)
      //      simplificationPlot.addValue("Unmodified",gp.length(),rewriteCounter.getUnmodifiedInvocationCount)
      simplificationPlot.addValue("Partial", gp.length(), rewriteCounter.getPartialModificationInvocationCount)
      simplificationPlot.addValue("Unmodified", gp.length(), rewriteCounter.getUnmodifiedInvocationCount)
      Plot.update("SimplificationPlot").asCurve().title("Simplification Counts").asDB().plot(simplificationPlot)
      count += 1;
    }
  }
}
