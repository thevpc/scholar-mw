package net.vpc.scholar

import java.io.{File, PrintStream}

import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadrumaths.cache.ObjectCache
import net.vpc.scholar.hadrumaths.scalarproducts.MemComplexScalarProductCache
import net.vpc.scholar.hadrumaths.util.IOUtils
import net.vpc.scholar.hadruwaves.mom.BoxSpaceFactory._
import net.vpc.scholar.hadruwaves.mom.{HintAxisType, MomStructure}
import net.vpc.scholar.hadruwaves.mom.modes.SimpleModeIteratorFactory


/**
  * Created by imen on 7/15/2017.
  * to compare with matlab code to validate our method
  * patch antenna dea box EEEE without attach function
  */
object d_2017_07_26_PatchDEA1_ParamVariation_verif {
  Maths.Config.setSimplifierCacheSize(1000000);
  Maths.Config.setAppCacheName("3.1.2")
  var MN = 200000 // number of modes
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
    //    println(Maths.Config.getCacheFolder())
    //    if(true){
    //      return;
    //    }
    var cr = chrono()
    val console = Plot.console("v3.1.2", new File("."))
    val dimTimes = 1;
    var frequencies = (dtimes(4.0 * GHZ, 6.0 * GHZ, 2));
    val refineEssai = isteps(0, 2)
    val refineFrequencies = isteps(0, 2)

