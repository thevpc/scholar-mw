package net.thevpc.scholar.mentoring.ch03_hadruwaves.a_testfunctions

import net.thevpc.scholar.hadrumaths.Expr
import net.thevpc.scholar.hadrumaths.MathScala._

object ExampleBSpline3 {


  def main(args: Array[String]): Unit = {

    var ksi=dtimes(0,1,10)

    def Spline(p:Int,i:Int,ksi:Array[Double]):Expr={
      if (p==0){
        1*domain(ksi(i)->ksi(i+1))
      }else{
        ((X-ksi(i))/(ksi(i+1)-ksi(i)))*Spline(p-1,i,ksi)+((ksi(i+p+1)-X)/(ksi(i+p+1)-ksi(i+1)))*Spline(p-1,i+1,ksi)
      }
    }

    //var f= 2*X*domain(0.0->0.5)+(-2*X+2)*domain(0.5->1.0);
    val SS = Spline(0, 0, ksi)
    //      val valurs = SS.computeComplexArg(dtimes(0,2,100))
    println(SS)
    //      println(clist(valurs :_ *))
    Plot.plot(SS);
    //      Plot.plot(valurs);


  }
}
