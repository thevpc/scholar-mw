package net.thevpc.scholar.mentoring.ch02_hadrumaths

import net.thevpc.scholar.hadrumaths.MathScala._

object T11_Plot {

  def main(args: Array[String]): Unit = {

    Plot.plot(sin(X)*II(0.0->4*PI))
    Plot.title("My title").asArea().plot(sin(X)*II(0.0->4*PI))

    val c = Plot.console()
    c.Plot.plot(sin(X));

    Plot.cd("/Folder/SubFolder").title("My title 1").asArea().plot(sin(X)*II(0.0->4*PI))
    Plot.cd("/Folder/SubFolder").title("My title 2").asArea().plot(cos(X)*II(0.0->4*PI))
  }
}
