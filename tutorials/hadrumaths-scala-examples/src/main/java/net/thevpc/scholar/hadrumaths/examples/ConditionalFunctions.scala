package net.thevpc.scholar.hadrumaths.examples

import net.thevpc.scholar.hadrumaths._
import net.thevpc.scholar.hadrumaths.MathScala._

object ConditionalFunctions {
  def main(args: Array[String]): Unit = {
    main1()
    main2()
    main3()
 }

  def main1(): Unit = {
    val dom = domain(0.0 -> 2 * PI, 0.0 -> 2 * PI)
    var n=param("n")
    var fn=(((1*(n===1))+(2*(n<>1)))*X).title("f${n}")
    println(fn(n->2))
    println(fn(n->0)!!)
    println(fn(n->1)!!)
    println(fn(n->2)!!)
    println(fn(n->3)!!)
    val gn=(If(n===1, 1, 2)*X).title("g${n}")
    val fn_s=fn.inflate(n.steps(1,2))!!;
    val gn_s=gn.inflate(n.steps(1,2))
    println("fn="+fn)
    println("fn_s="+fn_s.transform($STRING,(i:Int,e:Expr)=>e.getTitle))
    println("fn_s2="+fn_s.transform($STRING,(i:Int,e:Expr)=>e.toString))
    println("gn="+gn)
    println("gn_s="+gn_s)
//    val gs2 = gn_s(1)
    println(gn_s(0))
    println(gn_s(1))
    println(gn_s(1).toDC.evalComplex(2))
    val console = Plot.console()
    console.newPlot().title("fn_s").asCurve().domain(dom).plot(fn_s)
    //console.newPlot().title("gn_s").asCurve().domain(dom).plot(gn_s)
  }

  def main2(): Unit = {
    val dom = domain(0.0 -> 2 * PI, 0.0 -> 2 * PI)
    var n=param("n")
    var fn=((sin(X)*(n===1))+(cos(X)*(n!==1))).setTitle("f${n}")
    var gn=If(n===1, sin(X), cos(X)).setTitle("g${n}")

    var fn_s=fn.inflate(n.steps(1,2))
    var gn_s=gn.inflate(n.steps(1,2))
    println(fn.toString)
    println(fn_s)
    println(gn)
    println(gn_s)
//    val gs2 = gn_s(1)
    println(gn_s(0))
    println(gn_s(1))
    println(gn_s(1).toDC.evalComplex(2))
    val console = Plot.console()
    console.Plot.title("fn_s").asCurve().domain(dom).plot(fn_s)
    console.Plot.title("gn_s").asCurve().domain(dom).plot(gn_s)
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
