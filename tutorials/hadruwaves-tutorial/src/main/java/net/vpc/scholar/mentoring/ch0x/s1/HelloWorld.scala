package net.vpc.scholar.mentoring.ch0x.s1

import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths._

/**
  * This is a hello world example in scholar math
  * Created by vpc on 4/25/17.
  */
object HelloWorld extends App {
  println("Hello world")
  val f = cos(X) * cos(Y) * domain(0.0 -> 2 * PI, 0.0 -> 2 * PI)
  Plot.console().plotter().title("Mon Titre").asPolar().asAbs().plot(f)
}
