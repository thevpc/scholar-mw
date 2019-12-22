package net.vpc.scholar

import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadruplot.Plot

/**
  * Created by vpc on 7/17/17.
  */
object IntegrationExamples extends App{
//  MathsBase.Config.setCacheEnabled(false)
//  MathsBase.Config.setScalarProductOperator(ScalarProductOperatorFactory.hardFormal())
  //  MathsBase.Config.setScalarProductOperator(ScalarProductOperatorFactory.quad())


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
