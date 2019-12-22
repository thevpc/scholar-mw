package net.vpc.scholar


import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadrumaths.scalarproducts.formal.FormalScalarProductOperator
import net.vpc.scholar.hadruplot.Plot


/**
 * Created by imen on 7/15/2017.
 * to compare with matlab code to validate our method
 * patch antenna dea box EEEE without attach function
 */
object PatchComparisonMatlab1_article_nostr_old {

  EnvConfigurer.configure(getClass.getName)

  //  Maths.Config.setRootCachePath("D:\\cache\\d_15_07_2017")
  var N = 20 // number of modes TE or TM

  // Parameters
  var freq = 4.79 * GHZ; // 4.79 * GHZ; // to change
  var wfreq = 2 * PI * freq;
  var lambda = C / freq;
  val epsr = 2.2;
  val epsilon = EPS0
  val mu = U0
  val Z0 = sqrt(U0 / EPS0)
  var k0 = 2 * PI / lambda
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
  var srcexpr = normalize(s * dSource)

  var PPatch = 3
  var PLine = 2
  val TE = 0
  val TM = 1
  var p = param("p")
  var q = param("q")
  var m = param("m")
  var n = param("n")
  var t = param("t")
  var A, B, sp: ComplexMatrix = null;
  var gp, zmn, ymn1, ymn2, fmnx: EList = null;

  def build(plot: Boolean): Boolean = {
    Maths.Config.setCacheEnabled(false)
    var cr = chrono()
    gp = elist();
    var testX = ((cos((2 * p + 1) * PI * X / (2 * (l + l / 1.2))) * cos(q * PI / d * (Y + d / 2))) * dLine).setTitle("gl${p}${q}")
    val essaiPatchX = ((sin(p * PI * (X - l) / L) * cos(q * PI * (Y + W / 2) / W)) * dPatch).setTitle("gp${p}${q}")
    gp :+= testX.inflate(p.in(0, PLine - 1).and(q.in(0, PLine - 1))).normalize(); //.setName("p=" + pp + ";" + "q=" + qq));
    gp :+= essaiPatchX.inflate(p.in(1, PPatch - 1).and(q.in(0, PPatch - 1))).normalize();

    var fmnxdef = If(t === TE).Then(n / b).Else(-m / a) *
      sqrt(2 * If(m <> 0 && n <> 0, 2, 1) / (a * b * (sqr(n * PI / b) + sqr(m * PI / a)))) *
      //      sqrt(2 * If(m <> 0 && n <> 0, 2, 1) / (a * b * (sqr(n / b) + sqr(m / a)))) *
      sin((n * PI / b) * (Y - dBox.ymin)) * cos((m * PI / a) * (X - dBox.xmin)) * dBox
    fmnx = fmnxdef.inflate(m.in(0, N - 1).and(n.in(0, N - 1)).and(t.in(0,1))
        .where((p === TE && (n <> 0)) || (p === TM && (m <> 0 && n <> 0))))!!;
    //    println(fmnx.size())
    //    fmnx.forEach(tt=>{
    //      println((if (TE==tt.getIntProperty("t")) "TE" else "TM") +" "+ tt.getIntProperty("m") +" "+tt.getIntProperty("n") +" "+ tt)
    //    })
    if (plot) {
      Plot.title("gp").plot(gp)
      Plot.title("fmn").plot(fmnx)
    }

    var omega = 2 * PI * freq
    var k0 = 2 * PI * freq / C
    val f0 = fmnx.get(0) !!;
    val g0 = gp(0) !!;
    println(f0);
    println(g0);
    val expr = (f0 ** g0) !!;
    println(expr);

    val f0r = ScalarProductOperatorFactory.formal().asInstanceOf[FormalScalarProductOperator].getExpressionRewriter.rewrite(f0)
    val g0r = ScalarProductOperatorFactory.formal().asInstanceOf[FormalScalarProductOperator].getExpressionRewriter.rewrite(g0)
    println(f0r);
    println(g0r);
    println(g0);
    //    if (true) {
    //      return false;
    //    }
    //    ymn1=exprList()
    zmn = fmnx.transform((i, fmn) => {
      var mm = fmn.getIntProperty("m")
      var nn = fmn.getIntProperty("n")
      var t = fmn.getIntProperty("t")
      var gamma1 = csqrt(sqr(mm * PI / a) + sqr(nn * PI / b) - sqr(k0) * 1)
      var y1 = if (t == TE) (gamma1 / (I * omega * U0)) else (I * omega * (EPS0 * 1) / gamma1);
      var gamma2 = csqrt(sqr(mm * PI / a) + sqr(nn * PI / b) - sqr(k0) * epsr)
      var y2 = if (t == TE) (gamma2 * cotanh(gamma2 * ep) / (I * omega * U0)) else (I * omega * EPS0 * epsr) * cotanh(gamma2 * ep) / gamma2;
      val vzmn = complex(1 / (y1 + y2))
      //      println(vzmn)
      vzmn
    })
    ymn1 = fmnx.transform((i, fmn) => {
      var mm = fmn.getIntProperty("m")
      var nn = fmn.getIntProperty("n")
      var t = fmn.getIntProperty("t")
      var gamma1 = csqrt(sqr(mm * PI / a) + sqr(nn * PI / b) - sqr(k0) * 1)
      var y1 = if (t == TE) (gamma1 / (I * omega * U0)) else (I * omega * (EPS0 * 1) / gamma1);
      var gamma2 = csqrt(sqr(mm * PI / a) + sqr(nn * PI / b) - sqr(k0) * epsr)
      var y2 = if (t == TE) (gamma2 * cotanh(gamma2 * ep) / (I * omega * U0)) else (I * omega * EPS0 * epsr) * cotanh(gamma2 * ep) / gamma2;
      y1
    })
    ymn2 = fmnx.transform((i, fmn) => {
      var mm = fmn.getIntProperty("m")
      var nn = fmn.getIntProperty("n")
      var t = fmn.getIntProperty("t")
      var gamma1 = csqrt(sqr(mm * PI / a) + sqr(nn * PI / b) - sqr(k0) * 1)
      var y1 = if (t == TE) (gamma1 / (I * omega * U0)) else (I * omega * (EPS0 * 1) / gamma1);
      var gamma2 = csqrt(sqr(mm * PI / a) + sqr(nn * PI / b) - sqr(k0) * epsr)
      var y2 = if (t == TE) (gamma2 * cotanh(gamma2 * ep) / (I * omega * U0)) else (I * omega * EPS0 * epsr) * cotanh(gamma2 * ep) / gamma2;
      //      println(vzmn)
      y2
    })
    //    Plot.title("zmn").plot(zmn)
    //Plot.title("test functions ").domain(dPlot).plot(gp)

    sp = matrix(gp :** fmnx)
    A = matrix(gp.size(), (i: Int, j: Int) => {
      csum(fmnx.size, (nn: Int) => {
        sp(i, nn) * sp(j, nn) * complex(zmn(nn))
      })
    });
    B = columnMatrix(gp.size(), (i: Int) => complex(gp(i) ** srcexpr))
    true
  }

