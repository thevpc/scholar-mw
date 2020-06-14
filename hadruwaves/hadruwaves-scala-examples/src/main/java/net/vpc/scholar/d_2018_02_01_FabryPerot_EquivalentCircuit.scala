package net.vpc.scholar

import net.vpc.common.tson.Tson
import net.vpc.common.util.Chronometer
import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector
import net.vpc.scholar.hadruwaves.Material
import net.vpc.scholar.hadruwaves.mom.BoxSpace._
import net.vpc.scholar.hadruwaves.mom.{StrLayer, _}
import net.vpc.scholar.hadruwaves.project.scene.HWProjectScenePlot

object d_2018_02_01_FabryPerot_EquivalentCircuit {
  Maths.Config.setSimplifierCacheSize(1000000);
  Maths.Config.setCacheEnabled(true);
  var nbthreads = 1
  var MN = 3000
  var PLine = 4 //9;
  var PPatch = 4 //12;
  var M = 10 //  samples of theta
  var N = 10 // samples of phi
//  var impedance = (-150.0) * î
  var impedance = complex(4.319673582398524E-15 - 157.89750378255098 * î) //50.0

  var p = param("p")
  var q = param("q")
  var theta = dtimes(-PI / 2, PI / 2, M)
  var phi = dtimes(0, 2 * PI, N)

  // Parameters
  var freq = 5.0 * GHZ; // to change
  var wfreq = 2 * PI * freq;
  var lambda = C / freq;
  val epsilon = EPS0
  // 8.854 * 1e-12;
  val mu = U0
  //4 * PI * 1e-7
  val Z0 = sqrt(U0 / EPS0)
  var k0 = 2 * PI / lambda
  var r = (500 * MM) * 2; // observation point (1m)

  // Box
  var a = (360.0 * MM);
  var b = (240.0 * MM);
  var ep = 2.5 * MM;
  var dBox = domain(0.0, a, -b / 2, b / 2)
  //Line
  var d = 3.439 * MM;
  var l = 8.995 * MM;
  // Patch
  var L = 17.401 * MM;
  var W = 29.256 * MM;
  var dLine = domain(0.0, (l + l / 1.2), -d / 2, d / 2)
  var dPatch = domain(l, (l + L), -W / 2, W / 2)
  // Feed
  var s = 2.5 * MM;
  // value from DEA
  var dSource = domain(0.0 -> s, -d / 2 -> d / 2)
  // Norms
  var NSource = 1.0 / sqrt(s * d);
  val NLigne = sqrt(2.0 / (l * d));
  val NPatch = sqrt(2.0 / (L * W));

  var st: MomStructure = null;
  val console = Plot.console().setWindowTitle("version : " + Maths.getHadrumathsVersion).setGlobal()

