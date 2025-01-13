package net.thevpc.scholar.mentoring.extra
import net.thevpc.scholar.hadrumaths.MathScala._
object Malek extends App{
  val f=3*GHZ
  val P=4
  val Q=5
  val π=PI
  val λ=C/f
  val a=λ/3
  val b=λ/5
  val p=param("p")
  val q=param("q")

  val gc=cos((2*p+1)*π/2*(X+a/2)/(a/3))*cos(q*π*(Y-b/2)/b) *
    domain(-a/2 -> (-a/2+a/3), -b/2 -> b/2) +
    sin((2*p+1)*π/2*(X+a-a/3)/(a/3))*cos(q*π*(Y-b/2)/b) * II(a/2-a/3->a/2, -b/2->b/2)
  val g=seq(gc,p,P,q,Q)
  Plot.title("courant").plot(g)
}
