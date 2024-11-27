package net.thevpc.scholar.mentoring.extra.imen
import net.thevpc.common.mon.{ProgressMonitor, ProgressMonitorFactory, ProgressMonitors}
import net.thevpc.scholar.hadrumaths.MathScala._
import net.thevpc.scholar.hadrumaths._
import net.thevpc.scholar.hadrumaths.cache.{CacheEvaluator, PersistenceCache, PersistenceCacheBuilder}
import net.thevpc.scholar.hadrumaths.util.dump.Dumper
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector
import net.thevpc.scholar.hadrumaths.util.FastMessageFormat.Evaluator
import net.thevpc.scholar.hadruwaves.Material
import net.thevpc.scholar.hadruwaves.mom.BoxSpace.{matchedLoad, shortCircuit}
import net.thevpc.scholar.hadruwaves.mom._

object d_2018_07_10_EMPropertiesFreeSpaceCase {
  Maths.Config.setSimplifierCacheSize(1000000);
  Maths.Config.setRootCachePath("d_2018_07_10")
  Maths.Config.setAppCacheName("fields and radiation")
  var MN = 100
  var PPatch = 5
  var PLine = 3
  var π = PI
  var N  = 5//7;//  samples of theta; 29 samples
  var M  = 5//7;//  samples of theta; 29 samples
  var nbthreads=1
  //  samples of theta
  var theta = dtimes( -PI/2, PI/2, 4*M+1)
  var phi = dtimes(0, 2 * PI, 4*N+1)
  var r =1.0// (500 * MM)*2;

  // Parameters
  var freq = 5.0 * GHZ;
  var wfreq = 2 * π * freq;
  var lambda = C / freq;
  val epsr = 4.32;
  val epsilon = EPS0
  val mu = U0
  val Z0 = sqrt(U0 / EPS0)
  var k0 = 2 * π / lambda
  var a = (100.0 * MM) ;
  var b = (40.0 * MM) ;
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
  var s = 2.0 * MM;
  var dSource = domain(0.0 -> s, -d / 2 -> d / 2)
  var myMaterial=new Material("myMaterial",epsr,1,0)

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
    gp :+= normalize(seq(linetest, p, PLine - 1, q, PLine - 1));
    gp :+= normalize(seq(patchtest, p, PPatch - 1, q, PPatch - 1, (pp:Double, qq:Double) => (pp != 0 || qq != 0)))
    Plot.title("test functions ").domain(dPlot).plot(gp)
    //=================================== structure definition ===========================================
    var E0 = vector(normalize(dSource),0)
    st = MomStructure.EEEE(dBox, freq, MN, shortCircuit(myMaterial, ep), matchedLoad())
    st.setSources(E0, 50)
    st.setTestFunctions(gp)
    //=========================================== radiation properties calculation =================
    var Etheta = zerosMatrix(theta.length,phi.length);
    var Ephi = zerosMatrix(theta.length,phi.length);
    var EdBTheta = zerosMatrix(theta.length,phi.length);
    var EdBPhi = zerosMatrix(theta.length,phi.length);
    var DirDB = zerosMatrix(theta.length,phi.length);
    var DirPhiDB = zerosMatrix(theta.length,phi.length);
    var Dir = zerosMatrix(theta.length,phi.length);
    println("computing radiated Fields Etheta and Ephi")

    st.matrixX().monitor(ProgressMonitors.createLogMonitor(1000))
      .evalMatrix()
    var i = 0;
    var j = 0;
    while (i < theta.length) {
      j = 0
      while (j < phi.length) {
        Etheta(i, j)=calculEtetha(theta(i), phi(j))
        Ephi(i, j)=calculEphi(theta(i), phi(j))
        j += 1
      }
      println(i)
      i += 1
    }

    EdBTheta = db(Etheta)
    EdBPhi   = db(Ephi)
    //Etheta.store("EthetaFreeSpace.m")
    //Ephi.store("EphiFreeSpace.m")
    var Etot=(((abs(Etheta)) :^ 2) + ((abs(Ephi)):^ 2));
    var Poy = Etot * ((r * r) / (2 * Z0))
    Poy.getColumn(0).store("PoyFreeSpaceXZ.m"); // H-plane
    Poy.getColumn((phi.length/4)).store("PoyFreeSpaceYZ.m"); // E-plane
    var PoyAvg = Poy.avg();
    println("PoyAvg=" + PoyAvg)
    var Directivity = Poy / PoyAvg;
    //Directivity.store("DirFreeSpace.m");
    var Eyz = Etot.dotsqrt().getColumn(phi.length/4)//.abs()
    var Exz = Etot.dotsqrt().getColumn(0)//.abs();

