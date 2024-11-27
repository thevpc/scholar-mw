package net.thevpc.scholar.mentoring.ch01_scala

object T10_ScalaLambda {

  def main(args: Array[String]): Unit = {
    //lambda expression
    calc(1, 2, (x: Int, y: Int) => x + y);
  }

  def calc(x: Int, y: Int, c: Calc) {
    println(c.doit(x, y))
  }

  trait Calc {
    def doit(x: Int, y: Int): Int
  }

}
