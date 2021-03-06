package net.thevpc.scholar.mentoring.ch02_hadrumaths

//these are Main Mandatory Imports

import net.thevpc.scholar.hadrumaths.MathScala._
import net.thevpc.scholar.hadrumaths._

object T05_Vector extends App {
  var cv = columnVector(10, (i: Int) => 0 * î + 3 * i)
  var cv2 = columnVector(10, (i: Int) => complex(3 * i))
  println(cv2(3))
  var cv3 = cv + cv2
  //  var cv3=cv+cv2
  var s1 = csum(cv2);
  var s1u = cv2.stream().reduce((x: Complex, y: Complex) => x + y);
  var s2 = cv2.sum;
  var s3 = cv2.prod();
  var s4 = csum(1, 2, 5, 4, 7)
  var s5 = csum(10, (x: Int) => complex(x + 1))
}
