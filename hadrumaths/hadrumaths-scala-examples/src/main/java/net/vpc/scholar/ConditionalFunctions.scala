package net.vpc.scholar

import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths._

object ConditionalFunctions {
  def main(args: Array[String]): Unit = {
    main3()
 }

  def main1(): Unit = {
    val dom = domain(0 -> 2 * PI, 0 -> 2 * PI)
    var n=param("n")
    var fn=((1*(n===1))+(2*(n!==1))).setName("f${n}")
    var gn=If(n===1, 1, 2).setName("g${n}")
    var fn_s=seq(fn,n,1,2).toList
    var gn_s=seq(gn,n,1,2).toList
    println(fn.toString)
    println(fn_s)
    println(gn)
    println(gn_s)
//    val gs2 = gn_s(1)
    println(gn_s(0))
    println(gn_s(1))
    println(gn_s(1).computeComplex(2))
    val console = Plot.console()
    console.plotter().title("fn_s").asCurve().domain(dom).plot(fn_s)
    console.plotter().title("gn_s").asCurve().domain(dom).plot(gn_s)
  }

  def main2(): Unit = {
    val dom = domain(0 -> 2 * PI, 0 -> 2 * PI)
    var n=param("n")
    var fn=((sin(X)*(n===1))+(cos(X)*(n!==1))).setName("f${n}")
    var gn=If(n===1, sin(X), cos(X)).setName("g${n}")

    var fn_s=seq(fn,n,1,2).toList
    var gn_s=seq(gn,n,1,2).toList
    println(fn.toString)
    println(fn_s)
    println(gn)
    println(gn_s)
//    val gs2 = gn_s(1)
    println(gn_s(0))
    println(gn_s(1))
    println(gn_s(1).computeComplex(2))
    val console = Plot.console()
    console.plotter().title("fn_s").asCurve().domain(dom).plot(fn_s)
    console.plotter().title("gn_s").asCurve().domain(dom).plot(gn_s)
  }

  def main3(): Unit = {
    val dom = domain(0 -> 2 * PI, 0 -> 2 * PI)
    var n=param("n")
    var fn=((sin(X)*(n===1))+(cos(X)*(n!==1))).setName("f${n}")*dom
    println(fn)
    fn=fn(n->4)
    println(fn)
    println(fn!!)
    var t=fn**fn;
    var t2=complex(fn**fn);
    print(t)
  }
}