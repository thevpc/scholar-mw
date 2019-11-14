package net.vpc.scholar.mentoring.ch01_scala

object T05_ScalaLists extends App {
  //linked lists
  var a = List(
    "alia",
    "hammadi",
  )
  var b = List(
    "hammadi",
    "belleaid",
  )
  //for(x:a)
  for (x <- a) {
    println(x)
  }
  println(a.length)
  println(a(0))

  a.foreach(x => println(x))
  //concatenate
  var c = a ++ b
  println(c)
}
