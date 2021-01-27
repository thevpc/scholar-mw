package net.thevpc.scholar.mentoring.extra

import net.thevpc.scholar.hadrumaths.MathScala.{II, _}
import net.thevpc.scholar.hadrumaths.{Complex, ComplexMatrix, Expr, MathScala, Matrix}
import net.thevpc.scholar.hadruwaves.Physics

object CorrectionTP extends App{
  var f=3*GHZ
  val π=PI
  val P=4
  val Q=5
  val MN=10
  val λ=C/f
  val l=λ/8
  val w=λ/30
  val a=λ/3
  val b=λ/5
  val p=param("p")
  val q=param("q")
  val m=param("m")
  val n=param("n")
  val u= 2 * (m<>0 && n<>0)+1* (m===0 || n===0);
  var domain=II(0.0->a,0.0->b)
  var epsr=2.4 // permettivité du dielectrique
  var ep=1*MM //epaisseur du direlectrique
  var srcw=1*MM //epaisseur du direlectrique
  var antepaisseur=2*MM //epaisseur du direlectrique
  var Zc =50;
  var S11=0*î;
  var Zin=0*î;
//  println(u(n->0)(m->0))
//  println(u(n->1)(m->0))
//  println(u(n->0)(m->1))
//  println(u(n->1)(m->1))

  // donner l'expression de abs(x)
  //var ax=X*(X>0)-X*(X<0);


 def calcLesS11(): Unit={
    var s=dtimes(1*GHZ,10*GHZ,10).map(x=>{
      calc(x);
      S11
    })
   Plot.plot(s);
 }

 def calc(fr:Double): Unit={
   f=fr;
   val omega=2*π*f
   val k=omega/C
   var gp= cos( (2* p+1)* π * X) *2*l*
   II(0.0->l,-antepaisseur/2->antepaisseur/2);

   val g=seq(gp,p,0,Q);
   var E0=1*II(0.0->srcw,-antepaisseur/2->antepaisseur/2);


   val f_temn_x=n/b*sqrt(2*u/((a*b)*(sqr(m*π/a)+sqr(n*π/b))))*
   cos(n*π*X/a)*sin(m*π*Y/b);
   val f_temn_y= -m/b*sqrt(2*u/((a*b)*(sqr(m*π/a)+sqr(n*π/b))))*
   sin(n*π*X/a)*cos(m*π*Y/b);
   var f_temn=vector(f_temn_x,f_temn_y)*domain
   var f_te=seq(f_temn,n,MN,m,MN,(mm:Double,nn:Double)=>mm!=0 || nn!=0)

   val f_tmmn_x= -m/a*sqrt(2*u/((a*b)*(sqr(m*π/a)+sqr(n*π/b))))*
   cos(n*π*X/a)*sin(m*π*Y/b);
   val f_tmmn_y= -n/b*sqrt(2*u/((a*b)*(sqr(m*π/a)+sqr(n*π/b))))*
   sin(n*π*X/a)*cos(m*π*Y/b);
   var f_tmmn=vector(f_tmmn_x,f_tmmn_y)*domain
   var f_tm=seq(f_tmmn,n,MN,m,MN,(mm:Double,nn:Double)=>mm!=0 || nn!=0)

   var fmn= f_te:+f_tm
   Plot.title("Fonctions de mode").plot(fmn)
   //  Plot.plot(f_temn_x(n->1)(m->1))
   val sp: Matrix[Expr] = fmn :** g
   // fmn(0) ** g(0) == sp(0,0)

   val gammaccmn=sqrt(sqr(m*π/a)+sqr(n*π/b)-k*k*epsr)
   val gammalomn=sqrt(sqr(m*π/a)+sqr(n*π/b)-k*k*1)
   val yccmn=gammaccmn * cotanh(gammaccmn*ep)/î*omega*U0
   val ylomn=sqrt(sqr(m*π/a)+sqr(n*π/b)-k*k*1)
   val zmn=1/(yccmn+ylomn)
   var A=matrix(Q,(p:Int,q:Int)=>{
   var s=0*î;
   dsteps(MN).foreach(mm=>{
   dsteps(MN).foreach(nn=>{
   var f_mn = f_temn(m -> mm)(n -> nn)
   val z_mn = zmn(m -> mm)(n -> nn).toComplex
   s = s + (g(p) ** f_mn).toComplex *
   z_mn *
   (f_mn).toComplex

   f_mn = f_tmmn(m -> mm)(n -> nn);
   s = s+ (g(p)** f_mn).toComplex *
   z_mn *
   (f_mn*g(q)).toComplex
 })
 })
   s
 })

   var B=columnMatrix(Q,(pp:Int)=>(gp(p->pp)**E0).toComplex);

   //A*X=B (X == J)
   var J: ComplexMatrix=inv(A)*B; // la matrice des aplhas p
   var Je=J ** g
   var Je2=esum(Q,(pp:Int)=>J(pp) * gp(p->pp))

   /*
     A=[a1, a2, a3]  B=[b1, b2, b3]
     A ** B produit scalaire entre deux vecteurs
     A ** B = a1*b1 + a2*b2 + ...

     A :** B la matrice (combinaison)  des produits scalaire
     [a1**b1  a1**b2 a1**b3
      a2**b1  a2**b2 a2**b3
      a3**b1  a3**b2 a3**b3 ]

     A=[a1, a2, a3]  B=[b1, b2, b3]
     A :+ B  ==> [a1, a2, a3, b1, b2, b3]
     A + B  ==> [a1+b1, a2+b2, a3+b3]
   */

   var Jm=0*ê;
   dsteps(MN).foreach((mm:Double)=>{
   dsteps(MN).foreach((nn:Double)=>{
   isteps(Q).foreach((pp:Int)=>{
   var g_p : Expr=gp(p->pp);
   var f_mn = f_temn(m -> mm)(n -> nn)
   Jm = Jm + J(pp) * (g_p ** f_mn) * f_mn;
   f_mn = f_tmmn(m -> mm)(n -> nn)
   Jm = Jm + J(pp,0) * (g_p ** f_mn) * f_mn;
 })
 })
 })


   var Em=0*ê;
   dsteps(MN).foreach((mm:Double)=>{
   dsteps(MN).foreach((nn:Double)=>{
   isteps(Q).foreach((pp:Int)=>{
   var g_p : Expr=gp(p->pp);
   val z_mn = zmn(m -> mm)(n -> nn).toComplex
   var f_mn = f_temn(m -> mm)(n -> nn)
   Em = Em + J(pp) * (g_p ** f_mn) * z_mn* f_mn;
   f_mn = f_tmmn(m -> mm)(n -> nn)
   Em = Em + J(pp,0) * (g_p ** f_mn) * z_mn* f_mn;
 })
 })
 })
   Zin=inv(tr(B)*inv(A)*B).toComplex;
   S11=(Zin-Zc)/(Zin+Zc)
 }
}

