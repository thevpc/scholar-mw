package net.thevpc.scholar.hadrumaths.examples

import net.thevpc.scholar.hadrumaths._
import net.thevpc.scholar.hadrumaths.MathScala._

/**
  * Created by vpc on 7/17/17.
  */
object IntegrationExamples extends App{
//  Maths.Config.setCacheEnabled(false)
//  Maths.Config.setScalarProductOperator(ScalarProductOperatorFactory.hardFormal())
  //  Maths.Config.setScalarProductOperator(ScalarProductOperatorFactory.quad())


  var T : Expr=null;
  var t : Expr=null;
  var ii : Complex=null;
  T=define("T",(x:Double)=>sin(cos(x))/cos(x))
  t = T * domain(0.0 -> PI)
  Plot.plot(t)
  ii = integrate(t)
  println(ii)

  T=define("T",(x:Double)=>sin(x))
  t = T * domain(0.0 -> PI)
  Plot.plot(t)
  ii = integrate(t)
  println(ii)
//
  T=define("T",(x:Double)=>(x))
  t = T * domain(0.0 -> 1.0)
  Plot.plot(t)
  ii = integrate(t)
  println(ii)
}