  def main(args: Array[String]): Unit = {
    if (!build(true)) return
    Plot.title("sp").plot(sp)
    Plot.title("sp").asMatrix().plot(sp)
    Plot.title("A").asMatrix().plot(A)
    Plot.title("B").asMatrix.plot(B)
    Plot.title("zmn").asTable.plot(zmn)
    isteps(0, zmn.length() - 1).foreach(i => {
      val strfmn = (if (TE == fmnx(i).getIntProperty("t")) "TE" else "TM") + " " + fmnx(i).getIntProperty("m") + " " + fmnx(i).getIntProperty("n")
      var s = (strfmn + " :: zmn=" + zmn(i) + " ymn1=" + ymn1(i) + " ymn2=" + ymn2(i))
      //      println(s)
    })
    if (true) return;
    //    println(zmn)
    println("=========================================")
    //    println(A)
    var JJ = inv(A) * B
    //    println(JJ)
    //    println(complex(inv(tr(B)*inv(A)*B)))
    //    println(JJ)
    //    Plot.domain(dBox).title("J").plot(JJ ** gp)
    //
    var frequencies = 4 * GHZ :: 2.0 / 20 * GHZ :: 6 * GHZ
    var zinlist = elist()
    var Zin = frequencies.toDoubleArray.map(fr0 => {
      freq = fr0
      build(false)
      val zin = complex(inv(tr(B) * inv(A) * B))
      zinlist.add(zin)
      Plot.update("|zin|").asCurve().title("Zin (module)").xsamples(frequencies).asAbs.plot(zinlist)
      Plot.update("real(zin)").asCurve().asReal().title("Zin (real part)").xsamples(frequencies).asReal().plot(zinlist)
      Plot.update("image(zin)").asCurve().asReal().title("Zin (imaginary part)").xsamples(frequencies).asImag().plot(zinlist)
      zin
    })
    println("======== FREQUENCIES ============================")
    frequencies.toDoubleArray().foreach(tt => println(tt))
    println("======== ZIN         ============================")
    zinlist.forEach(r => println(r))

  }

}