  def main(args: Array[String]): Unit = {
    Maths.Config.setScalarProductOperator(
      ScalarProductOperatorFactory.hardFormal()); // !=quad
    var cr = Chronometer.start


    //======================================== Test Functions =====================================
    var gp = elist()
    var testX = ((cos((2 * p + 1) * PI * X / (2 * (l + l / 1.2))) * cos(q * PI / d * (Y + d / 2))) * dLine) !!;
    var testY = ((sin((2 * p + 1) * PI * X / (2 * (l + l / 1.2))) * sin(q * PI / d * (Y + d / 2))) * dLine) !!;
    var linetest = vector(testX, testY) //.normalize()
    var essaiPatchX = ((sin(p * PI * (X - l) / L) * cos(q * PI * (Y + W / 2) / W)) * dPatch) !!;
    var essaiPatchY = ((cos(p * PI * (X - l) / L) * sin(q * PI * (Y + W / 2) / W)) * dPatch) !!;
    var patchtest = vector(essaiPatchX, essaiPatchY) //.normalize()
    gp :+= linetest.title("Line$p$q").inflate(p.in(0, PLine - 1).and(q.in(0, PLine - 1)));
    gp :+= patchtest.title("Test$p$q").inflate(p.in(0, PPatch - 1).and(q.in(0, PPatch - 1)).where((p <> 0 || q <> 0)));
    Plot.title("test functions ").domain(dBox).plot(gp)
    //if (true) return;
    //===================================== structure definition ==================================
    var E0 = vector(normalize(dSource), 0)
    dBox = domain(0.0 -> a, -b / 2 -> b / 2)
    st = MomStructure.EEEE(dBox, freq, MN, shortCircuit(Material.substrate("substrate",2.2), ep), matchedLoad(Material.VACUUM)) //!!!!!!! nothing())
    st.setSources(E0, 50)
    st.setTestFunctions(gp)
    st.setMonitorFactory(console)
    //
    //faut tester....
    st.setLayers(new StrLayer(
      lambda / 2, // position of the layer z-axis: cavity thickness
      impedance // value of surface impedance
    ))

    Plot.title("Scene").plot(new HWProjectScenePlot(st.createScene(),null))
    if(true){
      return
    }
    //   st.toString;
    //================================================================================================
    //=========================================== radiation properties calculation =================
    var Etheta = zerosMatrix(theta.length, phi.length);
    var Ephi = zerosMatrix(theta.length, phi.length);
    var EdBTheta = zerosMatrix(theta.length, phi.length);
    var EdBPhi = zerosMatrix(theta.length, phi.length);
    var DirDB = zerosMatrix(theta.length, phi.length);
    var DirPhiDB = zerosMatrix(theta.length, phi.length);
    var Dir = zerosMatrix(theta.length, phi.length);
    println("computing radiated Fields Etheta and Ephi")

    Plot.title("SP").plot(st.testModeScalarProducts());
    st.matrixX() //.monitor(ProgressMonitors.createLogMonitor(1000))
      .evalMatrix()

    Plot.title("A").plot(st.matrixA().evalMatrix());
    Plot.title("B").plot(st.matrixB().evalMatrix());
    Plot.title("X").plot(st.matrixX().evalMatrix());
    val EthtaPhi = st.electricField().spherical().evalMatrix(theta, phi, 1)
    Plot.title("Etheta").plot(EthtaPhi(Axis.THETA));
    Plot.title("Ephi").plot(EthtaPhi(Axis.PHI));
    Plot.title("Etotal").plot(st.electricField().spherical().evalModuleMatrix(theta,phi, 1));
    Plot.title("E E-Plane").plot(st.electricField().spherical().evalEPlaneModuleVector(theta,1));
    Plot.title("E H-Plane").plot(st.electricField().spherical().evalHPlaneModuleVector(theta,1));
    Plot.title("Poynting").plot(st.poyntingVector().spherical().evalModuleMatrix(theta,phi, 1));
    Plot.title("Poynting E-Plane").plot(st.poyntingVector().spherical().evalEPlaneModuleVector(theta,1));
    Plot.title("Poynting H-Plane").plot(st.poyntingVector().spherical().evalHPlaneModuleVector(theta,1));
    Plot.title("Directivity").plot(st.directivity().spherical().evalMatrix(theta,phi, 1));
    Plot.title("Directivity E-Plane").plot(st.directivity().spherical().evalEPlaneVector(theta,1));
    Plot.title("Directivity H-Plane").plot(st.directivity().spherical().evalHPlaneVector(theta,1));


    var i = 0;
    var j = 0;
    var t=0L;
    var maxt=1L*theta.length*phi.length;
    while (i < theta.length) {
      j = 0
      while (j < phi.length) {
        val ccc = Chronometer.start()
        Etheta(i, j) = calculEtetha(theta(i), phi(j))
        Ephi(i, j) = calculEphi(theta(i), phi(j))
        j += 1
        t+=1;
        println((i,j)+"/"+(theta.length,phi.length)+" (global "+t+"/"+maxt+") (done in "+ccc.stop()+")")
      }
      println(i)
      i += 1
    }
    Plot.title("Etheta2").plot(Etheta)
    Plot.title("Ephi2").plot(Ephi)

    EdBTheta = db(Etheta)
    EdBPhi = db(Ephi)
    //Etheta.store("EthetaFreeSpace.m")
    //Ephi.store("EphiFreeSpace.m")
    var Etot = (((abs(Etheta)) :^ 2) + ((abs(Ephi)) :^ 2));
    var Poy = Etot * ((r * r) / (2 * Z0))
    Poy.getColumn(0).store("PoyFreeSpaceXZ.m"); // H-plane
    Poy.getColumn((phi.length / 4)).store("PoyFreeSpaceYZ.m"); // E-plane
    var PoyAvg = Poy.avg();
    println("PoyAvg=" + PoyAvg)
    var Directivity = Poy / PoyAvg;
    //Directivity.store("DirFreeSpace.m");
    var Eyz = Etot.dotsqrt().getColumn(phi.length / 4) //.abs()
    var Exz = Etot.dotsqrt().getColumn(0) //.abs();

    var PoyXZ = Poy.getColumn(0) //.abs();
    var PoyYZ = Poy.getColumn(phi.length / 4) //.abs();
    PoyXZ.toMatrix.store("PoyFreeSpaceXZ.m");
    PoyYZ.toMatrix.store("PoyFreeSpaceYZ.m");
    var DirXZ = Directivity.getColumn(0) //.abs();
    var DirYZ = Directivity.getColumn(phi.length / 4) //.abs();
    //DirYZ.store("DirYZ.m");
    //Plot.title("TestModeScalarProducts").plot(st.getTestModeScalarProducts.toMatrix)

    Plot.title("Etheta - phi=PI/2 - E plane").asPolar().xsamples(theta).plot(Eyz)
    Plot.title("Etheta - phi=0 - H plane ").asPolar().xsamples(theta).plot(Exz)
    Plot.title("Poy phi=PI/2").asPolar().xsamples(theta).plot(PoyYZ)
    Plot.title("Poy phi=0").asPolar().xsamples(theta).plot(PoyXZ)
    Plot.title("Directivity phi=PI/2").asPolar().xsamples(theta).plot(DirYZ)
    Plot.title("Directivity phi=0").asPolar().xsamples(theta).plot(DirXZ)

    Plot.title("Etheta").plot(Etheta);
    Plot.title("Ephi").plot(Ephi);
    Plot.title("Dir").plot(Directivity);


  } // end main


