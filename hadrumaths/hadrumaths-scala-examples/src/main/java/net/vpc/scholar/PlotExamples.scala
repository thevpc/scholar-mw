package net.vpc.scholar
import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths._
/**
  * Created by vpc on 7/17/17.
  */
object PlotExamples extends App{
  val d1 = domain(-5 * PI -> 5 * PI, -5 * PI -> 5 * PI)
  val d2 = domain(0.0 -> 2 * PI, -(2 * PI) -> 2 * PI)
//  var f=sin(2*X)*cos(3*PI/5*(Y-5))*d2
  var f=tan(X)*domain(0.0 -> 2*PI)

  Plot.update("Titi").samples(Samples.adaptive(100,1000)).titles().asReal().plot(f)

////  Maths.adaptiveEval(f,new AdaptiveConfig().setMinimumXSamples(10).setMaximumXSamples(20).setListener((event) => println(event.getSamples.values)))
//  var r=Maths.adaptiveEval(f,new AdaptiveConfig().setError(1).setMinimumXSamples(50).setMaximumXSamples(100).setListener((event) => {
//    val samples = event.getSamples
////    println(samples.x)
////    println(samples.values)
//    println(event.getSamples.size()+" : "+event.getError+";"+ event.getX(0)+ " ;" +event.getValue(0))
////    println("============")
//    var match2=new DoubleArray()
//    var i=0;
//    while(i<samples.size()){
////      if(i>=event.getIndex-1 && i<=event.getIndex+1){
//      if(i==event.getIndex){
//        match2.add(40*(event.getType));
//      }else{
//        match2.add(0);
//      }
//      i=i+1
//    }
//    while(match2.size()>4){
//      match2.remove(0);
//    }
//    Plot.update("Toto").xsamples(samples.x).titles("X",(if(event.getType==1)"Width" else if(event.getType==2) "width" else "Derive")+event.getError).asReal().asSurface().plot(samples.values,match2)
////    Thread.sleep(500)
//  }))
//  println("finish..."+r.size()+" : "+r.error)
//
}
