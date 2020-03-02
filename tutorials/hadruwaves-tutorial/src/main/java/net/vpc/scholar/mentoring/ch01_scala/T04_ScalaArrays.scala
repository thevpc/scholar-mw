package net.vpc.scholar.mentoring.ch01_scala

object T04_ScalaArrays extends App{
  //create an array
  var a=Array(1,2,3)
  var b=Array(3,4,5)

  //0 to a.length-1
  var ss="toz"

  println(ss.substring(2))
  println(ss substring 2)

  for ( j <- 0 to a.length-1) {
    //get array primitiveElement3D by index (zero based)
    println(a(j))
  }

  var s=0;
  a.foreach(x=>s=s+x)

  var ab=a ++ b
  println(ab)
  println(java.util.Arrays.toString(ab))
  println(ab.mkString(","))
  println(ab mkString ";")

  ////////////////////

  var m=Array(Array(1,2,3),Array(4,5,6),Array(7,8,9))
  m.foreach(row=>{
    row.foreach(cell=>print("," +cell))
    print("\n")
  })
}
