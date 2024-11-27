package net.thevpc.scholar.mentoring.ch01_scala

object T11_ScalaClosures {

  def main(args: Array[String]): Unit = {
    var h=3
    val rf: (Int, Int) => Int = (x: Int, y: Int) => x + y + h
    val of = (x: Int, y: Int) => x + y + h
    val cf : Calc = (x: Int, y: Int) => x + y + h

    calc(1, 2, (x: Int, y: Int) => x + y + h);
    calc(1, 2, cf);

    h=6
    calc(1, 2, (x: Int, y: Int) => x + y + h);
    calc(1, 2, cf);

    calc(1, 2, (x: Int, y: Int) => x - y+h);
    h=9
    var f:Calc=(x: Int, y: Int) => x + y+h
    h=3
    calc(1, 2, f);
  }

  def calc(x: Int, y: Int, c: Calc) {
    println(c.doit(x, y))
  }

  trait Calc {
    def doit(x: Int, y: Int): Int
  }

}
