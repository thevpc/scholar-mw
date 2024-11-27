package net.thevpc.scholar.mentoring.ch01_scala

object T03_ScalaVarValDef extends App{

  //declare variable
  var a1=3 // this is an integer (32 bits)
  a1=4 //ok
  // a1=4.3 //ko because type change!
  println(a1) // will produce 4
  println(a2)// will produce 0

  val a2=3 // this is an integer (32 bits)
  //a2=4 //ko because a2 is a constant
  // a1=4.3 //ko because a2 is a constant

  def a3=1
  // a3=1 //KO because a3 is a FUNCTION (we will see further functions later
          // it will be evaluated to 1 (here) every call
  println(a3+1)// will produce 2

  def a4=Math.random
  val a5=Math.random()
  // a3=1 //KO because a3 is a FUNCTION (we will see further functions later
          // it will be evaluated to 1 (here) every call
  println(a3) // will produce 1
  println(a4) // will produce different value each call
  println(a4) // will produce different value each call
  println(a5) // will produce new value
  println(a5) // will produce same value
  println(a5) // will produce same value

}
