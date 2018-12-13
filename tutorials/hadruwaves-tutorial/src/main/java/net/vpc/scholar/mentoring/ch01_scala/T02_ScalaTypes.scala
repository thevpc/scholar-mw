package net.vpc.scholar.mentoring.ch01_scala

object T02_ScalaTypes extends App{
  var a1=3 // this is an integer (32 bits)
  var a2=3.0 // this is a double (64 bits)
  var a3='3' // this is a character (16bits)
  var a4=true // this is a boolean
  var a5=3.0F // this is a float (32bits)
  var a6=300L // this is a long (64bits)

  //you still can declare explicitly var types
  var d_a1 : Int=3 // this is an integer (32 bits)
  var d_a2 : Double=3 // this is a double (64 bits)
  var d_a3 : Character='3' // this is a character (16bits)
  var d_a4 : Boolean=true // this is a boolean
  var d_a5 : Float=3.0F // this is a float (32bits)
  var d_a6 : Long=300L // this is a long (64bits)

  //standard operations are allowed (as in Java)
  //standard Conversion are applied (int+double=double)
  var expr  =a1+d_a2  // the result is 'Double" because d_a2 is of Double type

  //-------------------------------
  // ARRAYS
  // int array of size 5 initialized with 1, ...5 values
  var b1 : Array[Int] =Array(1,2,3,4,5)
  println(b1(0)) // prints first element
  println(b1.length) // prints 5, le array length
  // b1(0)++  not allowed
  b1(0)=b1(0)+1 //increments the first element

  //-------------------------------
  // MULTI DIMENTIONAL ARRAYS
  // int array of size 5 initialized with 1, ...5 values
  var b2 : Array[Array[Double]] =Array.ofDim[Double](2,3)
  println(b2(0)(0)) // prints first element
  b2(0)(0)=3.8
  println(b2(0)(0))
  println(b2.length) // prints 2, le array length of the first dim
  println(b2(0).length) // prints 2, le array length of the second dim
}
