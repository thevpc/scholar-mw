//
//import net.vpc.scholar.hadrumaths.*;
//import net.vpc.scholar.hadrumaths.symbolic.DoubleParam;
//import static net.vpc.scholar.hadrumaths.Maths.*;
//import static net.vpc.scholar.hadrumaths.util.StringUtils.*;
//
//public class PatchComparisonMatlab1_article_nostr {
//
//    //  Maths.Config.setRootCachePath("D:\\cache\\d_15_07_2017")
//    static int N = 300;// number of modes TE or TM
//    // Parameters
//    static double freq = 4.79 * GHZ;
//    static double wfreq = 2 * PI * freq;
//    ;// 4.79 * GHZ; // to change
//    static double lambda = C / freq;
//    static double epsr = 2.2;
//    static double epsilon = EPS0;
//    static double mu = U0;
//    static double Z0 = sqrt(U0 / EPS0);
//    static double k0 = 2 * PI / lambda;
//    static double r = (500 * MM) * 2;
//    static double a = (100.567 * MM);
//    static double b = (30 * MM);
//    static double ep = 1.59 * MM;
//    static Domain dBox = Domain.forBounds(0, a, -b / 2, b / 2);
//    static double ap = 50 * MM;
//    static double bp = 5 * MM;
//    static Domain dPlot = Domain.forBounds(0, ap, -bp, bp);
//    static double d = 2.812 * MM;
//    static double l = 5.69 * MM;
//    static double L = 22.760 * MM;
//    static double W = 5.989 * MM;
//    static Domain dLine = Domain.forBounds(0, (l + l / 1.2), -d / 2, d / 2);
//    static Domain dPatch = Domain.forBounds(l, (l + L), -W / 2, W / 2);
//    static double s = 0.786 * MM;
//    static Domain dSource = Domain.forBounds(0, s, -d / 2, d / 2);
//    static Expr srcexpr = normalize(mul(expr(s), expr(dSource)));
//    static int PPatch = 3;
//    static int PLine = 2;
//    static double TE = 0;
//    static double TM = 1;
//    static DoubleParam p = param("p");
//    static DoubleParam q = param("q");
//    static DoubleParam m = param("m");
//    static DoubleParam n = param("n");
//    static DoubleParam t = param("t");
//    static Matrix Xp = null;
//    static Matrix A = null, B = null;
//    static TMatrix<Expr> sp = null;
//    static TList<Expr> gp, zmn, ymn1, ymn2, fmnx = null;
//
//    static {
//        Maths.Config.setSimplifierCacheSize(1000000);
//    }
//
//    static void perf1() {
//        Expr E1 = chrono(formatLeft("E1 (:** ** (,))", 20), () -> (vscalarProduct(Xp.toVector().to($EXPR), sp.columns())).scalarProductAll(false, zmn, fmnx));
////        Plot.domain(dBox).title("E1 (:** ** (,))").plot(E1);
//    }
//
//    static void perf2() {
//        Expr E2 = chrono(formatLeft("E2 ((:**) **(,))", 20), () -> {
//            TVector<Expr> exprs1 = (vscalarProduct(Xp.toVector().to($EXPR), sp.columns()));
//            TVector<Expr> exprs = columnEVector(exprs1.size(), (i) -> exprs1.get(i).toComplex());
//            return exprs.scalarProductAll(false, zmn, fmnx);
//        });
////        Plot.domain(dBox).title("E2 ((:**) **(,))").plot(E2);
//    }
//
//    static void perf3() {
//        Expr E3 = chrono(formatLeft("E3 (:** ** (:*))", 20), () -> (vscalarProduct(Xp.toVector().to($EXPR), sp.columns())).scalarProduct(false, zmn.dotmul(fmnx)));
////        Plot.domain(dBox).title("E3 (:** ** (:*))").plot(E3);
//    }
//
//    static void perf4() {
//        Expr E4 = chrono(formatLeft("E4 (sum)", 20), () -> esum(fmnx.length(), (i) -> {
//            return mul(esum(gp.length(), (j) -> {
//                return mul(sp.get(j, i), Xp.get(j));
//            }), zmn.get(i), fmnx.get(i));
//        }));
////        Plot.domain(dBox).title("E4 (sum)").plot(E4);
//    }
//
//    static void perf5() {
//        Expr E5 = chrono(formatLeft("E5 (loop)", 20), () -> {
//            Expr E50 = EZERO;
//            int ii = 0;
//            int jj = 0;
//            while (ii < fmnx.length()) {
//                jj = 0;
//                Complex kk = CZERO;
//                while (jj < gp.length()) {
//                    kk = kk.add(complex(complex(sp.get(jj, ii)).mul(complex(Xp.get(jj)))));
//                    jj += 1;
//                }
//                E50 = add(E50, mul(zmn.get(ii), fmnx.get(ii), kk));
//                ii += 1;
//            }
//            return E50;
//        });
////        Plot.domain(dBox).title("E5 (loop)").plot(E5);
//    }
//
//    static boolean build(boolean plot) {
//        Maths.Config.setCacheEnabled(false);
//        gp = chrono(formatLeft("gp", 20), () -> {
//            TList<Expr> gp0 = elist();
//            Expr testX = (mul(cos(div(mul((add(mul(p, 2), 1)), expr(PI), X), ((2 * (l + l / 1.2))))), cos(mul(q, expr(PI / d), add(Y, d / 2))), expr(dLine))).setTitle("gl${p}${q}");
//            Expr essaiPatchX = (mul(sin(mul(p, expr(PI / L), add(X, -l))), cos(mul(q, expr(PI / W), div(add(Y, W), 2))), expr(dPatch))).setTitle("gp${p}${q}");
//            gp0.appendAll(seq(testX, p, PLine - 1, q, PLine - 1).eval((index, e) -> e.normalize())); //.setName("p=" + pp + ";" + "q=" + qq));
//            gp0.appendAll(seq(essaiPatchX, p, PPatch - 1, q, PPatch - 1, (pp, qq) -> pp != 0).eval((index, e) -> e.normalize()));
//            return gp0;
//        });
//
//        fmnx = chrono(formatLeft("fmnx", 20), () -> {
//            Expr fmnxdef = mul(
//                    If(eq(t, expr(TE)), div(n, b), div(m, -a)),
//                    sqrt(div(mul(expr(2), If(and(ne(m, expr(0)), ne(n, expr(0))), expr(2), expr(1))), mul(expr(a * b), add(sqr(mul(n, (PI / b))), sqr(mul(m, (PI / a))))))),
//                    sin(mul(n, expr(PI / b), sub(Y, dBox.ymin()))),
//                    cos(mul(m, expr(PI / a), sub(X, dBox.xmin()))),
//                    expr(dBox)
//            );
//
//            return seq(fmnxdef, m, N - 1, n, N - 1, t, 1, (mm, nn, pp) -> (pp == TE && (nn != 0)) || (pp == TM && (mm != 0 && nn != 0)));
//        });
//
////        fmnx.forEach(tt=>{
////          println((if (TE==tt.getIntProperty("t")) "TE" else "TM") +" "+ tt.getIntProperty("m") +" "+tt.getIntProperty("n") +" "+ tt)
////          println((if (TE==tt.getIntProperty("t")) "TE" else "TM") +" "+ tt.getIntProperty("m") +" "+tt.getIntProperty("n") +" "+ " ==> "+ (tt!!))
////        })
//        if (plot) {
//            Plot.title("gp").plot(gp);
//            Plot.title("fmn").plot(fmnx);
//        }
//
//        double omega = 2 * PI * freq;
//        double k0 = 2 * PI * freq / C;
//
////    ymn1=elist()
//        zmn = chrono(formatLeft("zmn", 20), () -> copyOf(fmnx.eval((i, fmn) -> {
//            int mm = fmn.getIntProperty("m");
//            int nn = fmn.getIntProperty("n");
//            int t = fmn.getIntProperty("t");
//            Expr gamma1 = csqrt(sqr(mm * PI / a) + sqr(nn * PI / b) - sqr(k0) * 1);
//            Expr y1 = (t == TE) ? div(gamma1, mul(I, omega * U0)) : div(mul(I, omega * (EPS0 * 1)), gamma1);
//            Expr gamma2 = csqrt(sqr(mm * PI / a) + sqr(nn * PI / b) - sqr(k0) * epsr);
//            Expr y2 = (t == TE) ? div(mul(gamma2, cotanh(mul(gamma2, ep))), (mul(I, omega * U0))) : mul(I, expr(omega * EPS0 * epsr), div(cotanh(mul(gamma2, ep)), gamma2));
//            Expr vzmn = complex(inv(add(y1, y2)));
////      println(vzmn)
//            return vzmn;
//        })));
////    ymn1 = fmnx.map((i, fmn) => {
////      var mm = fmn.getIntProperty("m")
////      var nn = fmn.getIntProperty("n")
////      var t = fmn.getIntProperty("t")
////      var gamma1 = csqrt(sqr(mm * PI / a) + sqr(nn * PI / b) - sqr(k0)*1)
////      var y1 = if (t == TE) (gamma1 / (I * omega * U0)) else (I * omega * (EPS0 * 1) / gamma1);
////      var gamma2 = csqrt(sqr(mm * PI / a) + sqr(nn * PI / b) - sqr(k0)*epsr)
////      var y2 = if (t == TE) (gamma2 * cotanh(gamma2 * ep) / (I * omega * U0)) else (I * omega * EPS0 * epsr) * cotanh(gamma2 * ep) / gamma2;
////      y1
////    })
////    ymn2 = fmnx.map((i, fmn) => {
////      var mm = fmn.getIntProperty("m")
////      var nn = fmn.getIntProperty("n")
////      var t = fmn.getIntProperty("t")
////      var gamma1 = csqrt(sqr(mm * PI / a) + sqr(nn * PI / b) - sqr(k0)*1)
////      var y1 = if (t == TE) (gamma1 / (I * omega * U0)) else (I * omega * (EPS0 * 1) / gamma1);
////      var gamma2 = csqrt(sqr(mm * PI / a) + sqr(nn * PI / b) - sqr(k0)*epsr)
////      var y2 = if (t == TE) (gamma2 * cotanh(gamma2 * ep) / (I * omega * U0)) else (I * omega * EPS0 * epsr) * cotanh(gamma2 * ep) / gamma2;
////      //      println(vzmn)
////      y2
////    })
//        //    Plot.title("zmn").plot(zmn)
//        //Plot.title("test functions ").domain(dPlot).plot(gp)
//
//        sp = chrono(formatLeft("sp", 20), () -> ematrix(loadOrEvalMatrix("~/sp" + N + ".m", () -> matrix(scalarProductMatrix(true, gp, fmnx)))));
//        A = chrono(formatLeft("A", 20), () -> loadOrEvalMatrix("~/A" + N + ".m", () -> matrix(gp.size(), (i, j) -> {
//            return complex(esum(fmnx.size(), (nn) -> mul(sp.get(i, nn), sp.get(j, nn), zmn.get(nn))));
//        })));
//        B = chrono(formatLeft("B", 20), () -> columnMatrix(gp.size(), (i) -> complex(scalarProduct(true, gp.get(i), srcexpr))));
//        Xp = chrono(formatLeft("Xp", 20), () -> (inv(A).mul(B)));
//        for (int i = 0; i < 3; i++) {
//            System.out.println("================ Iteration " + (i + 1));
//            perf1();
//            perf2();
//            perf3();
//            perf4();
//            //perf5();
//        }
////
////
////
////
////
////        Plot.domain(dBox).title("E5").plot(E5);
////
////        Plot.title("sp").asMatrix().plot(sp);
////        Plot.title("A").asMatrix().plot(A);
////        Plot.title("B").asMatrix().plot(B);
////        Plot.title("zmn").asTable().plot(zmn);
//        if (true) {
////            System.exit(0);
//        }
//        return false;
//    }
//
//    public static void main(String[] args) {
//        System.out.println(Double.compare(1,-Double.NaN));
//        if(true){
//            return;
//        }
//        Plot.Config.setDefaultWindowTitle("NoSTR3.1.3");
//        if (!build(false)) {
//            return;
//        }
//        ilist(isteps(0, zmn.length() - 1)).forEachIndex((i, item) -> {
//            String strfmn = ((TE == fmnx.get(i).getIntProperty("t")) ? "TE" : "TM") + " " + fmnx.get(i).getIntProperty("m") + " " + fmnx.get(i).getIntProperty("n");
//            String s = (strfmn + " :: zmn=" + zmn.get(i) + " ymn1=" + ymn1.get(i) + " ymn2=" + ymn2.get(i));
//            System.out.println(s);
//        });
//
//        if (true) {
//            return;
//        }
////    println(zmn)
//        System.out.println("=========================================");
////    println(A)
////    println(JJ)
////    println(complex(inv(tr(B)*inv(A)*B)))
//        //    println(JJ)
////    Plot.domain(dBox).title("J").plot(JJ ** gp)
////
//        TList<Double> frequencies = dlist(dsteps(4 * GHZ, 6 * GHZ, 2.0 / 20 * GHZ));
//        TList<Expr> zinlist = elist();
//        TList<Complex> Zin = frequencies.transform($COMPLEX, (index, fr0) -> {
//            freq = fr0;
//            build(false);
//            Complex zin = complex(inv(tr(B).mul(inv(A)).mul(B)));
//            zinlist.append(zin);
//            Plot.update("|zin|").asCurve().title("Zin (module)").xsamples(frequencies).asAbs().plot(zinlist);
//            Plot.update("real(zin)").asCurve().asReal().title("Zin (real part)").xsamples(frequencies).asReal().plot(zinlist);
//            Plot.update("image(zin)").asCurve().asReal().title("Zin (imaginary part)").xsamples(frequencies).asImag().plot(zinlist);
//            return zin;
//        });
//        System.out.println("======== FREQUENCIES ============================");
//        frequencies.forEachIndex((i, tt) -> System.out.println(tt));
//        System.out.println("======== ZIN         ============================");
//        zinlist.forEach((r) -> System.out.println(r));
//
//    }
//
//}
///*
// Maths.Config.setSimplifierCacheSize(1000000);
//  //  Maths.Config.setRootCachePath("D:\\cache\\d_15_07_2017")
//  var N = 100 // number of modes TE or TM
//
//  // Parameters
//  var freq = 4.79 * GHZ; // 4.79 * GHZ; // to change
//  var wfreq = 2 * PI * freq;
//  var lambda = C / freq;
//  val epsr = 2.2;
//  val epsilon = EPS0
//  val mu = U0
//  val Z0 = sqrt(U0 / EPS0)
//  var k0 = 2 * PI / lambda
//  var r = (500 * MM) * 2;
//  var a = (100.567 * MM);
//  var b = (30 * MM);
//  var ep = 1.59 * MM;
//  var dBox = domain(0.0 -> a, -b / 2 -> b / 2)
//  var ap = 50 * MM
//  var bp = 5 * MM
//  var dPlot = domain(0.0 -> ap, -bp -> bp)
//  var d = 2.812 * MM;
//  var l = 5.69 * MM;
//  var L = 22.760 * MM;
//  var W = 5.989 * MM;
//  var dLine = domain(0.0 -> (l + l / 1.2), -d / 2 -> d / 2)
//  var dPatch = domain(l -> (l + L), -W / 2 -> W / 2)
//  var s = 0.786 * MM;
//  var dSource = domain(0.0 -> s, -d / 2 -> d / 2)
//  var srcexpr = normalize(s * dSource)
//
//  var PPatch = 3
//  var PLine = 2
//  val TE = 0
//  val TM = 1
//  var p = param("p")
//  var q = param("q")
//  var m = param("m")
//  var n = param("n")
//  var t = param("t")
//  var Xp : Matrix=null;
//  var A, B : Matrix = null;
//  var sp: EMatrix = null;
//  var gp, zmn, ymn1, ymn2, fmnx : EList = null;
//
//  def build(plot:Boolean): Boolean = {
//    Maths.Config.setCacheEnabled(false)
//    var cr = new Chronometer
////    var tt=cos(((1 * 3.141592653589793) / 0.10056699999999999) * (X - 0));
////    var tt=cos(3.141592653589793 * X);
////    println((tt.simplify()))
////    if(true){
////      return false;
////    }
//    gp = elist();
//    var testX = ((cos((2 * p + 1) * PI * X / (2 * (l + l / 1.2))) * cos(q * PI / d * (Y + d / 2))) * dLine).setName("gl${p}${q}")
//    val essaiPatchX = ((sin(p * PI * (X - l) / L) * cos(q * PI * (Y + W / 2) / W)) * dPatch).setName("gp${p}${q}")
//    gp :+= seq(testX, p, PLine - 1, q, PLine - 1).map((i, e) => e.normalize()); //.setName("p=" + pp + ";" + "q=" + qq));
//    gp :+= seq(essaiPatchX, p, PPatch - 1, q, PPatch - 1, (pp, qq) => pp != 0).map((i, e) => e.normalize());
//
//    var fmnxdef = If(t === TE, n / b, -m / a) *
//      sqrt(2 * If(m <> 0 && n <> 0, 2, 1) / (a * b * (sqr(n * PI / b) + sqr(m * PI / a)))) *
////      sqrt(2 * If(m <> 0 && n <> 0, 2, 1) / (a * b * (sqr(n / b) + sqr(m / a)))) *
//      sin((n * PI / b) * (Y - dBox.ymin)) * cos((m * PI / a) * (X - dBox.xmin)) * dBox
//    fmnx = seq(fmnxdef, m, N - 1, n, N - 1, t, 1, (mm, nn, pp) => (pp == TE && (nn != 0)) || (pp == TM && (mm != 0 && nn != 0)));
//        println(fmnx.size())
////        fmnx.forEach(tt=>{
////          println((if (TE==tt.getIntProperty("t")) "TE" else "TM") +" "+ tt.getIntProperty("m") +" "+tt.getIntProperty("n") +" "+ tt)
////          println((if (TE==tt.getIntProperty("t")) "TE" else "TM") +" "+ tt.getIntProperty("m") +" "+tt.getIntProperty("n") +" "+ " ==> "+ (tt!!))
////        })
//    if(plot) {
//          Plot.title("gp").plot(gp)
//          Plot.title("fmn").plot(fmnx)
//    }
//
//    var omega = 2 * PI * freq
//    var k0 = 2 * PI * freq / C
//
////    ymn1=elist()
//    println("Zmn?");
//    zmn = fmnx.map((i, fmn) => {
//      var mm = fmn.getIntProperty("m")
//      var nn = fmn.getIntProperty("n")
//      var t = fmn.getIntProperty("t")
//      var gamma1 = csqrt(sqr(mm * PI / a) + sqr(nn * PI / b) - sqr(k0)*1)
//      var y1 = if (t == TE) (gamma1 / (I * omega * U0)) else (I * omega * (EPS0 * 1) / gamma1);
//      var gamma2 = csqrt(sqr(mm * PI / a) + sqr(nn * PI / b) - sqr(k0)*epsr)
//      var y2 = if (t == TE) (gamma2 * cotanh(gamma2 * ep) / (I * omega * U0)) else (I * omega * EPS0 * epsr) * cotanh(gamma2 * ep) / gamma2;
//      val vzmn = complex(1 / (y1 + y2))
////      println(vzmn)
//      vzmn
//    })
////    ymn1 = fmnx.map((i, fmn) => {
////      var mm = fmn.getIntProperty("m")
////      var nn = fmn.getIntProperty("n")
////      var t = fmn.getIntProperty("t")
////      var gamma1 = csqrt(sqr(mm * PI / a) + sqr(nn * PI / b) - sqr(k0)*1)
////      var y1 = if (t == TE) (gamma1 / (I * omega * U0)) else (I * omega * (EPS0 * 1) / gamma1);
////      var gamma2 = csqrt(sqr(mm * PI / a) + sqr(nn * PI / b) - sqr(k0)*epsr)
////      var y2 = if (t == TE) (gamma2 * cotanh(gamma2 * ep) / (I * omega * U0)) else (I * omega * EPS0 * epsr) * cotanh(gamma2 * ep) / gamma2;
////      y1
////    })
////    ymn2 = fmnx.map((i, fmn) => {
////      var mm = fmn.getIntProperty("m")
////      var nn = fmn.getIntProperty("n")
////      var t = fmn.getIntProperty("t")
////      var gamma1 = csqrt(sqr(mm * PI / a) + sqr(nn * PI / b) - sqr(k0)*1)
////      var y1 = if (t == TE) (gamma1 / (I * omega * U0)) else (I * omega * (EPS0 * 1) / gamma1);
////      var gamma2 = csqrt(sqr(mm * PI / a) + sqr(nn * PI / b) - sqr(k0)*epsr)
////      var y2 = if (t == TE) (gamma2 * cotanh(gamma2 * ep) / (I * omega * U0)) else (I * omega * EPS0 * epsr) * cotanh(gamma2 * ep) / gamma2;
////      //      println(vzmn)
////      y2
////    })
//    //    Plot.title("zmn").plot(zmn)
//    //Plot.title("test functions ").domain(dPlot).plot(gp)
//
//    println("gp :** fmnx ?");
//    sp=ematrix(loadOrEvalMatrix("sp"+N+".m",()=>matrix(gp :** fmnx)))
//    println("A ?");
//    A = matrix(gp.size(), (i: Int, j: Int) => {
//      complex(sum(fmnx.size, (nn: Int) => sp(i, nn) * sp(j, nn) * zmn(nn)))
//    });
//    println("B ?");
//    B = columnMatrix(gp.size(), (i: Int) => complex(gp(i) ** srcexpr))
//    println("Xp ?");
//    Xp = (inv(A) * B)
//
//    println("E4?");
//    var cr4=chrono()
//    var E4 = sum(fmnx.length(),(i:Int) =>{sum(gp.length(),(j:Int)=>{sp(j,i)*Xp(j)}) *zmn(i) *fmnx(i)})
//    println("E4 : sum(fmnx.length(),(i:Int) =>{sum(gp.length(),(j:Int)=>{sp(j,i)*Xp(j)}) *zmn(i) *fmnx(i)}) : " + cr4.stop);
//
////    println("J?");
////    var crj=chrono()
////    var J = (Xp :** sp.columns()) ** fmnx;
////    println("J : (Xp :** sp.columns()) ** fmn " + crj.stop);
//
//    println("E1?");
//    var cr1=chrono()
//    var E1 = (Xp :** sp.columns()) ** (zmn,fmnx)
//    println("E1 : (Xp :** sp.columns()) ** (zmn,fmnx) : " + cr1.stop);
//
//    println("E2?");
//    var cr2=chrono()
//    val exprs1: EVector = Xp :** sp.columns()
//    val exprs = Maths.columnEVector(exprs1.size(), (i:Int)=>exprs1(i).toComplex)
//    var E2 = exprs ** (zmn,fmnx)
//    println("E2 : (Xp :** sp.columns()) ** (zmn,fmnx) : " + cr2.stop);
//
//    println("E3?");
//    var cr3=chrono()
//    var E3 = (Xp :** sp.columns()) ** ( zmn :* fmnx)
//    println("E3 : (Xp :** sp.columns()) ** ( zmn :* fmnx) : " + cr3.stop);
//
//
//    println("E5?");
//    var cr5=chrono()
//    var E5 = 0*ê
//    var ii=0;
//    var jj=0;
//    while(ii<fmnx.length()) {
//      jj=0;
//      var kk=0*î
//      while (jj < gp.length()) {
//        kk+=complex(sp(jj,ii)*Xp(jj));
//        jj+=1;
//      }
//      E5+=zmn(ii) *fmnx(ii) * kk;
//      ii+=1;
//    }
//    println("E5 : while : " + cr5.stop);
//
//    Plot.title("sp").plot(sp)
//    Plot.title("sp").asMatrix().plot(sp)
//    Plot.title("A").asMatrix().plot(A)
//    Plot.title("B").asMatrix.plot(B)
//    Plot.title("zmn").asTable.plot(zmn)
//    Plot.domain(dBox).title("E").plot(E1)
//    Plot.domain(dBox).title("E").plot(E2)
//    Plot.domain(dBox).title("E2").plot(E3)
//    Plot.domain(dBox).title("E3").plot(E4)
//    Plot.domain(dBox).title("E5").plot(E5)
//    false
//  }
//
//  def main(args: Array[String]): Unit = {
//    Plot.Config.setDefaultWindowTitle("NoSTR3.1.3")
//    if (!build(true)) return
//    isteps(0, zmn.length()-1).foreach(i => {
//      val strfmn = (if (TE == fmnx(i).getIntProperty("t")) "TE" else "TM") + " " + fmnx(i).getIntProperty("m") + " " + fmnx(i).getIntProperty("n")
//      var s=(strfmn+" :: zmn=" + zmn(i)+ " ymn1="+ ymn1(i)+ " ymn2="+ ymn2(i))
////      println(s)
//    })
//    if(true)return;
////    println(zmn)
//    println("=========================================")
////    println(A)
////    println(JJ)
////    println(complex(inv(tr(B)*inv(A)*B)))
//    //    println(JJ)
////    Plot.domain(dBox).title("J").plot(JJ ** gp)
////
//        var frequencies = 4 * GHZ :: 2.0/20 * GHZ :: 6 * GHZ
//        var zinlist=elist()
//        var Zin = frequencies.toDoubleArray.map(fr0 => {
//          freq=fr0
//          build(false)
//          val zin = complex(inv(tr(B)*inv(A)*B))
//          zinlist.append(zin)
//          Plot.update("|zin|").asCurve().title("Zin (module)").xsamples(frequencies).asAbs.plot(zinlist)
//          Plot.update("real(zin)").asCurve().asReal().title("Zin (real part)").xsamples(frequencies).asReal().plot(zinlist)
//          Plot.update("image(zin)").asCurve().asReal().title("Zin (imaginary part)").xsamples(frequencies).asImag().plot(zinlist)
//          zin
//        })
//    println("======== FREQUENCIES ============================")
//    frequencies.toDoubleArray().foreach(tt=>println(tt))
//    println("======== ZIN         ============================")
//    zinlist.forEach(r=>println(r))
//
//  }
//
//
// */
