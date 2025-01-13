//package test
//
//import net.thevpc.scholar.math.MathScala._
//import net.thevpc.scholar.math.{Matrix, MathScala}
//import MathScala._
//import net.thevpc.math.plot.Plot
//
//object Main {
//  def main(args: Array[String]) {
//    var t = Matrix.newHermitian(3, (row: Int, column: Int) => CONE)
//    val domain = II(1.0 -> 2.0, 3.0 -> 4.0)
//    val x = î * 3
//    val y = 3 * î
//
//    //    Plot.plotCurvesX(x,y,i=>{"Le titre de "+i})
//    //    val s: DDxy = sinxy("NS", m, n, domain);
//
//    //    intPairsPositive(5).foreach(x=>println(x));
//    //    plotSurface(dddd.addMargin (1,1,1,1)/100,s).exitOnClose
//    //    plotCurves(domain.xtimes(100),3.0,ComplexAsReal, s(n->1).Series(m -> dsteps(1.0, 10.0)))
//    val m = param("m")
//    val n = param("n")
//    val p = param("p")
//    //    var f: DefaultDoubleToComplex = (X + sin(X) * sin(m * Y)) * II(0.0 -> 10, 0.0 -> 20.0) !!;
//    //    val d: DomainXY = f.Domain.addMargin(2, 2, 2, 2); //.expand(new DomainXY(-10.0-> 12.0 , -10.0-> 30.0))
//    //    plotSurface(d / 100, f(m -> 2)).exitOnClose.setTitle(f.toString)
//    //    plotCurve(5, d.y / 100, f(m -> 2)).exitOnClose.setTitle(f.toString)
//    //    plotCurves(d.x / 1000, 5, f(m -> 1), f(m -> 2), f(m -> 3)).exitOnClose.setTitle(f.toString)
//    //
//    val a = 0.3;
//    val b = 0.3;
//    var gp = (Y * sin((2 * PI * p) / a * X) * II(-0.3 -> 0.3, -0.3 -> 0.3) + 0.2*II(-0.1 -> 0.1, -0.1 -> 0.1)); //!!;
////    var gp = (Y  * II(-0.3 -> 0.3, -0.3 -> 0.3)) //+ 0.2*II(-0.1 -> 0.1, -0.1 -> 0.1)); //!!;
//    var s2 = simplify(gp.setParam("p", 3));
//    println(s2)
//    println(s2.getDomain)
//    println(s2.isDD)
//    println("**********" + s2.toDD)
//    println("**********" + (a / 2)+" :: " +(b / 2))
//    println("**********" + s2.toDD.computeDouble(a / 2, b / 2))
//    gp=s2
////    val gp = (X*X*2-Y)*(X*X*2-Y) * II(0.0->1.0,0.0->1.0);// /* sin((2 * PI * p) / a * X) * II(-0.3 -> 0.3, -0.3 -> 0.3) +*/ *0.2*II(-0.1 -> 0.1, -0.1 -> 0.1); //!!;
//    println(gp)
//    println(gp.setParam("p", 3))
//
//    var r=inv(neg(cos(n*n+n/n-n))).setParam("n",1)
//    println("**********" + r)
//    println("**********" + simplify(r))
//    println("**********" + r)
//        Plot.title("One").plot(gp)
//        Plot.title("Two").plot(gp)
////    Plot.plotExpressions("Here2", null, s2).display()
//    //    val fmn = sin((2 * PI * m) / A * X) * sin((2 * PI * n) / B * Y) * II(0.0 -> A, 0.0 -> B) !!;
//    //    //plotSurface(fmn.Domain/100,fmn(m->2)(n->2)).exitOnClose.setTitle(f.toString)
//    //    //    var f4 = sum2(m,1,3,n,1,3,gp**fmn gp**fmn)!!;
//    //    val maxp = 10;
//    //    val maxq = 10;
//    //    var matrix = CMatrix(maxp, maxq, (i, j) => {
//    //      val v1 = gp(p -> i) ** fmn
//    //      val v2 = gp(p -> j) ** fmn
//    //      sum2(m, 1, 300, n, 1, 300, v1 * v2).!!.Value
//    //    });
//    //
//    //    println(matrix)
//    //    plotMatrix(matrix);
//
//  }
//
//}