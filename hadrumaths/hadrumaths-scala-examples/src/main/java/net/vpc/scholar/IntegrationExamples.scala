package net.vpc.scholar

import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadrumaths.symbolic._
import net.vpc.scholar.hadruplot.Plot

/**
  * Created by vpc on 7/17/17.
  */
object IntegrationExamples extends App{
//  Maths.Config.setCacheEnabled(false)
//  Maths.Config.setScalarProductOperator(ScalarProductOperatorFactory.hardFormal())
  //  Maths.Config.setScalarProductOperator(ScalarProductOperatorFactory.quad())


  var T : CustomDDFunctionXDefinition=null;
  var t : Expr=null;
  var ii : Complex=null;
  T=define("T",(x:Double)=>sin(cos(x))/cos(x))
  t = T(X) * domain(0.0 -> PI)
  Plot.plot(t)
  ii = integrate(t)
  println(ii)

  T=define("T",(x:Double)=>sin(x))
  t = T(X) * domain(0.0 -> PI)
  Plot.plot(t)
  ii = integrate(t)
  println(ii)
//
  T=define("T",(x:Double)=>(x))
  t = T(X) * domain(0.0 -> 1.0)
  Plot.plot(t)
  ii = integrate(t)
  println(ii)
}
