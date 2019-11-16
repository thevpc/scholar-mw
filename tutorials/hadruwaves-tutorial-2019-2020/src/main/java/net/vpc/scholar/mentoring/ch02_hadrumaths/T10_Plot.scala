package net.vpc.scholar.mentoring.ch02_hadrumaths

import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths.MathScala._

object T10_Plot {

  def main(args: Array[String]): Unit = {

    Plot.plot(sin(X))
    Plot.title("My title").asArea().plot(sin(X))

    val c = Plot.console()
    c.Plot.plot(sin(X));

    Plot.cd("/Folder/SubFolder").title("My title 1").asArea().plot(sin(X))
    Plot.cd("/Folder/SubFolder").title("My title 2").asArea().plot(cos(X))
  }
}
