package net.vpc.scholar.mentoring.temp
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadruplot.Plot
object Example extends App{
  var n=param("n");
  var λ=X;
  var r=X;
  var π=PI;
  var k=1;
//  val ee: Expr = sin(n * λ) / (n * sin(λ)) * II(0.0 -> 2 * π)
//  itimes(0,8).foreach(i=>Plot.title(""+i).plot(ee(n->i)))
//  Plot.plot(ee(n->8))
//  var ee2=1/(r*r)*(1+1/(î*r))*(cos(-k*r)+î*sin(-k*r))* II(1.0->2.0)
//  var ee3=1/(r*r)* II(1.0->2.0)
//
  var ee2=sin(r)/r* II(0.0->6*π)
  var ee3=cos(r*r)/r * II(0.0->6*π)
//  Plot.plot((1/((r*r))))
  Plot.plot(ee2,ee3)
}
