package net.vpc.scholar.mentoring.ch01_scala

object T04_ScalaDataFlows extends App{
 var a=1;

  //conditional
  if(a<=3){
    println("sure")
  }else if(a<2){
    println("never called")
  }else {
    println("really? a=3!!")
  }

  if(a<=3) println("sure")
  else if(a<2) println("never called")
  else println("really? a=3!!")


  // conditional expression
  var b= if (a<=3)  10 else 15 // b =10 for this example, { and } are not mandatory
  //b=a<=3?10:15
  var i=8;
  while(i>0){
    println("hello")
    i=i-1 // remember i++ and i-- are not allowed in scala
  }
  println(1 to 30)
  println(1.to(30))

  for ( j <- 1 to 30 ) {
    println(j)
  }

}