  // Etheta
  def calculEtetha(theta: Double, phi: Double): Complex = {
    var persistentCache = st.getDerivedPersistentCache("Etheta")
//    persistentCache.setEnabled(true);
    val dumper = Tson.obj("Etheta")
      .add("st", Tson.serializer().serialize(st))
      .add("theta", Tson.elem(theta))
      .add("r", Tson.elem(r))
      .add("phi", Tson.elem(phi))
    //.add("Xvalues", dsteps(0, a, 100))
    // .add("Yvalues", dsteps(-b / 2, b / 2, 100))
    return persistentCache.of("Etheta", dumper).eval(() => calculEthetaEff(theta, phi)).get();
  }

  def calculEthetaEff(theta: Double, phi: Double): Complex = {
    println("caculating Etheta : phi=" + phi + " , theta=" + theta + " ; " + Thread.currentThread().getName)
    var C1 = ((-1.0) * î * k0 * exp(-î * k0 * r)) / (4.0 * PI * r)
    var SPM = st.getTestModeScalarProducts
    var rp = cos(k0 * sin2(theta) * (X * cos2(phi) + Y * sin2(phi)))
    var ip = sin(k0 * sin2(theta) * (X * cos2(phi) + Y * sin2(phi)))
    val rr: Expr = rp + î * ip
    var mm = 0;
    var Xj = st.matrixX().evalMatrix()
    var fx = 0 * î
    var fy = 0 * î
    var Jx = 0 * î
    var Jy = 0 * î
    val zmns: Array[Complex] = st.modesImpedances
    val fn: Array[DoubleToVector] = st.fn()
    while (mm < MN) {
      var fmn = fn(mm)
      val gp_fmn = SPM.column(mm)
      var J = (gp_fmn ** Xj) * fmn;
      Jx = Jx + integrate(J.toDV.X * rr)
      Jy = Jy + integrate(J.toDV.Y * rr)
      var Ee = J * zmns(mm)
      fx = fx + integrate(Ee.toDV.Y * rr)
      fy = fy + integrate(Ee.toDV.X * rr)
      mm += 1;
      //println("mm="+mm)
    }
    fy = fy * (-1.0)
    return complex(C1 * ((cos(phi) * fy - sin(phi) * fx) +
      Z0 * (cos(theta) * (Jx * cos(phi) + Jy * sin(phi))))); // Etheta
  }


