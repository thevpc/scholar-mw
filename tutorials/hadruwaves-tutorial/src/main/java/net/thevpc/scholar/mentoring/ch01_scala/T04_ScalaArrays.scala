package net.thevpc.scholar.mentoring.ch01_scala

object T04_ScalaArrays extends App{
  //create an array
  var a=Array(1,2,3)
  var b=Array(3,4,5)

  //0 to a.length-1
  var ss="toz"

//  a.b.c.d(1,2,6)
//  a b c d (1,2,6)
//  a b c d 1

  println(ss.substring(2))
  println(ss substring 2)

  //0.to(a.length-1)
  for ( j <- 0 to a.length-1) {
    //get array primitiveElement3D by index (zero based)
    println(a(j))
  }

  var s=0;
  // lambda expression
//  a.map(x=>x*2).foreach(x=>s=s+x)
  a.foreach(x=>s=s+x)

  var ab=a ++ b
  println(ab)
  println(java.util.Arrays.toString(ab))
  println(ab.mkString(",toz,"))
  println(ab mkString ";")

  ////////////////////

  var m=Array(Array(1,2,3),Array(4,5,6),Array(7,8,9))
  m.foreach(row=>{
    row.foreach(cell=>print("," +cell))
    print("\n")
  })
}
