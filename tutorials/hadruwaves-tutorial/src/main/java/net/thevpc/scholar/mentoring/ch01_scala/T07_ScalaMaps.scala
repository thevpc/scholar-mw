package net.thevpc.scholar.mentoring.ch01_scala

object T07_ScalaMaps extends App {
  //linked lists
  var a = Map(
    "alia"->12,
    "hammadi"->40
  )

//  Map<String,Integer> a=new HashMap<>();
//  a.put("alia",12);
//  a.put("hammadi",40);
//
  var a2 = List(
    "hammadi"->30,
    "belleaid"->35,
  )
  var b = Map(
    "hammadi"->30,
    "belleaid"->35,
  )

  for (x <- a) {
    println(x)
  }
  println(a.size)
  println(a("alia"))

  a.foreach(x => println(x))
  println("------------")
  a.foreach(x => println(x._1))
  a.foreach(x => println(x._2))
  //concatenate
  var c = a ++ b
  println(c)

  println(a.keySet)
  println(a.values)
  println(a.values.toList)
}
