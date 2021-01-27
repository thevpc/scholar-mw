package net.thevpc.scholar.mentoring.extra
import net.thevpc.common.util.{FrequencyFormat, MetricFormat}
import net.thevpc.scholar.hadrumaths.MathScala._
import net.thevpc.scholar.hadruwaves.Physics._
import net.thevpc.scholar.hadrumaths.{Complex, ConvergenceSolver, DoubleSolverX, Maths}
import _root_.java.util.Arrays;

object Aguili2 extends App {
  val εr = 10.2
  val f0 = 76.064 * GHZ
  val df = 128 * MHZ
  var d = 1 * MM
  val fs = dstepsFrom(f0, 39, df)
  var w = 1.36 * MM
  var μ0 = U0
  var π = PI

  //  println(new Data(76.064*GHZ).find_V1().V)
  println(formatFrequency(new Data(76.064 * GHZ).find_V1().df2))
  println(formatFrequency(new Data(77.28 * GHZ).find_V1().df2))

  //  println(stream(fs).mapToObj[String](operand => formatFrequency(operand)).collect(Collectors.joining(", ")));

  //  println("w="+find_f0_w(f0))

  //  Plot.title("dela_f=fct(f)").yname("V").xsamples(fs).titles("V").plot(stream(fs).map(ff=>new Data(ff).find_V().df2).toArray)
  //  Plot.title("ρ=fct(f)").yname("V").xsamples(fs).titles("V").plot(stream(fs).map(ff=>new Data(ff).find_V().ρ).toArray)

  //  private val aaa: CustomDDFunctionXYExpr = define("A", new CustomDDFunctionXY {
  //    override def eval(x: Double, y: Double): Double = new Data(fs(0)).G(Complex.of(x, y))
  //  });

  //  println(new Data(fs(0)).G(Complex.of(0, 0)))
  //  println(new Data(fs(0)).find_V().df2)

  Plot.title("V=fct(f)").yname("V").xsamples(fs).titles("V").plot(Arrays.stream(fs).map(ff => new Data(ff).find_V1().V).toArray)
  d = 0.9 * MM
  private val a1: Array[Double] = Arrays.stream(fs).map(ff => new Data(ff).find_V1().df2).toArray
  d = 1.3 * MM
  private val a2: Array[Double] = Arrays.stream(fs).map(ff => new Data(ff).find_V1().df2).toArray
  Plot.title("df=fct(f)").yname("df-1.30mm").xsamples(fs).titles("df").plot(
    a1, a2
  )

  //  Plot.title("V").samples(1000, 1000).plot(aaa * domain(-1.5 -> 1.5, -1.5 -> 1.5))
  //  Plot.title("V").samples(1000, 1000).plot(aaa * domain(-1.5 -> 1.5, -1.5 -> 1.5))

  class Data(f: Double) {
    var Ψ = 0.0
    var λ = 0.0
    var B = 0.0
    var Xm: Complex = complex(0.0)
    var df2 = 0.0
    var V: Double = 0.0
    var Zm = 0.0
    var β = 0.0
    var ω = 0.0
    var F = 0.0;
    var ρ = 0.0;

    B = π * df / f * (sqr(d / w) + 1)
    Ψ = 2 * π * d / λ
    ω = 2 * π * f
    β = sqrt(sqr(ω / C) * εr - sqr(π / w))
    λ = 2 * π / β;
    Zm = ω * μ0 / β

    def solveG(): Complex = {
      val xx = dsteps(0.7, 1.5, 1000)
      val yy = dsteps(0.7, 1.5, 1000)
      var ix = 0;
      var iy = 0;
      var m = _root_.java.lang.Double.NaN
      var cc: Complex = Complex.NaN;
      while (ix < xx.length) {
        iy = 0;
        while (ix < xx.length) {
          val c0 = Complex.of(xx(ix), xx(iy))
          var g = G(c0);
          if (_root_.java.lang.Double.isNaN(m) || g < m) {
            m = g;
            cc = c0;
          }
          ix = ix + 1;
        }
        iy = iy + 1;
      }
      return cc;
    }

    def G(Xm: Complex): Double = {
      var x = /*min(*/ absdbl(
        (1 + sqr(Xm.arg() * λ / (2 * π * w)))
          * (df * π * sqrt(abs(Xm)) / (f * (1 - abs(Xm)))) - 1
      ) /*, 0.3)*/
      if (x < 1E-1) {
        return x;
      }
      return 0;
    }

