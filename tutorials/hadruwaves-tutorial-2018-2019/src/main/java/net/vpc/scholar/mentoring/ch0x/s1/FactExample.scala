package net.vpc.scholar.mentoring.ch0x.s1

/**
  * Created by vpc on 5/27/17.
  */
object FactExample extends App {
  val f0:Array[Double]=Array.ofDim(10002)
  var b0:Array[Boolean]=Array.ofDim(10002)
  def f(x:Double,n:Int) : Double= {
    //  java : x= y/2
    //  java : x= ((double)y)/2
    //  scala : x= y/2
    //  scala : x= y.asInstanceOf[Double]/2
    if(!b0(n)) {
      println("calcul de " + x + ";" + n)
      f0(n)=if (n == 0) x else
        -f(x, n - 1) * x * x / (2 * n + 2) / (2 * n + 3)
      b0(n)=true
    }
    f0(n)
  }

  def f2(x:Double,n:Int) : Double= {
    println("calcul de " + x + ";" + n)
    if (n == 0) x else
      -f2(x, n - 1) * x * x / (2 * n + 2) / (2 * n + 3)
  }

  private val start = System.currentTimeMillis()
  var c=0;
  // Il n'ya pas de for
  while (c<1000) {
    println(f(2, c))
    c+=1
  }
  println(System.currentTimeMillis()-start)

}
