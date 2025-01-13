package net.thevpc.scholar.mentoring.ch0x.s4

import net.thevpc.scholar.hadrumaths.MathScala._
import net.thevpc.scholar.hadrumaths._
import net.thevpc.scholar.hadruwaves.{Material, ModeIndex}
import net.thevpc.scholar.hadruwaves.Physics._
import net.thevpc.scholar.hadruwaves.WallBorders.EEEE

/**
  * This is a simple example of calculating antenna characteristics
  * using MoM method.
  * Created by vpc on 4/25/17.
  */
object MomMicrostripAntennaMoMExample1 {
  def main(args: Array[String]): Unit = {
    val N = 1000
    var l = 12.5 * MM
    var s = 1 * MM
    var L = 18.75 * MM
    var a = 47.55 * MM
    var b = 4 * MM //22.15 * MM;
    var w = 0.31 * MM
    var W = 1 * MM
    var h = 1 * MM; //substrate thikness
    var espr = 2.2; //substrate permettivity
    var fr = 4 * GHZ
    var g = elist()
    var k = param("k")
    var k2 = param("k2")
    var k3 = param("k3")
    //var p = param("p");
    var Natt = 1.0
    var A = 2 * l / 3

    val boxDomain = domain(0.0 -> a, -b / 2 -> b / 2) //gate function
    val lineDomain = domain(0.0 -> l, -w / 2 -> w / 2)
    val srcDomain = domain(0.0 -> s, -w / 2 -> w / 2)
    val patchDomain = domain(l -> (L + l), (-W) / 2 -> W / 2)

    g :+= (cos(((2 * k + 1) * PI) * X / (2 * l)) * lineDomain).inflate(k.in(0, 5))
//    g :+= seq((cos(((2 * k + 1) * PI) * X / (2 * l)) * lineDomain),k,5)
    g :+= (sin((k2 * PI) * (X - l) / L) * cos((k3 * PI / W) * (Y + W / 2)) * patchDomain).inflate(k2.in(1, 4).and(k3.in(0, 4)))
    g :+= (
      Natt * (X - l + A / 2) / A / 2 * domain(l - (A / 2) -> l, (-w) / 2 -> w / 2)
        +
        Natt * (l + A / 2 - X) / A / 2 * domain(l -> (l + (A / 2)), (-w) / 2 -> w / 2)
      )
    val P = g.size() // number of test functions

    var src = 1 * srcDomain
    val substrateSpace = shortCircuitBoxSpace(Material.substrate(espr), h)
    val superstrateSpace = matchedLoadBoxSpace(Material.VACUUM)

    val mbox=boxModes(EEEE,boxDomain)
    val modes = mbox.indexes(N)
    val f = mbox.functions(N)


    var B = columnVector(P, (p:Int) => complex(g(p) ** src))

    var zmn: Array[Complex] = modes.map((mode: ModeIndex) => {
      val y1 = mbox.admittance(mode, fr, substrateSpace)
      val y2 = mbox.admittance(mode, fr, superstrateSpace)
      1 / (y1 + y2)
    })

    var sp = g :** f // evaluate all scalar products between g and f, it will produce a matrix where
                     // rows are test functions g(p)
                     // and columns are mode functions f(n)
                     // and sp(p,i) = <g(p),f(n)>

    // Z matrix is square hermitian (P x P) Matrix  so half of the matrix evaluation could be spared
    var Z = hermitianMatrix(P, P, (p:Int, q:Int) => csum(N, (n:Int) => complex(sp(p, n) * zmn(n) * sp(q, n))))

    var J = inv(Z) * B
    Plot.title("A").plot(Z)
    Plot.title("B").plot(B)
    Plot.title("X").plot(J)

    var J_test = g ** J // current over test function is actually the scalar product beween test functions and
                        // J unknown

    var J_mode = csum(P, N, (p:Int, n:Int) => complex(J(p) * sp(p, n) * f(n)))
    var E = csum(P, N, (p:Int, n:Int) =>  complex(J(p) * sp(p, n) * zmn(n) * f(n)))


    Plot.title("g").domain(boxDomain).plot(g)
    Plot.title("fn").plot(f)
    Plot.title("J_test").domain(boxDomain).asMesh().plot(J_test)
    Plot.title("J_mode").asMesh().plot(J_mode)
    Plot.title("E").asMesh().plot(E)
  }
}