  // E phi
  def calculEphi(theta: Double, phi: Double): Complex = {
    var persistentCache = st.getDerivedPersistentCache("Ephi")
//    var persistentCache = new PersistenceCacheImpl("Ephi");
//    persistentCache.setEnabled(true);
    var dumper = Tson.obj("Ephi")
      .add("st", Tson.serializer().serialize(st))
      .add("phi", Tson.elem(phi))
      .add("r", Tson.elem(r))
      .add("theta", Tson.elem(theta))
    //.add("Xvalues", dsteps(0, a, 100))
    //.add("Yvalues", dsteps(-b / 2, b / 2, 100))
    //    return persistentCache.evaluate[Complex]("Ephi", null,new CacheEvaluator {
    //      override def evaluate(args: Array[Object]): Object = {
    //        return calculEphiEff(theta, phi);
    //      }
    //
    //    }, dumper);
    return persistentCache.of("Ephi", dumper).eval(() => calculEphiEff(theta, phi)).get();
  }


  def calculEphiEff(theta: Double, phi: Double): Complex = {
    println("caculating Ephi   : phi=" + phi + " , theta=" + theta + " ; " + Thread.currentThread().getName)
    var C1 = (î * k0 * exp(-î * k0 * r)) / (4.0 * PI * r) //
    var SPM = st.testModeScalarProducts
    var rp = cos(k0 * sin2(phi) * (X * cos(phi) + Y * sin(phi)))
    var ip = sin(k0 * sin2(phi) * (X * cos(phi) + Y * sin(phi)))
    val rr: Expr = rp + î * ip
    var fx = î * 0;
    var fy = î * 0;
    var Jx = î * 0;
    var Jy = î * 0;
    var mm = 0;
    var Xj = st.matrixX().evalMatrix()

    val zmns: Array[Complex] = st.modesImpedances
    val fn: Array[DoubleToVector] = st.fn()
    while (mm < MN) {
      var fmn = fn(mm)
      val gp_fmn = SPM.column(mm)
      // Js**exp()
      var J = (gp_fmn ** Xj) * fmn;
      Jx = Jx + integrate(J.toDV.X * rr)
      Jy = Jy + integrate(J.toDV.Y * rr)
      //Ms**exp()
      var Ee = J * zmns(mm)
      fx = fx + integrate(Ee.toDV.Y * rr)
      fy = fy + integrate(Ee.toDV.X * rr)
      mm += 1;
      //println("mm="+mm)
    }
    fy = fy * (-1.0);
    //fx = fx * (2.0)
    return (C1 * ((cos2(theta) * (cos2(phi) * fx + sin2(phi) * fy)) -
      Z0 * (-1.0 * Jx * sin2(phi) + Jy * cos2(phi)))) // Ephi matrix 2D
  }


}