    refineFrequencies.foreach(refineIndex => {
      println("============ Raffinement " + (refineIndex) + "  => " + frequencies)
      var zinEssaiList = new java.util.ArrayList[TList[Expr]]();
      var zinModeList = new java.util.ArrayList[TList[Expr]]();
      var zinTitles = new java.util.ArrayList[String]();
      refineEssai.foreach(PIncrement => {
        PPatch = 5 + PIncrement
        PLine = 3 + PIncrement

        var gp = elist()
        var testX = ((cos((2 * p + 1) * π * X / (2 * (l + l / 1.2))) * cos(q * π / d * (Y + d / 2))) * dLine)
        val essaiPatchX = ((sin(p * π * (X - l) / L) * cos(q * π * (Y + W / 2) / W)) * dPatch);
        gp :+= normalize(seq(testX, p, PLine - 1, q, PLine - 1));
        gp :+= normalize(seq(essaiPatchX, p, PPatch - 1, q, PPatch - 1, (pp, qq) => pp != 0))
        gp = simplify(gp)
        //        Plot.title("test functions " + PIncrement).domain(dPlot).plot(gp)
        // src dimensions variation:
        var count = 0
        var dimX = dtimes(a * 3.25, a * 3.25, dimTimes) //dtimes(a, a * 10, 5)
        var dimY = dtimes(b * 3.25, b * 3.25, dimTimes) // dtimes(b, b * 10, 5)
        //    val coeff = dtimes(1, 10, 5)
        //    println(dlist(dtimes(1, 10, 5)))

        var dBox = domain(0.0 -> a, -b / 2 -> b / 2)

        while (count < dimX.length) {

          dBox = domain(0.0 -> dimX(count), -dimY(count) / 2 -> dimY(count) / 2)
          var E0 = normalize(expr(dSource))
          st = MomStructure.EEEE(dBox, freq, MN, shortCircuit(epsr, ep), matchedLoad(1.0))
          st.getHintsManager.setHintAxisType(HintAxisType.X_ONLY);
          st.getModeFunctions.setModeInfoComparator(null)
          st.getModeFunctions.setModeIteratorFactory(new SimpleModeIteratorFactory)
          st.setSources(E0, 50)
          st.setTestFunctions(gp)
          var fmn = st.getModeFunctions.list()

          //          val azerty = fmn.sublist(0, 100).toArray
          ////          val zzz1 =new MemDoubleScalarProductCache(true).evaluate(Maths.Config.getDefaultScalarProductOperator, azerty, azerty, false, AxisXY.XY, monitor);
          //          val zzz2 =new MemDoubleScalarProductCache(false).evaluate(Maths.Config.getDefaultScalarProductOperator, azerty, azerty, false, AxisXY.XY, monitor);
          //          val zzz4 =new MemComplexScalarProductCache(false,true,false).evaluate(Maths.Config.getDefaultScalarProductOperator, azerty, azerty, false, AxisXY.XY, monitor);
          //          Plot.title("2").plot(zzz2)
          //          Plot.title("4").plot(zzz4)
          //          if(true){
          //            return;
          //          }
          //          println(st.getModeFunctions.get(0));
          var zEssai = elist()
          var zMode = elist()
          var sp = st.getTestModeScalarProducts()
          //        Plot.title("sp:"+a+","+b+","+freq).asAbs().plot(st.getTestModeScalarProducts);
          var ss = columnVector(fmn.length(), (i: Int) => complex(E0 ** fmn(i).getComponent(Axis.X)))
          var sg = columnVector(gp.length(), (i: Int) => complex(E0 ** gp(i)))
          zinEssaiList.add(zEssai)
          zinModeList.add(zMode)
          zinTitles.add(PLine + "x" + PPatch)
          frequencies.foreach((fr0) => {
            val zz = st.setFrequency(fr0).inputImpedance().computeComplex()
            println(">>>>>>   Zin[fr=" + fr0 + ",ai=" + count + ",g=" + PIncrement + ", box=" + dBox + "]=" + zz + "  ;; " + st.getCurrentCache(false).getFolder)
            println(st.getCurrentCache(false))
            val objectpath = "/structure.dump/3h/81/5z"
            val c2 = "/home/vpc/.cache/mathcache/3.1.2/" + objectpath + "/test-mode-scalar-products.cacheobj";
            //            val cc1 = ObjectCache.loadObject(new java.io.File("/").get(c1),null)
            val cc2 = ObjectCache.loadObject(IOUtils.createHFile(c2), null).asInstanceOf[MemComplexScalarProductCache].toMatrix
            cc2.store(new File("/home/vpc/cmp/sp.v321.m"))
            println("bye")
            //            Plot.asMatrix().plot(cc2)
            var Xp = st.matrixX().computeMatrix().toVector
            val AA = st.matrixA().computeMatrix()
            AA.store("/home/vpc/cmp/A.v321.m");
            AA.inv().store("/home/vpc/cmp/Ainv.v321.m");
            st.matrixB().computeMatrix().store("/home/vpc/cmp/B.v321.m");
            st.inputImpedance().computeMatrix().store("/home/vpc/cmp/Zin.v321.m");
            zMode :+= 1.0 / (csum(gp.length(), (pp: Int) => Xp(pp) * csum(fmn.length(), (mm: Int) => (ss(mm) * sp(pp, mm)))));
            st.matrixX().computeMatrix().store("/home/vpc/cmp/X.v321.m");
            val dumFile = new PrintStream(new File("/home/vpc/cmp/dump.v321.m"))
            dumFile.println(st.dump());
            dumFile.close()

            zEssai :+= zz
            if (true) return;
            //            println("dim "+a+" :: gcout"+PIncrement)
            //            println("\t"+zinEssaiList)
            //            println("\t"+frequencies)
            Plot.update("zin Essai :" + count).asCurve().title("zin Essai :" + count).titles(zinTitles).xsamples(frequencies).asReal().plot(zinEssaiList)
            Plot.update("zin Mode  :" + count).asCurve().title("zin Mode  :" + count).titles(zinTitles).xsamples(frequencies).asReal().plot(zinModeList)
          })

          //        Plot.update("list zin "+anyP).title("list zin").asAbs().asCurve().xsamples(frequencies).plot(zinList)
          //        Plot.update("list s11 "+anyP).title("list s11").asAbs().asCurve().xsamples(frequencies).plot(s11List)

          count += 1;

        } // end while


      })
    })


  }

}
