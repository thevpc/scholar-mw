package net.vpc.scholar.mentoring.ch01_scala

import net.vpc.scholar.mentoring.ch01_scala.T04_ScalaDataFlows.i

object T09_ScalaFunctions {

  def main(args: Array[String]): Unit = {
    var x = fact(3)
    proc(x.toString)
  }

  def fact(x: Int): Int = if (x <= 1) 1 else x * fact(x - 1)

  def proc(s: String): Unit = println(s)
}