    var PoyXZ = Poy.getColumn(0)//.abs();
    var PoyYZ = Poy.getColumn(phi.length/4)//.abs();
    PoyXZ.toMatrix.store("PoyFreeSpaceXZ.m");
    PoyYZ.toMatrix.store("PoyFreeSpaceYZ.m");
    var DirXZ = Directivity.getColumn(0)//.abs();
    var DirYZ = Directivity.getColumn(phi.length/4)//.abs();
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


  }// end main


  // Etheta
  def calculEtetha(theta: Double, phi: Double): Complex = {
    var persistentCache = PersistenceCacheBuilder.of().name("Etheta").build();
    persistentCache.setEnabled(true);
    val dumper = new Dumper("Etheta")
      .add("st", st)
      .add("theta", theta)
      .add("r", r)
      .add("phi", phi)
    //.add("Xvalues", dsteps(0, a, 100))
    // .add("Yvalues", dsteps(-b / 2, b / 2, 100))
    return persistentCache.evaluate[Complex]("Etheta", null,new CacheEvaluator {
      override def evaluate(args: Array[Object],cacheMonitor: ProgressMonitor): Object = {
        return calculEthetaEff(theta, phi);
      }
    }, dumper);
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
    val zmns: Array[Complex] = st.getModesImpedances
    val fn: Array[DoubleToVector] = st.fn()
    while (mm < MN) {
      var fmn = fn(mm)
      val gp_fmn: ComplexVector = SPM.getColumn(mm)
      var J  = ((gp_fmn ** Xj)  * fmn).toDV;
      Jx = Jx + integrate(J.getX * rr)
      Jy = Jy + integrate(J.getY * rr)
      var Ee = (J * zmns(mm)).toDV
      fx = fx + integrate(Ee.getY * rr)
      fy = fy + integrate(Ee.getX * rr)
      mm += 1;
      //println("mm="+mm)
    }
    fy = fy * (-1.0)
    return      C1 * ((cos2(phi) * fy - sin2(phi) * fx)+
      Z0 *(cos2(theta) * ( Jx*cos2(phi) + Jy*sin2(phi) ) ) ); // Etheta
  }


  // E phi
  def calculEphi(theta: Double, phi: Double): Complex = {
    var persistentCache = PersistenceCacheBuilder.of().name("Ephi").build();
    persistentCache.setEnabled(true);
    var dumper = new Dumper("Ephi")
      .add("st", st)
      .add("phi", phi)
      .add("r", r)
      .add("theta", theta)
    //.add("Xvalues", dsteps(0, a, 100))
    //.add("Yvalues", dsteps(-b / 2, b / 2, 100))
    return persistentCache.evaluate[Complex]("Ephi", null,new CacheEvaluator {
      override def evaluate(args: Array[Object],cacheMonitor: ProgressMonitor): Object = {
        return calculEphiEff(theta, phi);
      }

    }, dumper);
  }


  def calculEphiEff(theta: Double, phi: Double): Complex = {
    println("caculating Ephi   : phi=" + phi + " , theta=" + theta + " ; " + Thread.currentThread().getName)
    var C1 = (î* k0 * exp(-î * k0 * r)) / (4.0 * PI * r) //
    var SPM = st.getTestModeScalarProducts
    var rp = cos(k0 * sin2(phi) * (X * cos2(phi) + Y * sin2(phi)))
    var ip = sin(k0 * sin2(phi) * (X * cos2(phi) + Y * sin2(phi)))
    val rr: Expr = rp + î * ip
    var fx = î * 0;
    var fy = î * 0;
    var Jx = î * 0;
    var Jy = î * 0;
    var mm = 0;
    var Xj = st.matrixX().evalMatrix()

    val zmns: Array[Complex] = st.getModesImpedances
    val fn: Array[DoubleToVector] = st.fn()
    while (mm < MN) {
      var fmn = fn(mm)
      val gp_fmn: ComplexVector = SPM.getColumn(mm)
      // Js**exp()
      var J = ((gp_fmn ** Xj)  * fmn).toDV;
      Jx = Jx + integrate(J.getX * rr)
      Jy = Jy + integrate(J.getY * rr)
      //Ms**exp()
      var Ee = (J * zmns(mm)).toDV
      fx = fx + integrate(Ee.getY * rr)
      fy = fy + integrate(Ee.getX * rr)
      mm += 1;
      //println("mm="+mm)
    }
    fy = fy * (-1.0);
    //fx = fx * (2.0)
    return C1 * ((cos2(theta) * (cos2(phi) * fx + sin2(phi) * fy))-
      Z0*(-1.0*Jx*sin2(phi) + Jy*cos2(phi)))// Ephi matrix 2D
  }




}
