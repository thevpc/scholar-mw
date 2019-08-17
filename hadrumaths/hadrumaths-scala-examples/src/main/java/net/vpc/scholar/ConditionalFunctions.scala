package net.vpc.scholar

import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadruplot.Plot

object ConditionalFunctions {
  def main(args: Array[String]): Unit = {
    main3()
 }

  def main1(): Unit = {
    val dom = domain(0.0 -> 2 * PI, 0.0 -> 2 * PI)
    var n=param("n")
    var fn=((1*(n===1))+(2*(n!==1))).setTitle("f${n}")
    var gn=If(n===1, 1, 2).setTitle("g${n}")
    var fn_s=seq(fn,n,1,2)
    var gn_s=seq(gn,n,1,2)
    println(fn.toString)
    println(fn_s)
    println(gn)
    println(gn_s)
//    val gs2 = gn_s(1)
    println(gn_s(0))
    println(gn_s(1))
    println(gn_s(1).computeComplex(2))
    val console = Plot.console()
    console.newPlot().title("fn_s").asCurve().domain(dom).plot(fn_s)
    console.newPlot().title("gn_s").asCurve().domain(dom).plot(gn_s)
  }

  def main2(): Unit = {
    val dom = domain(0.0 -> 2 * PI, 0.0 -> 2 * PI)
    var n=param("n")
    var fn=((sin(X)*(n===1))+(cos(X)*(n!==1))).setTitle("f${n}")
    var gn=If(n===1, sin(X), cos(X)).setTitle("g${n}")

    var fn_s=seq(fn,n,1,2)
    var gn_s=seq(gn,n,1,2)
    println(fn.toString)
    println(fn_s)
    println(gn)
    println(gn_s)
//    val gs2 = gn_s(1)
    println(gn_s(0))
    println(gn_s(1))
    println(gn_s(1).computeComplex(2))
    val console = Plot.console()
    console.newPlot().title("fn_s").asCurve().domain(dom).plot(fn_s)
    console.newPlot().title("gn_s").asCurve().domain(dom).plot(gn_s)
  }

  def main3(): Unit = {
    val dom = domain(0.0 -> 2 * PI, 0.0 -> 2 * PI)
    var n=param("n")
    var fn=((sin(X)*(n===1))+(cos(X)*(n!==1))).setTitle("f${n}")*dom
    println(fn)
    fn=fn(n->4)
    println(fn)
    println(fn!!)
    var t=fn**fn;
    var t2=complex(fn**fn);
    print(t)
  }
}
