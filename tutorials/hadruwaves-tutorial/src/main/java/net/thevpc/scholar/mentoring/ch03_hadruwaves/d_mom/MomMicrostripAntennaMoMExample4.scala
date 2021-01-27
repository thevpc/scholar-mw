/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.mentoring.ch0x.s4

import net.thevpc.scholar.hadrumaths.MathScala._
import net.thevpc.scholar.hadrumaths._
import net.thevpc.scholar.hadruwaves.{Material, WallBorders}
import net.thevpc.scholar.hadruwaves.mom.BoxSpace._
import net.thevpc.scholar.hadruwaves.mom.DefaultModeFunctionsEnv
import net.thevpc.scholar.hadruwaves.mom.ModeFunctionsFactory._
import net.thevpc.scholar.hadruwaves.mom.modes.BoxModeFunctions

object MomMicrostripAntennaMoMExample4 {
  def main(args: Array[String]): Unit = {
    var freq = 4.79 * GHZ
    var a = (100.567 * MM) / 2
    var b = 45 * MM
    var ep = 1.59 * MM
    var lx = 10 * MM
    var L = b / 2
    var w = b / 20
    var P=4

    var dBox = domain(0.0 -> a, -b / 2 -> b / 2)
    var dLgn = domain((a / 2 - lx / 2) -> (a / 2 + lx / 2), -b / 2 -> (-b / 2 + L))
    var dSrc = domain((a / 2 - lx / 2) -> (a / 2 + lx / 2), -b / 2 -> (-b / 2 + w))
    var env=new DefaultModeFunctionsEnv()
      .setBorders(WallBorders.EEEE)
      .setDomain(dBox)
      .setFrequency(freq)
      .setFirstBoxSpace(shortCircuit(Material.substrate(2.2), ep))
      .setSecondBoxSpace(shortCircuit(Material.VACUUM, 0.1))
    var modes = new BoxModeFunctions().setEnv(env).setSize(1000)


    Plot.title("Mode functions").plot(modes.fn)

    var n = param("n")

    var gpexpr = cos((2 * n + 1) * (PI / 2) * (Y+b/2) / L) * dLgn

    var gp = gpexpr.inflate(n.in(0, P-1))
    Plot.title("Test Functions").plot(gp)

    var s=1/sqrt(lx*w) *dSrc

    //creates a matrix of scala products for each row r and column c the cell sp(r)(c) = gp(r) ** modes.fn(c)
    //this is more effective than calculating (gps(p)**fi)*zi*(fi**gps(q)) on each iteration
    val sp = (gp :** modes.fn).rows

    var B=columnMatrix(P, (p:Int)=>complex(s**gp(p)))
    var A=matrix(P, (p:Int, q:Int)=>complex(sp(p) ** (row(modes.zn),sp(q))))
    var Xp=inv(A)*B

    var J = Xp ** gp

    Plot.title("J").domain(dBox).plot(J)
  }
}
