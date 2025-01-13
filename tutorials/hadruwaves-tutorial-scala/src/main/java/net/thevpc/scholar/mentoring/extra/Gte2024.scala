package net.thevpc.scholar.mentoring.extra
import net.thevpc.scholar.hadrumaths._
import net.thevpc.scholar.hadrumaths.MathScala._
import net.thevpc.scholar.hadruwaves.Physics;
object Gte2024 extends App {
  val π=PI;
  val ε0=EPS0;
  val μ0=U0;

  var f=2.45*GHZ;
  var λ=C/f;
  var μr=1;
  var εr=2.4;
  var μ=μ0*μr;
  var a=10*λ;
  var b=9*λ;
  var L=λ/4;
  var s=λ/10;
  var w=λ/3;
  var k=param("k");
  var p=param("p");
  var gxkp=
    cos(((2*k+1)/2)*π/L*(X-0))*II(0.0->L)
  cos(p*π/w*(Y+w/2))*II(-w/2->w/2)
  ;

  //Plot.plot(gxk(k->0),gxk(k->1));
  private val allExpr: ExprVector = gxkp.inflate(k.in(0, 2)).inflate(p.in(0, 2))
  println(allExpr)
  Plot.title("Les fonctions d'essai")
    .asReal()
    .domain(domain(0.0->a/2,-b/4->b/4))
    .xsamples(100)
    .ysamples(100)
    .asMesh()
    .plot(allExpr)

}
