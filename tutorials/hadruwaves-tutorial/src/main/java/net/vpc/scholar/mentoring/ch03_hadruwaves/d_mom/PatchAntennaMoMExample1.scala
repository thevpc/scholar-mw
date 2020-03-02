package net.vpc.scholar.mentoring.ch03_hadruwaves.d_mom

import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadrumaths._

object PatchAntennaMoMExample1 extends App {
//  val π = PI
  var f = 5.7 * GHZ
  var λ = C / f
  val a = λ * 10
  val b = λ * 10
  var L = 12.3 * MM
  var l = λ / 5
  var h = λ / 10
  var s = λ / 20
  var w = λ / 20
  var W = 16 * MM
  var k = param("k")
  var kp = param("kp")
  var t = param("t")
  var E0: Expr = 1 * II(0.0 -> s, -w / 2 -> w / 2)
  var gpx = sin(k * π * (X - l) / L) * cos(kp * π * (Y + (W / 2)) / W) * II(l -> (l + L), -w / 2 -> w / 2)
  var gpxs = gpx.inflate(k.in(0, 2).and(kp.in(0, 2)))
  var glx = cos((2 * t + 1) / 2 * π * (X) / l) * II(0.0 -> (l), -w / 2 -> w / 2)
  var glxs = glx.inflate(t.in(0, 2))
  //concat !
  var g = gpxs ++ glxs
  // Plot.title("Essai Patch").plot(gpxs)
  // Plot.title("Essai Ligne").plot(glxs)
  val m = param("m")
  val n = param("n")
  //var u=1 si m=n=0 sinon u=2
  var u = ((m === 0) * (n === 0) * 1) + ((m !== 0) + (n !== 0)) > 0 * 2
  var pp = sqrt(2 * u / (a * b * (sqr(m * π / a) + sqr(n * π / b))))
  var fmn = vector(
    n / b * pp * cos(n * π * X / a) * sin(m * π * Y / b),
    -m / a * pp * sin(n * π * X / a) * cos(m * π * Y / b),
  )
  var B = columnVector(g.size, (i: Int) => complex(E0 ** g(i)))

  var A = matrix(g.size, (i: Int, j: Int) => î) //should complete me!

  Plot.title("B").plot(B)
  Plot.title("A").plot(A)

  var Jx = inv(A) * B

  var Js = Jx ** g

  Plot.plot(Js)
}
