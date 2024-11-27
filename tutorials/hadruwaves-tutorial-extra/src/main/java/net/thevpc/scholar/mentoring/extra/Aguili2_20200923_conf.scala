package net.thevpc.scholar.mentoring.extra

import net.thevpc.common.util.{FrequencyFormat, MetricFormat}
import net.thevpc.scholar.hadrumaths.MathScala._
import net.thevpc.scholar.hadrumaths.convergence.ConvergenceSolver
import net.thevpc.scholar.hadrumaths.{Complex, DoubleSolverX, Maths}

object Aguili2_20200923_conf extends App {
  var ε0 = EPS0
  val εr = 1
  //  val εr =  10.2
  //26,5 à 27,5
//  val f0 = 5 * GHZ
  val f0 = 26.5 * GHZ
  val df = 128 * MHZ
  val fs = dstepsFrom(f0, 9, df)
  var π = PI

  // w =
  var w = 1.5 * C / (2 * 10 * GHZ) //1.36 * MM
  var h = w / 2
  var μ0 = U0

  /**
   * the index of the frequency used to calculate valid 'd'
   */
  var freqIndexForReferenceD = 0;
  var datas: Map[Double, Data] = Map();

  //  println(calcData(fs(0)).Capa_TE(0,0));
  //  println(calcData(fs(0)).Capa_TE(0,1));
  //  println(calcData(fs(0)).Capa_TE(0,2));
  //  println(calcData(fs(0)).Capa_TE(0,3));
  //  println(calcData(fs(0)).Capa_TE(0,1000));
  //
  //  println(calcData(fs(0)).Capa_TE(h/2,0));
  //  println(calcData(fs(0)).Capa_TE(h/2,1));
  //  println(calcData(fs(0)).Capa_TE(h/2,2));
  //  println(calcData(fs(0)).Capa_TE(h/2,3));
  //  println(calcData(fs(0)).Capa_TE(h/2,1000));
  //
  //  println(calcData(fs(0)).Capa_TE(h,0));
  //  println(calcData(fs(0)).Capa_TE(h,1));
  //  println(calcData(fs(0)).Capa_TE(h,2));
  //  println(calcData(fs(0)).Capa_TE(h,3));
  //  println(calcData(fs(0)).Capa_TE(h,1000));
  //  println(calcData(fs(0)).)
  plotS_interval_all()
  Maths.Config.setFrequencyFormatter(new FrequencyFormat("HT I2 D3 F"))
  Maths.Config.setMetricFormatter(new MetricFormat("M-6 M3 I2 D4 F"))
  println("")
  println("---------------------------------")
  println("")
  println("w, εr, df, w, f, fi, λ, d, ρ, Ψ, V, ird, Cte, Cvar")
  fs.forEach(x => {
    var dd = calcData(x);
    printf("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s\n",
      formatDimension(w),
      εr, formatFrequency(df), formatDimension(w), formatFrequency(dd.ff), formatFrequency(dd.fr), formatDimension(dd.λ),
      formatDimension(dd.d).replace("u", "μ")
      , dd.ρ, dd.Ψ, dd.V,
      formatDimension(dd.find_ird()),
      dd.Capa_TE(),
      dd.Capa_Vari(),
    )
  })
  println("")
  println("---------------------------------")
  println("")

  def calcData(f: Double): Data = {
    if (datas.contains(f)) {
      return datas(f);
    } else {
      val nv = new Data(f, f_threshold(f)).find_Vd();
      datas = datas ++ Map(f -> nv);
      return nv;
    }
  }

  def plotS_interval_index(index: Int): Unit = {
    //  private val doubles: Array[Double] = dtimes(5*GHZ, 200*GHZ, 1000);//f_intervall(0)
    val doubles: Array[Double] = dtimes(fs(index) - df / 2, fs(index) + df / 2, 10000)
    Plot.title("S-" + formatFrequency(fs(index))).
      xsamples(doubles).xformat("frequency").
      plot(
        doubles.map(ff => calcData(ff).S1),
        doubles.map(ff => calcData(ff).S1)
      )
  }

