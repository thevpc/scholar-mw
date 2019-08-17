package net.vpc.scholar.mentoring.ch01_scala

object T04_ScalaStructures extends App{
 var a=1;

  //conditional
  if(a<=3){
    println("sure")
  }else if(a<2){
    println("never called")
  }else {
    println("really? a=3!!")
  }

  // conditional expression
  var b= if (a<=3)  10 else 15 // b =10 for this example, { and } are not mandatory

  var i=8;
  while(i>0){
    println("hello")
    i=i-1 // remember i++ and i- are not allowed in scala
  }

  for ( j <- 1 to 30 ) {
    println(j)
  }

  //loop over array
  var ar=Array(1,2,3)

  for ( j <- 0 to ar.length-1 ) {
    println(ar(j))
  }

  //loop over array
  ar.foreach(v=>println(v))
  var s=0;
  for(j<-0 to ar.length-1){
    s=s+ar(j)
  }
  println(s);
  s=0
  ar. foreach(x=>s=s+x)
  println(s)

}
