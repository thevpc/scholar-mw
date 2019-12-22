package net.vpc.scholar

import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadruwaves.mom.BoxSpaceFactory._
import net.vpc.scholar.hadruwaves.mom._


/**
  * Created by imen on 7/15/2017.
  * to compare with matlab code to validate our method
  * patch antenna dea box EEEE without attach function
  */
object d_2017_07_26_PatchDEA1_ParamVariation_verifV2 {
  EnvConfigurer.configure(getClass.getName)

  //  Maths.Config.setAppCacheName("Essai_6_8_no_compress")
  //  Maths.Config.setCompressCache(false)
  //  Maths.Config.setRootCachePath("/run/media/vpc/KHADHRAOUII/.cache/mathcache")
  //  Maths.Config.setDefaultMatrixFactory(OjalgoMatrixFactory.INSTANCE)
  var MN = 10000
  //000 // number of modes
  var π = PI
  // Parameters
  var freq = 4.79 * GHZ; // to change
  var wfreq = 2 * π * freq;
  var lambda = C / freq;
  val epsr = 2.2;
  val epsilon = EPS0
  val mu = U0
  val Z0 = sqrt(U0 / EPS0)
  var k0 = 2 * π / lambda
  var r = (500 * MM) * 2;
  var a = (100.567 * MM);
  var b = (30 * MM);
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
    Config.setDefaultMatrixInverseStrategy(InverseStrategy.SOLVE)
    Config.setMaxMemoryThreshold(0);
    Config.setCacheEnabled(false);
    //isteps(0,10).foreach(i=>println(i+" : "+((5+i)*(5+i-1)+(3+i)*(3+i))));
    //    if(true) System.exit(0)
    val m1 = prepareStructure(1, 4.203125E9)
    //val m2 = prepareStructure(1,5.703125E9)
    //Plot.title("m1").plot(m2)
  }

  def prepareStructure(PIncrement: Int, fr0: Double): ComplexVector = {
    PPatch = 5 + PIncrement
    PLine = 3 + PIncrement
    var gp = elist()
    var testX = ((cos((2 * p + 1) * π * X / (2 * (l + l / 1.2))) * cos(q * π / d * (Y + d / 2))) * dLine)
    val essaiPatchX = ((sin(p * π * (X - l) / L) * cos(q * π * (Y + W / 2) / W)) * dPatch);
    gp :+= normalize(testX.inflate(p.in(0,PLine - 1).and(q.in(0, PLine - 1))));
    gp :+= normalize(essaiPatchX.inflate(p.in(1, PPatch - 1).and(q.in(0,PPatch - 1))));
    var count = 0
    val dimX = a * 3.25 //dtimes(a, a * 10, 5)
    val dimY = b * 3.25d // dtimes(b, b * 10, 5)

    val dBox = domain(0.0 -> dimX, -dimY / 2 -> dimY / 2)
    val E0 = normalize(dSource)
    st = MomStructure.EEEE(dBox, freq, MN, shortCircuit(epsr, ep), matchedLoad(1.0))
    st.getHintsManager.setHintAxisType(HintAxisType.X_ONLY);
    st.setSources(E0, 50)
    st.setTestFunctions(gp)
    st.setFrequency(fr0);
    val tst = vector((sin((828.1878700148839 * X) + (-4.71238898038469)) * cos((1049.1209395858382 * Y) + 3.1415926535897927) * (171.30368691960348 * domain(0.005690000000000001 -> 0.028450000000000003, -0.0029945 -> 0.0029945))), 0);
    val fnn = st.getModeFunctions.fn()
    val ps = columnVector(fnn.length, (i: Int) => complex(tst ** fnn(i)))
    Plot.title(String.valueOf(fr0)).plot(ps.transpose())
    ps;
  }
}