  def plotS_interval_all(): Unit = {
    //  private val doubles: Array[Double] = dtimes(5*GHZ, 200*GHZ, 1000);//f_intervall(0)
    //  private val doubles: Array[Double] = f_intervall(0)
    val doubles: Array[Double] = dtimes(fs(0) - df / 2, fs(fs.length - 1) + df / 2, 100000)

    Plot.title("S Pow").
      xsamples(doubles).xformat("frequency").plot(doubles.map(ff => (calcData(ff).S1)))

    Plot.title("db(S) Pow").
      xsamples(doubles).xformat("frequency").plot(doubles.map(ff => db(calcData(ff).S1)))

    Plot.title("Q").
      xsamples(doubles).xformat("frequency").plot(doubles.map(ff => calcData(ff).Q()))

    Plot.title("Δf").
      xsamples(doubles).yformat("frequency").xformat("frequency").
      plot(doubles.map(ff => calcData(ff).df0()))
  }


  class Data(f: Double, fref: Double) {

    var ff = f
    /**
     * reference (discrete value of) frequency
     */
    var fr = fref
    var Ψ = 0.0
    var λ = 0.0
    var B = 0.0
    //    var Xm :Complex = complex(0.0)
    var df2 = 0.0
    var V: Double = 0.0
    var Zm = 0.0
    var β = 0.0
    var ω = 0.0
    var F = 0.0;
    var ρ = 0.0;
    var d = 0.0;
    var S = 0.0;
    //    B = π * df / f * (sqr(d / w) + 1)
    //    Ψ = 2 * π * d / λ
    ω = 2 * π * f
    β = sqrt(sqr(ω / C) * εr - sqr(π / w))
    λ = 2 * π / β;
    Zm = ω * μ0 / β

    val Vmin = 1;
    val Vmax = 60;
    val Vtimes = 1000000;

    val ird_times = 1000000;

    var capa = 0.0;

    var S1 = 0.0;
    var ird = -1.0;


    def plotG() {
      val vValues: Array[Double] = dtimes(Vmin, Vmax, Vtimes)
      val fi = fs(0)
      Plot.title("G(V)=0 / f=" + formatFrequency(fi)).yname("G(V)").xsamples(vValues).titles("G(V)").plot(
        vValues.map(v => G0(v))
      )
    }

    def Ze(V: Double): Complex = {
      (-î * V * Zm) / (Zm - î * V);
    }

    def S11(V: Double): Complex = {
      val Ze0 = Ze(V)
      (Ze0 - Zm) / (Ze0 + Zm);
    }

    def Ψ(V: Double): Double = {
      argdbl(S11(V))
    }

    def ρ(V: Double): Double = {
      absdbl(S11(V))
    }

    def d(V: Double): Double = {
      val Ze = this.Ze(V);
      var Ψ2 = /*π +*/ argdbl(Ze);
      if (Ψ2 < 0) {
        Ψ2 = Ψ2 + (2 * π);
      }
      return Ψ2 * λ / (2 * π)
    }

    def df0(): Double = {
      return fr / Q();
    }


    def Q(): Double = {
      if (V == 0) {
        find_Vd();
      }
      (1 + sqr(d / w)) * (sqrt(ρ) * π / (1 - ρ))
    }

    def G0(V: Double): Double = {
      val ρ2 = ρ(V)
      val Ψ2 = /*π +*/ Ψ(V)
      val d2 = Ψ2 * λ / (2 * π)
      val gg = (1 + sqr(d2 / w)) * (sqrt(ρ2) * π / (1 - ρ2)) - f / df
      return gg;
    }

    def Gr(V: Double): Double = {
      val d2 = d
      val ρ2 = ρ(V)
      val gg = (1 + sqr(d2 / w)) * (sqrt(ρ2) * π / (1 - ρ2)) - f / df
      return gg;
    }

    def S(ρ1: Double, Ψ1: Double): Double = {
      (1 - sqr(ρ1)) / (1 + sqr(ρ1) - 2 * ρ1 * cos(2 * Ψ1 - 4 * π / λ * d));
    }

