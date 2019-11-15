package net.vpc.scholar.mentoring.ch0x.s1

import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadruwaves.Physics._
import net.vpc.scholar.hadruwaves.WallBorders

/**
  * Created by vpc on 5/27/17.
  */
object MicrostripLineMoMExample extends App {
  var N = 200000        // number of modes
  var Gn = 4        // number of modes
  var a = 100 * CM // box length along x
  var b = 50 * CM  // box length along y
  var v = 1 * MM   // width (along x) of the source
  var l = 5 * MM   // length of the strip line
  var w = 1 * MM   // width (along y) of the strip line
  var f = 3 * GHZ     // frequency
  val space = matchedLoadBoxSpace(1) // layer descr for the open componentVectorSpace, 1 refers to the espilon_r of the air/void
  val mass = shortCircuitBoxSpace(2.2, 1 * CM) // layer descr for 1cm height of substrate, with espilon_r 2.2

  var lineDomain = domain(0.0 -> l * 1.5, -w -> w) // line domain
  var box = domain(0.0 -> a, -b / 2 -> b / 2) // box domain

  var m = param("m")  // m and n parameters
  var n = param("n")

  // defines a parametrized test function
  var gp = cos((2 * m + 1) * PI / 2 * X / l) * cos(n * PI / w * (Y + w / 2)
  ) * lineDomain

  //if(true) System.exit(0);

  println(gp)

  println(gp(m -> 3)) // replaces m by 3 in gp, remember m is a parameter using (->) operator
  println(gp(m -> 3) !!) // replaces m by 3 in gp and then simplifies it with the !! operator

  var g = seq(gp,        // evaluates a sequence/list of all values of gp test functions where
    m, dsteps(0, Gn),     // m varies from 0 to 4 (inclusive) an so does n
    n, dsteps(0, Gn)
  )

  var P = g.length()     // test functions count
  var e0 = 1 * domain(0.0 -> v, -w / 2 -> w / 2)  // source

  // B matrix according to the MoM method is
  // a column vector of P elements
  // the i'th element is defined as the scalar product
  // of g(i) and e0
  // thus, <g(i) , e0> is evaluated using the ** operator
  var B = columnMatrix(P, (i:Int) => complex(g(i) ** e0))


  Plot.title("Test functions").plot(g)  // plots the g test functions sequence/list

  // uses a predefined library for
  // evaluating the Z operator (Mode Operator)
  // for the Box of 4 Electric walls (EEEE)
  // over the defined domain box
  var modes = boxModes(WallBorders.EEEE, box)
  val fmn=modes.functions(N)
  val indexes = modes.indexes(N) // calculates all first N mode indices of the mode (TE01, TE10, TEM1,...)
  println(indexes.slice(0,4).mkString(", ")) // gets the first 4 modes (from 0 to 3) then prints items separated by ,
  val fct = modes.functions(N) // calculates all first N mode functions of the box

  println(fct.slice(0,4).mkString("\n")) // gets the first 4 modes function then prints items separated by \n

  Plot.title("Mode functions").plot(fct.slice(0,50))


  // calculates all scalar products between g test function and fct mode functions
  // evaluated as a matrix M where Mij=<g(i),fct(j)>
  //var ps: Matrix = matrix(g :** fct)
  val ymn1=modes.admittances(N,f,space)
  val ymn2=modes.admittances(N,f,mass)
  val ymn=ymn1++ymn2

  // z operator is the sum (using csum function) over
  // mode indexes (each u) of
  // <g(p),fct(u)> zu <fct(u),g(q)>
  // zu is the mode impedance of the box calculated as yu1 // yu2
  // witch are respectively the mode impedance of the upper componentVectorSpace
  // and the substrate
  def z(i: Int, j: Int): Complex = {
    csum(indexes.length, (mn:Int) => {
      var yu1 = ymn1(mn)
      var yu2 = ymn2(mn)
      var zu = inv((1 / yu1) + (1 / yu2))
      //      (gi**fct(u))*zmn*(fct(u)**gi)
      complex((g(i)** fmn(mn)) * zu * g(j)**fmn(mn))
    })
  }

  var Z = symmetricMatrix(P, P, (i, j) => z(i, j)) // Z matrix (MoM) defined as z operator over the gp test function
  var Xj = inv(Z) * B // Z * Xj = B  , so ...

  // now that we have Xj coefficients
  // J current is the weighted sum of g test functions
  // so J = <Xj , g>
  var J = Xj ** g
  //var E = Xj ** (g,zmn)

  // plot J current over a domain a bit larger than
  // the g domain
  Plot.title("Current").asHeatMap().
    domain(lineDomain).plot(J)
}
