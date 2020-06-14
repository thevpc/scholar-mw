package net.vpc.scholar

//package thesis.projects.thesisprojects

import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadruplot.Plot


object PatchAntennaNoStr {

  // ****************
  // Constants
  // ****************
  val N = 10 // number of modes TE or TM
  val P = 3  // number of test functions
  val epsr = 2.2;
  val epsilon = EPS0
  val mu = U0
  val r = (500 * MM) * 2
  val a = 100.567 * MM
  val b = 30 * MM
  val ep = 1.59 * MM
  val ap = 50 * MM
  val bp = 5 * MM
  val d = 2.812 * MM
  val l = 5.69 * MM
  val L = 22.760 * MM
  val W = 5.989 * MM
  val s = 0.786 * MM
  val Z0 = sqrt(U0 / EPS0)
  val box = domain(0.0 -> a, -b / 2 -> b / 2)
  val lineDomain = domain(0.0 -> l, -d / 2 -> d / 2)
  val srcDomain = domain(0.0 -> s, -d / 2 -> d / 2)
  val srcexpr = normalize(s * srcDomain)

  // ****************
  // Build Constants
  // ****************
  val TE = 0
  val TM = 1
  val p = param("p")
  val t = param("t")
  val m = param("m")
  val n = param("n")

  // ****************
  //  Parameters
  // ****************
  var freq = 4.79 * GHZ; // 4.79 * GHZ; // to change
  //dynamically evaluated params (functions)
  def lambda = C / freq
  def omega = 2 * PI * freq
  def k0 = 2 * PI / lambda


  // ****************
  // Build Parameters
  // ****************

  // Matrix A (Z operator), B (Source Projection)n sp (gp fn scalar product matrix)
  var A, B, sp: Matrix = null;
  // gp (test functions list), zmn (modes impedance list), fm (mode functions : Green Function)
  var gp, fmn: EList = null;
  var zmn : Vector =null

  //Unknown coefficients of Surface Current Density
  var Xp = zerosMatrix(1)
  var Zin = 0*î

  // Surface Current Density projected over Test functions
  var Jt = 0*ê
  // Surface Current Density projected over Mode functions
  var Jm = 0*ê
  // Surface Electric Field projected over Mode functions
  var Em = 0*ê


  /**
    * build function to evaluate unknown according to parameters
    */
  def build() {
    Maths.Config.setCacheEnabled(false)
    var cr = chrono()
// test functions

    var gpDef = ((cos((2 * p + 1) * PI * X / (2 * l)))  * lineDomain).setTitle("gl${p}")
    gp = seq(gpDef, p, 0,P - 1).map((i, e) => e.normalize()); //.setName("p=" + pp + ";" + "q=" + qq));

    var fmnDef = If(t === TE, n / b, -m / a) *
      sqrt(2 * If(m <> 0 && n <> 0, 2, 1) / (a * b * (sqr(n * PI / b) + sqr(m * PI / a)))) *
      sin((n * PI / b) * (Y - box.ymin)) * cos((m * PI / a) * (X - box.xmin)) * box
    fmn = seq(fmnDef, m, N - 1, n, N - 1, t, 1, (mm, nn, pp) => (pp == TE &&
      (nn != 0)) || (pp == TM && (mm != 0 && nn != 0))) !!;

    zmn = columnVector(fmn.size, (i:Int)=>{
      var mm = fmn(i).getIntProperty("m")
      var nn = fmn(i).getIntProperty("n")
      var t = fmn(i).getIntProperty("t")
      var gammalomn = csqrt(sqr(mm * PI / a) + sqr(nn * PI / b) - sqr(k0) * 1)
      var ylomn = if (t == TE) (gammalomn / (I * omega * U0)) else (I * omega * (EPS0 * 1) / gammalomn);
      var gammaccmn = csqrt(sqr(mm * PI / a) + sqr(nn * PI / b) - sqr(k0) * epsr)
      var yccmn = if (t == TE) (gammaccmn * cotanh(gammaccmn * ep) / (I * omega * U0)) else (I * omega * EPS0 * epsr) * cotanh(gammaccmn * ep) / gammaccmn;
      val vzmn = complex(1 / (ylomn + yccmn))
      vzmn
    })

    sp = matrix(gp :** fmn)

    A = matrix(gp.size, (i: Int, j: Int) => {
      csum(fmn.size, (nn: Int) => sp(i, nn) * sp(j, nn) * zmn(nn))
    })

    B = columnMatrix(gp.size, (i: Int) => complex(gp(i) ** srcexpr))

    Xp = inv(A) * B
    Zin =complex(inv(tr(B) * inv(A) * B))
    // discrete scalar product between two vectors is the sum of each element's product
    Jt= Xp ** gp ;
    Jm=elist(Xp :** sp.columns) ** fmn
    Em=elist(Xp :** sp.columns) ** (elist(zmn) :* fmn)
  }

  /**
    * main function, wil call build
    * @param args
    */
  def main(args: Array[String]): Unit = {
    build()
    Plot.title("sp").asMatrix.plot(sp)
    Plot.title("A").plot(A)
    Plot.title("B").asMatrix.plot(B)
    Plot.title("zmn").asTable.plot(zmn)
    Plot.title("gp").domain(box).asCurve().plot(gp)
    Plot.title("Xp").asMatrix.plot(Xp)
    Plot.title("Je").domain(box).asAbs.plot(Jt)
    Plot.title("Jm").domain(box).asAbs.plot(Jm)
    Plot.title("Em").domain(box).asAbs.plot(Em)
    var frequencies = 1 * GHZ :: 1.0 / 10 * GHZ :: 10 * GHZ
    var zinlist = elist()
    frequencies.foreach(fr0 => {
      freq = fr0
      build()
      zinlist.add(Zin)
      Plot.update("|zin|").asCurve.title("Zin (module)").xsamples(frequencies).asAbs.plot(zinlist)
      Plot.update("real(zin)").asCurve.asReal.title("Zin (real part)").xsamples(frequencies).asReal().plot(zinlist)
      Plot.update("image(zin)").asCurve.asReal.title("Zin (imaginary part)").xsamples(frequencies).asImag().plot(zinlist)
    })
  }

}
