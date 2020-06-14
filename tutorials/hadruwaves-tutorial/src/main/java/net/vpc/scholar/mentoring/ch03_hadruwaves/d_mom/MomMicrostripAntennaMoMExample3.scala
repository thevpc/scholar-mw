/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.mentoring.ch0x.s4

import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadruwaves.mom._
import net.vpc.common.util.Chronometer
import net.vpc.common.mon.{ProgressMonitor, ProgressMonitors}
import net.vpc.scholar.hadruwaves.{Material, WallBorders}
import net.vpc.scholar.hadruwaves.mom.modes.BoxModeFunctions

object MomMicrostripAntennaMoMExample3 {

  def main(args: Array[String]): Unit = {
    var f = 5 * GHZ
    //box
    var a = 100 * MM
    var b = 100 * MM
    var d = domain(0.0 -> a, -b / 2 -> b / 2)

    //line (relative to box)
    var ll = 60 * MM
    var lw = 10 * MM
    var ld = domain(0.0 -> ll, (-lw / 2) -> (lw / 2))
    //source
    var sl = 2 * MM
    var sw = lw
    var sd = domain(0.0 -> sl, -sw / 2 -> sw / 2)

    val nbModeFunctions: Int = 1000
    val nbTestFunctions: Int = 8
    val epsilon_air = 1
    val epsilon_epoxy = 2.2

    val modesFunctions: ModeFunctions = new  BoxModeFunctions()
    var env=new DefaultModeFunctionsEnv
    modesFunctions.setEnv(env)
    env.setBorders(WallBorders.EEEE)
    env.setDomain(d)
    env.setFrequency(f)
    modesFunctions.setSize(nbModeFunctions)
    env.setFirstBoxSpace(BoxSpace
      .shortCircuit(Material.substrate(2.2),
        1 * MM
      ))
    env.setSecondBoxSpace(BoxSpace
      .shortCircuit(Material.VACUUM,
        100 * MM
      ))

    val src = 1.0 * sd
    var p = param("p")
    val gp = cos((2 * p + 1) / 2 * PI * X / ll) * ld
    val g = gp.inflate(p.in(0,nbTestFunctions-1))
    //    m.setTestFunctionsCount(4);
    var B = columnMatrix(nbTestFunctions,(i: Int)=>  complex(g(i) ** src))

    var A = matrix(nbTestFunctions, nbTestFunctions,
      (r: Int, c: Int)=> {
          var v = 0 * î
          var gp1 = g(r)
          var gp2 = g(c)
          var mn = 0
          while (mn < nbModeFunctions) {
            var fn = modesFunctions.fn(mn)
            var zn = modesFunctions.zn(mn)
            v += complex((gp1 ** fn) * zn * (fn ** gp2))
            mn += 1
          }
          complex(v)
        }
      )
    tr(B)
    Plot.title("A").plot(A)
    Plot.title("B").plot(B)
    //val Xj: Matrix = A.solve(B)
    val Xj = inv(A) * B
    Plot.title("Xj").plot(Xj)
    var J: Expr = Xj ** g
    var pi = 0
    while (pi < nbTestFunctions) {
      J += Xj(pi) * g(pi)
      pi += 1
    }
    Plot.title("J").samples(100, 100).plot(J)
  }

}
