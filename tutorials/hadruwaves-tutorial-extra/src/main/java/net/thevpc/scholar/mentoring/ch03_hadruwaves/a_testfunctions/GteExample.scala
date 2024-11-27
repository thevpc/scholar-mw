package net.thevpc.scholar.mentoring.ch03_hadruwaves.a_testfunctions

import net.thevpc.scholar.hadrumaths.MathScala._

object GteExample extends App {
    val f =3 *GHZ
    val lambda = C/f
  val a= 2*lambda
  val b = 1.5 *lambda
  val w = lambda/20
  val s =lambda/50
  val L =lambda/2

  val k = param("k")
  val p = param("p")

  val box = domain(0.0-> a , -b/2->b/2)
  val sbox = domain(0.0->s , -w/2->w/2)
  val lbox =domain(0.0->L ,-w/2->w/2)

  val S = 1*sbox

  val gkp = cos((2*k+1) * (PI/2*L) * X) * cos (p*PI/w*(Y+(w/2)))*lbox

  var g00 = normalize (gkp (k->0) (p->0) !!);
  var g03 = gkp (k->0) (p->3) !!;
  var g30 = gkp (k->3) (p->0) !!;
  var g33 = gkp (k->3) (p->3) !!;
  println(g00)

  val T = 3
  var gbest = gkp.inflate(k.in(0,T).and(p.in(0,T)))
  Plot.title("toz").asMesh().plot(gbest )
  val B = columnMatrix(gbest.length() , (i:Int)=> complex(gbest(i)**S))
  Plot().asHeatMap().plot(B)
}