    def find_Vd(): Data = {
      if (f == fs(freqIndexForReferenceD)) {
        V = new DoubleSolverX.Times(G0(_), Vmin, Vmax, Vtimes).solve();
        d = d(V)
        ρ = ρ(V);
        F = sqrt(ρ) * π / (1 - ρ)
        df2 = f / ((1 + sqr(d / w)) * F)
        Ψ = 2 * π * d / λ
        S = S(ρ, Ψ);
        S1 = S;
      } else if (f == fr /*Maths.rerr(f1,f)<1E-5*/ ) {
        V = new DoubleSolverX.Times(Gr(_), Vmin, Vmax, Vtimes).solve();
        d = calcData(fs(freqIndexForReferenceD)).d
        ρ = ρ(V);
        F = sqrt(ρ) * π / (1 - ρ)
        df2 = f / ((1 + sqr(d / w)) * F)
        Ψ = 2 * π * d / λ
        S = S(ρ, Ψ);
        S1 = S;
      } else {
        val dataRef = calcData(fr)
        V = dataRef.V;
        ρ = dataRef.ρ;
        F = dataRef.F;
        df2 = dataRef.df2;
        Ψ = dataRef.Ψ;
        S = dataRef.S;
        d = calcData(fs(freqIndexForReferenceD)).d;
        S1 = S(dataRef.ρ, dataRef.Ψ);
      }
      capa = (1) / (V * ω)
      //      println(this)
      return this;
    }

    def find_ird(): Double = {
      if (ird == -1.0) {
        ird = h / 3;
      }
      return ird;
      //      if (f == fs(fs.length-1)) {
      //        if(ird == -1.0) {
      //          if(V==0){
      //            find_Vd();
      //          }
      //          ird= new DoubleSolverX.Times(x => Capa_TE(x) - capa,0, h, ird_times).solve();
      //        }
      //        return ird;
      //      }else{
      //        return calcData(fs(fs.length-1)).find_ird();
      //      }
    }

    def Capa_Vari(): Double = {
      if (V == 0) {
        find_Vd();
      }
      if (f == fs(fs.length - 1)) {
        return capa - Capa_TE();
      } else if (fr == fref) {
        return capa - Capa_TE();
      } else {
        calcData(fref).Capa_Vari();
      }
    }

    def Capa_TE(): Double = {
      Capa_TE(find_ird());
    }

    def Capa_TE(ird: Double): Double = {
      var best_n = new ConvergenceSolver.Infinite(x => Capa_TE(ird, x), 1, 1E-2).solve();
      //      println("best_n="+best_n+" :: "+ird);
      return Capa_TE(ird, best_n);
    }

    def Capa_TE(ird: Double, nn: Int): Double = {
      val k0 = ω / C;
      var the_sum = 0.0;
      var n = 1;
      var ird2 = h / 2 - ird / 2;
      while (n <= nn) {
        the_sum += (
          (
            sqr(cos(n * π / 2 * ((ird + 2 * ird2) / h)))
              /
              sqrt(sqr(π / w) + sqr(n * π / h) - sqr(k0))
            ) *
            sqr(sincard(n * π / 2 * ird / h))
          );
        n += 1;
        ;
      }
      return 4 * ε0(1 - sqr(π / (k0 * w))) * the_sum;
    }


    //    private def calcCp() : Double = {
    //      var k0=2*π/λ
    //      val cc = 4 * Maths.EPS0 *(1-π/(k0*w));
    //      var n=0;
    //      var vv=0;
    //      var d=h-2
    //      while(n<2000){
    //        vv+=sqr(cos(n*π/2))
    //        n+=1;
    //      }
    //    }


    override def toString: String = {
      return s"Data(Ψ=$Ψ, λ=$λ, B=$B, f=$f, df2=$df2, V=$V, Zm=$Zm, β=$β, ω=$ω, F=$F, ρ=$ρ)"
    }
  }


  private def f_threshold(f: Double) = {
    val ii = fs(0) - df / 2;
    val z = ((f - ii) / df).asInstanceOf[Int]
    if (z >= 0 && z < fs.length) {
      fs(z)
    } else {
      fs(0) + df * z
    }
  }
}
