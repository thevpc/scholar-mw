package net.vpc.scholar.mentoring.ch01_scala

object T06_ScalaSets extends App {
  //linked lists
  var a = Set(
    "alia",
    "hammadi",
    "hammadi",
  )
  var b = Set(
    "hammadi",
    "belleaid",
  )

  for (x <- a) {
    println(x)
  }
  println(a.size)
  //ERROR
  //    println(a(0))

  a.foreach(x => println(x))
  //concatenate
  var c = a ++ b
  println(c)

  println("intersect" ,a intersect b)
  println("union",a.union(b))
  println("diff",a.diff(b))

  //conversion
  println(a.toList.toSet)
}
