package net.vpc.scholar.mentoring.ch0x.s1

import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadruplot.Plot

/**
  * This is a hello world example in scholar math
  * Created by vpc on 4/25/17.
  */
object HelloWorld extends App {
  println("Hello world")
  val f = cos(X) * cos(Y) * II(0.0 -> 2 * PI, 0.0 -> 2 * PI)
  Plot.title("Mon Titre").asPolar().asAbs().plot(f)
  Plot.plot(f)
}
