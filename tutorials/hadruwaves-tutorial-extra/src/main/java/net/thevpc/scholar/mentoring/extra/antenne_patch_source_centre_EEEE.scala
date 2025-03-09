package net.thevpc.scholar.mentoring.extra

/**
 * Created by basma on 04/10/15.
 */

import net.thevpc.scholar.hadrumaths.MathScala._
import net.thevpc.scholar.hadrumaths._
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToComplex
import net.thevpc.scholar.hadruwaves.ModeInfo
import net.thevpc.scholar.hadruwaves.Physics._
import net.thevpc.scholar.hadruwaves.mom._


//ligne ouverte

object antenne_patch_source_centre_EEEE {

  def main(args: Array[String]) {
    var JE3 = calcul_JE3().J3
    // Plot.plot(JE3)

  }


  def calcul_JE3(): param_EJ = {
    var fn_max = 250000
    //var phi_max = 3
    var a = 12.7 * MILLIMETER
    // var b = 12.7 * MILLIMETER
    var freq = 3.5 * GHZ
    var lamda = C / freq
    // println("lamda="+lamda)
    var l = lamda + 0.25 * MILLIMETER
    var w = 2 * MILLIMETER //1.27 * MILLIMETER;;
    var c1 = 0.1 * MILLIMETER;
    //var h = SourceMethodParam.h//1.27 * MILLIMETER
    var Lp = (lamda / 2) + 0.1 * MILLIMETER //19 * MILLIMETER
    // println("Lp="+Lp)
    // var La=(l-Lp)/2
    var dbox = domain(0.0 -> a, 0.0 -> l)
    var nb = 22

    var dantenne = domain(((a - w) / 2) -> ((a + w) / 2), 0.0 -> Lp)
    var m = param("m")
    var n = param("n")
    var Omega = omega(freq)

    //    val e1 = (1/sqrt(w*c1)) * II( (a-w) / 2 -> (a+w) / 2 , (l-c1)/2-> (l+c1)/2 )
    val e1 = (1 / sqrt(w * c1)) * II((a - w) / 2 -> (a + w) / 2, 0.0 -> c1)

    var box1 = MomStructure.EEEE(dbox, freq, fn_max, BoxSpace.matchedLoad(), BoxSpace.nothing())

    // box1.getCacheManager
    //      .setEnabled(true)
    //      .setRootFolder(FormatFactory.format(new Date(),"dd-MM-yyyy"))
    box1.setSources(SourceFactory.createPlanarSources(SourceFactory.createPlanarSource(e1, complex(50))))

    val modes1: Array[ModeInfo] = box1.getModes
    var ii1 = 0
    while (ii1 < modes1.length) {
      val m: ModeInfo = modes1(ii1)
      m.mode
      m.mode.m // recupere m et n
      ii1 += 1;
    }
    //var fn1=modes1.getModeFunctions

    var SP: ComplexMatrix = matrix(modes1.length, nb,
      (p: Int, m: Int) => {
        var mm = m //+1
        var phi1_expr = sin((2 * mm + 1) * PI * (Y - Lp) / 2) / (2 * Lp) * dantenne
        var norme = norm(phi1_expr)
        var phi = phi1_expr / norme
        var fn1 = modes1(p).fn
        complex(fn1.getComponent(Axis.Y) ** phi)
      })

    Plot.asMatrix().title("scalar product").plot(SP)

    val Z: ComplexMatrix = matrix(nb, nb,
        (p: Int, q: Int) => {
          var i = 0
          var Zp: Complex = 0*î
          while (i < modes1.length) {
            var impedance = modes1(i).impedance.impedanceValue()
            Zp = Zp + impedance * (SP(i, p)) * (SP(i, q))
            i += 1
          }
          Zp
        }
      )

    Plot.asMatrix().title("Matrix Z").plot(Z)

    val E: ComplexMatrix = matrix(nb, 1,
      (p: Int, q: Int) => {
          var phi1_expr = sin((2 * p + 1) * PI * (Y - Lp) / 2) / (2 * Lp) * dantenne
          var norme = norm(phi1_expr)
          var phi = phi1_expr / norme
          complex(e1 ** phi)
        }
      )
    Plot.asMatrix().title("Vector E").plot(E)


    var X1 = Z.solve(E)
    Plot.asMatrix().title("X").plot(X1)
    var pq = 0
    var Je: Expr = 0*dbox
    while (pq < nb) {
      var p = pq //+1

      var phi1_expr = sin((2 * p + 1) * PI * (Y - Lp) / 2) / (2 * Lp) * dantenne
      var phi = phi1_expr / norm(phi1_expr)
      Je = Je + (X1(pq) * phi)
      pq += 1
    }
    Plot.asCurve().domain(dbox).title("Je courbe").plot(Je)
    //    Plot.asSurface().domain(dbox).title("Je surface").plot(Je)
    var Jb: Expr = 0.0
    var Eb: Expr = 0
    var pp = 0
    //    while (pp < nb) {
    //     // var p=pp+1
    //      for (nn <- 0 to modes1.length - 1) {
    //        var fn1=modes1(nn).fn
    //         Jb=Jb +  (X1(pp) * SP(nn, pp) * fn1.getComponent(Axis.X))
    //        var impedance=modes1(nn).impedance
    //         Eb = Eb + (X1(pp) * SP(nn, pp)*  impedance * fn1.getComponent(Axis.X))
    //      }
    //      pp+=1
    //    }

    //  //  Plot.asCurve().domain(dantenne).title("Jb courbe dantenne").plot(Jb)
    //    Plot.asCurve().domain(dbox).title("Jb courbe dbox").plot(Jb)
    //
    //  //  Plot.asSurface().domain(dantenne).title("Jb surface dantenne").plot(Jb)
    //    Plot.asSurface().domain(dbox).title("Jb surface dbox").plot(Jb)
    //
    //  //  Plot.asCurve().domain(dantenne).title("Eb courbe dantenne").plot(Eb)
    //    Plot.asCurve().domain(dbox).title("Eb courbe dbox").plot(Eb)
    //
    //   // Plot.asSurface().domain(dantenne).title("Eb surface dantenne").plot(Eb)
    //
    //
    //    Plot.asSurface().domain(dbox).title("Eb surface dbox").plot(Eb)
    var ej = new param_EJ
    ej.Jessai3 = Je
    ej.E3 = Eb
    ej.J3 = Jb

    return ej

  }

  class param_EJ {
    var Jessai3: Expr = 0.0;
    var E3: Expr = 0.0;
    var J3: Expr = 0.0;
  }
}