    def find_V(): Data = {
      //      val Xms: Array[Double] = stream(solvePoly2dbl(1, B, -1)).filter(x => x >= 0).toArray;
      //      if (Xms.length == 0) {
      //        throw new IllegalArgumentException("No solution")
      //      }
      Xm = solveG();
      var Ψ2 = Xm.arg()
      println("Ψ2=" + Ψ2 + ", Ψ" + Ψ)
      ρ = absdbl(Xm);
      F = sqrt(ρ) * π / (1 - ρ)
      df2 = f / ((1 + sqr(d / w)) * F)

      ////    var Ψ2=atan(refl)
      //    ///////
      //    var V=( Zm*(tan(Ψ)-1) ) / (1 + (Zm*(tan(Ψ)-1)) - tan(Ψ) )
      //
      //    var jV_par_Zm:Complex=(î *V * Zm)/(î *V * Zm);
      //    var refl=(1-jV_par_Zm)/(1+jV_par_Zm);
      //    var ρ=absdbl(refl)
      //    var F=sqrt(ρ)*π/(1-ρ)
      //    w=d/sqrt(f*(1-ρ)/(df*sqrt(ρ)*π)-1)
      println(this)
      return this;
    }

    def find_V1(): Data = {
      V = (-tan(2 * π * d / λ)) * Zm / 2;
      ρ = absdbl(-Zm / (2 * î * V + Zm))
      df2 = f * (1 - ρ) / ((1 + sqr(d / w)) * sqrt(ρ) * π)
      return this;
    }

    //    def find_V0(): Data = {
    //      val Xms: Array[Double] = stream(solvePoly2dbl(1, B, -1)).filter(x => x >= 0).toArray;
    //      if (Xms.length == 0) {
    //        throw new IllegalArgumentException("No solution")
    //      }
    //      Xm = Xms(0);
    //      println(Xm * Xm + B * Xm - 1)
    //
    //      val Xm2 = Xm * Xm
    //      V = (Zm * Xm2 - Zm) / (1 - (Xm2 + Zm * Xm2 + Zm))
    //
    //      // checks!
    //
    //      var Tm = (î * V * Zm) / (î * V + Zm);
    //      ρ = absdbl((1 - Tm) / (1 + Tm))
    //      F = sqrt(ρ) * π / (1 - ρ)
    //      df2 = f / ((1 + sqr(d / w)) * F)
    //
    //      ////    var Ψ2=atan(refl)
    //      //    ///////
    //      //    var V=( Zm*(tan(Ψ)-1) ) / (1 + (Zm*(tan(Ψ)-1)) - tan(Ψ) )
    //      //
    //      //    var jV_par_Zm:Complex=(î *V * Zm)/(î *V * Zm);
    //      //    var refl=(1-jV_par_Zm)/(1+jV_par_Zm);
    //      //    var ρ=absdbl(refl)
    //      //    var F=sqrt(ρ)*π/(1-ρ)
    //      //    w=d/sqrt(f*(1-ρ)/(df*sqrt(ρ)*π)-1)
    //      println(this)
    //      return this;
    //    }

    override def toString = s"Data(Ψ=$Ψ, λ=$λ, B=$B, Xm=$Xm, f=$f, df2=$df2, V=$V, Zm=$Zm, β=$β, ω=$ω, F=$F, ρ=$ρ)"
  }


  //
  //  def find_f0_w(f:Double): Double ={
  //    λ= lambda(f);
  //    Ψ=2*π*d/λ
  //    var ω=2*π*f
  //    var β=sqrt(sqr(ω/C)*εr+sqr(π/w))
  //    var Zm=ω*μ0/β
  ////    var Ψ2=atan(refl)
  //    ///////
  //    var V=( Zm*(tan(Ψ)-1) ) / (1 + (Zm*(tan(Ψ)-1)) - tan(Ψ) )
  //
  //    var jV_par_Zm:Complex=(î *V * Zm)/(î *V * Zm);
  //    var refl=(1-jV_par_Zm)/(1+jV_par_Zm);
  //    var ρ=absdbl(refl)
  //    var F=sqrt(ρ)*π/(1-ρ)
  //    w=d/sqrt(f*(1-ρ)/(df*sqrt(ρ)*π)-1)
  //    return w;
  //  }
  //

}
