package net.vpc.scholar.mentoring.ch02_hadrumaths

//these are Main Mandatory Imports
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadruplot.Plot
import net.vpc.scholar.hadruwaves._
import net.vpc.scholar.hadruwaves.Physics._

object T03_Matrix extends App{
  var m=matrix(
    3, (i:Int,j:Int) => i+ î*j
  )
  println(m)
  Plot.plot(m)
  println(m(1,2))
  println(m.det())
  println(det(m))
  println(inv(m))
  println(m*m)
//  println(m**2)


  var cv=columnVector(10,(i:Int)=>0*î +3*i)
  var cv2=columnVector(10,(i:Int)=>complex(3*i))
  println(cv2(3))
  var cv3=cv+cv2
//  var cv3=cv+cv2
  var s1=csum(cv2);
  var s2=cv2.sum;
  var s3=cv2.prod();
  var s4=csum(1,2,5,4,7)
  var s5=csum(10,(x:Int)=>complex(x+1))
}
