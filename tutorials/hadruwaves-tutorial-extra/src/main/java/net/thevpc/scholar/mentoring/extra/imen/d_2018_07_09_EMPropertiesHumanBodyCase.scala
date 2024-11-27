//package net.thevpc.scholar.mentoring.extra.imen
//import net.thevpc.common.mon.{ProgressMonitor, ProgressMonitorFactory, ProgressMonitors}
//import net.thevpc.scholar.hadrumaths.MathScala._
//import net.thevpc.scholar.hadrumaths._
//import net.thevpc.scholar.hadrumaths.cache.{CacheEvaluator, PersistenceCache, PersistenceCacheBuilder}
//import net.thevpc.scholar.hadrumaths.util.dump.Dumper
//import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector
//import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Mul
//import net.thevpc.scholar.hadrumaths.util.FastMessageFormat.Evaluator
//import net.thevpc.scholar.hadruwaves.Material
//import net.thevpc.scholar.hadruwaves.mom._
//
//object d_2018_07_09_EMPropertiesHumanBodyCase {
//  Maths.Config.setSimplifierCacheSize(1000000);
//  Maths.Config.setRootCachePath("d_2019_04_25");
//  Maths.Config.setAppCacheName("SAR");
//  val MN=25;
//  val PPatch = 3;
//  val PLine = 3;
//  val N  = 5//7;//  samples of theta; 29 samples
//  val M  = 5//7;//  samples of theta; 29 samples
//  val nbthreads=1
//  //  samples of theta
//  val theta = dtimes( -PI/2, PI/2, 4*M+1)
//  val phi = dtimes(0, 2 * PI, 4*N+1)
//  val r =1.0// (500 * MM)*2;
//  val π = PI;
//  // Parameters
//  val freq = 5.0 * GHZ;
//  val wfreq = 2 * π * freq;
//  val lambda = C / freq;
//  //val epsr = 4.32;
//  val epsr = 49.3 // human body tissue, at 5 GHz
//  val sigma= 5.07 // conductivity of tissue
//  val myMaterial=new Material("myMaterial",epsr,1,sigma)
//  val rho=1000.0 // 1000 kg/m^3 for a cube 1m x 1m x 1m
//
//  val epsilon = EPS0;
//  val mu = U0;
//  val Z0 = sqrt(U0 / EPS0);
//  val k0 = 2 * π / lambda;
//  val a = (100.0 * MM)//*2;
//  val b = (40.0  * MM)//*2;
//  val ep = 1.5 * MM;
//  val dBox = domain(0.0 -> a, -b / 2 -> b / 2);
//  val ap = 60.0 * MM;
//  val bp = 40.0 * MM;
//  val dPlot = domain(0.0 -> ap, -bp -> bp);
//  val d = 2.860 * MM;
//  val l = 18.815 * MM;
//  val L = 13.227 * MM;
//  val W = 26.688 * MM;
//  val dLine = domain(0.0 -> (l + L/1.2), -d / 2 -> d / 2);
//  val dPatch = domain(l -> (l + L), -W / 2 -> W / 2);
//  val s = 2.0 * MM;
//  val dSource = domain(0.0 -> s, -d / 2 -> d / 2);
//
//  val p = param("p");
//  val q = param("q");
//
//  var st: MomStructure = null;
//
//  def main(args: Array[String]): Unit = {
//
//    Maths.Config.setDefaultMatrixInverseStrategy(InverseStrategy.SOLVE)
//    Maths.Config.setScalarProductOperator(ScalarProductOperatorFactory.hardFormal()); // !=quad
//    var cr = chrono()
//    val console = Plot.console()
//    val monitor = console.logger("Total")
//    val dimTimes = 1;
//    //========================================= Test functions ==============================
//    var gp = elist()
//    var testX = ((cos((2 * p + 1) * π * X / (2 * (l + L / 1.2))) * cos(q * π / d * (Y + d / 2))) * dLine)
//    var testY = ((sin((2 * p + 1) * π * X / (2 * (l + L / 1.2))) * sin(q * π / d * (Y + d / 2))) * dLine)
//    var linetest = vector(testX, testY)
//    var essaiPatchX = ((sin(p * π * (X - l) / L) * cos(q * π * (Y + W / 2) / W)) * dPatch);
//    var essaiPatchY = ((cos(p * π * (X - l) / L) * sin(q * π * (Y + W / 2) / W)) * dPatch);
//    var patchtest = vector(essaiPatchX, essaiPatchY)
//    gp :+= normalize(seq(linetest, p, PLine - 1, q, PLine - 1));
//    gp :+= normalize(seq(patchtest, p, PPatch - 1, q, PPatch - 1, (pp:Double, qq:Double) => (pp != 0 || qq != 0)))
////    console.newPlot().title("test functions ").domain(dPlot).plot(gp(0))
////    val gg0=gp(0).toDV.getComponent(Axis.X).asInstanceOf[Mul].getChildren()(1);
//
//    var gg0=gp(0)//.toDV.getComponent(Axis.X).asInstanceOf[Mul].getChildren()(1)
////    println(gg0)
////    println(gg0.getDomain)
////    println(gg0.toDD.evalDouble(gg0.getDomain.x().dtimes(5).xvalues()).mkString("Array(", ", ", ")"))
//
//    println(gg0.simplify())
//    console.newPlot().title("test functions simplified").domain(dPlot).plot(gg0.simplify())
//    console.newPlot().title("test functions").domain(dPlot).plot(gg0)
//
//    //println(" test functions: "+gp)
//    if (true) return ;
////=================================== structure definition ===========================================
//    var E0 = vector(normalize(dSource), 0)
//    // var epsilon=epsr + î*sigma
//    st = MomStructure.EEEE(dBox, freq, MN, BoxSpace.shortCircuit(myMaterial,ep), BoxSpace.matchedLoad())
//    st.setSources(E0, 50)
//    st.setTestFunctions(gp)
//    st.electricField().cartesian().
//
////==================================== total electric field =======================================
//  /*  var Ex = st.electricField().monitor(ProgressMonitorFactory.createLogMonitor(1000))
//      .computeMatrix(Axis.X, dtimes(0, a/2, 200), dtimes(-b/2, b/2, 100), 0)
//    Plot.title(" Electric field Ex(x,y) ").plot(Ex)
//    var Ey = st.electricField().monitor(ProgressMonitorFactory.createLogMonitor(1000))
//      .computeMatrix(Axis.Y, dtimes(0, a/2, 200), dtimes(-b/2, b/2, 100), 0)
//    Plot.title(" Electric field Ey(x,y) ").plot(Ey)
//    var E = (((abs(Ex)) :^ 2) + ((abs(Ey)) :^ 2))
//    // SAR for body tissue
//    var SARXY= (E)*sigma/(2.0*rho)
//    Plot.title(" SAR XY plane ").plot(SARXY)*/
////====================================== Average SAR 1g calculation =======================================
//   // 1g equivalent to a cube of 10mm the side
//    // in our case: subvolume: 35 mm * 30 mm * 0.95 mm = 1000 mm^3 for 1g
////    var Mx=20
////    var My=10
////    var Mz=5
////    var samplesX= dtimes(0, 35*MM, Mx);// 35mm
////    var samplesY= dtimes(-15*MM, 15*MM, My); //30mm
////    var samplesZ= dtimes(-0.95*MM, 0, Mz);
////    var mn=0
////    var SPM = st.getTestModeScalarProducts
////    var Xj = st.matrixX().computeMatrix()
////    var modes = st.getModes
////
////    var zmn: Array[Complex] = st.getModesImpedances
////    val fn: Array[DoubleToVector] = st.fn()
////    var ExC :Expr=0 ;
////    var EyC :Expr=0 ;
////    var EzC :Expr=0 ;
////    while (mn < MN) {
////      // mn ?? m & n parameters ??
////      var m=modes(mn).mode.m
////      var n=modes(mn).mode.n
////      var gammaCC = modes(mn).firstBoxSpaceGamma
////      var fmn = fn(mn)
////      var fmnxPrime=derive(fmn.getComponent(Axis.X),Axis.X)
////      var fmnyPrime=derive(fmn.getComponent(Axis.Y),Axis.Y)
////      val gp_fmn: CVector = SPM.getColumn(mn)
////      ExC= ExC + ((zmn(mn)*(gp_fmn ** Xj)  * fmn.getX))//*exp(-Z*gammaCC))
////      EyC= EyC + ((zmn(mn)*(gp_fmn ** Xj)  * fmn.getY))//*exp(-Z*gammaCC))
////      EzC= EzC + (( (1.0/(î*wfreq*epsilon))*((gp_fmn ** Xj)*
////        (fmnxPrime+fmnyPrime))) )//*exp(-Z*gammaCC))
////      mn += 1;
////      //println("mm="+mm)
////    }
////    var Etotal=sqrt(sqr(ExC) + sqr(EyC))// + sqr(EzC));
////    var Esamples=discrete(Etotal,Samples.absolute(samplesX,samplesY,samplesZ));
////    //val dCube = dBox.cross(-0.95 * MM -> 0.0)
////    //Plot.samples(5,5,5).plot(vector(ExC,EyC,EzC)*dCube)
////    //    if(true){
////    //      return
////    //    }
////    var SAR1g= (1.0/(Mx*My*Mz))*sum(discrete(abssqr(Esamples)*sigma/(2.0*rho)))
////    println("average SAR1g ="+SAR1g);
//
//    //==============================================================================================
//   //=========================================== radiation properties calculation =================
//    var Etheta = zerosMatrix(theta.length,phi.length);
//    var Ephi = zerosMatrix(theta.length,phi.length);
//    var EdBTheta = zerosMatrix(theta.length,phi.length);
//    var EdBPhi = zerosMatrix(theta.length,phi.length);
//    var DirDB = zerosMatrix(theta.length,phi.length);
//    var DirPhiDB = zerosMatrix(theta.length,phi.length);
//    var Dir = zerosMatrix(theta.length,phi.length);
//    println("computing radiated Fields Etheta and Ephi")
//
//    st.matrixX().monitor(ProgressMonitors.createLogMonitor(1000))
//      .evalMatrix()
//    var i = 0;
//    var j = 0;
//    while (i < theta.length) {
//      j = 0
//      while (j < phi.length) {
//        Etheta(i, j)=calculEtetha(theta(i), phi(j))
//        Ephi(i, j)=calculEphi(theta(i), phi(j))
//        j += 1
//      }
//      println(i)
//      i += 1
//    }
//
//    EdBTheta = db(Etheta)
//    EdBPhi   = db(Ephi)
//   // Etheta.store("EthetaFreeSpace.m")
//   // Ephi.store("EphiFreeSpace.m")
//    var Etot=(((abs(Etheta)) :^ 2) + ((abs(Ephi)):^ 2));
//    var Poy = Etot * ((r * r) / (2 * Z0))
//    Poy.store("PoyTissue.m");
//    var PoyAvg = Poy.avg();
//    println("PoyAvg=" + PoyAvg)
//    var Directivity = Poy / PoyAvg;
//    Directivity.store("DirTissue.m");
//    var Eyz = Etot.dotsqrt().getColumn(phi.length/4)//.abs()
//    var Exz = Etot.dotsqrt().getColumn(0)//.abs();
//
//    var PoyXZ = Poy.getColumn(0)//.abs();
//    var PoyYZ = Poy.getColumn(phi.length/4)//.abs();
//    PoyXZ.toMatrix.store("poyTissueXZ.m");
//    PoyYZ.toMatrix.store("poyTissueYZ.m");
//    var DirXZ = Directivity.getColumn(0)//.abs();
//    var DirYZ = Directivity.getColumn(phi.length/4)//.abs();
//    //DirYZ.store("DirYZ.m");
//    //Plot.title("TestModeScalarProducts").plot(st.getTestModeScalarProducts.toMatrix)
//
//    Plot.title("Etheta - phi=PI/2 - E plane").asPolar().xsamples(theta).plot(Eyz)
//    Plot.title("Etheta - phi=0 - H plane ").asPolar().xsamples(theta).plot(Exz)
//    Plot.title("Poy phi=PI/2").asPolar().xsamples(theta).plot(PoyYZ)
//    Plot.title("Poy phi=0").asPolar().xsamples(theta).plot(PoyXZ)
//    Plot.title("Directivity phi=PI/2").asPolar().xsamples(theta).plot(DirYZ)
//    Plot.title("Directivity phi=0").asPolar().xsamples(theta).plot(DirXZ)
//
//    Plot.title("Etheta").plot(Etheta);
//    Plot.title("Ephi").plot(Ephi);
//    Plot.title("Dir").plot(Directivity);
//
//
//  }// end main
//
//
//  // Etheta
//  def calculEtetha(theta: Double, phi: Double): Complex = {
//    var persistentCache = PersistenceCacheBuilder.of().name("Etheta").build();
//    persistentCache.setEnabled(true);
//    val dumper = new Dumper("Etheta")
//      .add("st", st)
//      .add("theta", theta)
//      .add("r", r)
//      .add("phi", phi)
//    //.add("Xvalues", dsteps(0, a, 100))
//    // .add("Yvalues", dsteps(-b / 2, b / 2, 100))
//    return persistentCache.evaluate[Complex]("Etheta", null,new CacheEvaluator {
//      override def evaluate(args: Array[Object] ,cacheMonitor: ProgressMonitor): Object = {
//        return calculEthetaEff(theta, phi);
//      }
//    }, dumper);
//  }
//
//  def calculEthetaEff(theta: Double, phi: Double): Complex = {
//    println("calculating Etheta : phi=" + phi + " , theta=" + theta + " ; " + Thread.currentThread().getName)
//    var C1 = ((-1.0) * î * k0 * exp(-î * k0 * r)) / (4.0 * PI * r)
//    var SPM = st.getTestModeScalarProducts
//    var rp = cos(k0 * sin2(theta) * (X * cos2(phi) + Y * sin2(phi)))
//    var ip = sin(k0 * sin2(theta) * (X * cos2(phi) + Y * sin2(phi)))
//    val rr: Expr = rp + î * ip
//    var mm = 0;
//    var Xj = st.matrixX().evalMatrix()
//    var fx = 0 * î
//    var fy = 0 * î
//    var Jx = 0 * î
//    var Jy = 0 * î
//    val zmns: Array[Complex] = st.getModesImpedances
//    val fn: Array[DoubleToVector] = st.fn()
//    while (mm < MN) {
//      var fmn = fn(mm)
//      val gp_fmn: ComplexVector = SPM.getColumn(mm)
//      var J  = ((gp_fmn ** Xj)  * fmn).toDV;
//      Jx = Jx + integrate(J.getX * rr)
//      Jy = Jy + integrate(J.getY * rr)
//      var Ee = (J * zmns(mm)).toDV
//      fx = fx + integrate(Ee.getY * rr)
//      fy = fy + integrate(Ee.getX * rr)
//      mm += 1;
//      //println("mm="+mm)
//    }
//    fy = fy * (-1.0)
//    return      C1 * ((cos2(phi) * fy - sin2(phi) * fx)+
//      Z0 *(cos2(theta) * ( Jx*cos2(phi) + Jy*sin2(phi) ) ) ); // Etheta
//  }
//
//
//  // E phi
//  def calculEphi(theta: Double, phi: Double): Complex = {
//    var persistentCache = PersistenceCacheBuilder.of().name("Ephi").build();
//    persistentCache.setEnabled(true);
//    var dumper = new Dumper("Ephi")
//      .add("st", st)
//      .add("phi", phi)
//      .add("r", r)
//      .add("theta", theta)
//    //.add("Xvalues", dsteps(0, a, 100))
//    //.add("Yvalues", dsteps(-b / 2, b / 2, 100))
//    return persistentCache.evaluate[Complex]("Ephi", null,new CacheEvaluator {
//      override def evaluate(args: Array[Object],cacheMonitor: ProgressMonitor): Object = {
//        return calculEphiEff(theta, phi);
//      }
//
//    }, dumper);
//  }
//
//
//  def calculEphiEff(theta: Double, phi: Double): Complex = {
//    println("calculating Ephi   : phi=" + phi + " , theta=" + theta + " ; " + Thread.currentThread().getName)
//    var C1 = (î* k0 * exp(-î * k0 * r)) / (4.0 * PI * r) //
//    var SPM = st.getTestModeScalarProducts
//    var rp = cos(k0 * sin2(phi) * (X * cos2(phi) + Y * sin2(phi)))
//    var ip = sin(k0 * sin2(phi) * (X * cos2(phi) + Y * sin2(phi)))
//    val rr: Expr = rp + î * ip
//    var fx = î * 0;
//    var fy = î * 0;
//    var Jx = î * 0;
//    var Jy = î * 0;
//    var mm = 0;
//    var Xj = st.matrixX().evalMatrix()
//
//    val zmns: Array[Complex] = st.getModesImpedances
//    val fn: Array[DoubleToVector] = st.fn()
//    while (mm < MN) {
//      var fmn = fn(mm)
//      val gp_fmn: ComplexVector = SPM.getColumn(mm)
//      // Js**exp()
//      var J = ((gp_fmn ** Xj)  * fmn).toDV;
//      Jx = Jx + integrate(J.getX * rr)
//      Jy = Jy + integrate(J.getY * rr)
//      //Ms**exp()
//      var Ee = (J * zmns(mm)).toDV
//      fx = fx + integrate(Ee.getY * rr)
//      fy = fy + integrate(Ee.getX * rr)
//      mm += 1;
//      //println("mm="+mm)
//    }
//    fy = fy * (-1.0);
//    //fx = fx * (2.0)
//    return C1 * ((cos2(theta) * (cos2(phi) * fx + sin2(phi) * fy))-
//      Z0*(-1.0*Jx*sin2(phi) + Jy*cos2(phi)))// Ephi matrix 2D
//  }
//
//
//
//}
