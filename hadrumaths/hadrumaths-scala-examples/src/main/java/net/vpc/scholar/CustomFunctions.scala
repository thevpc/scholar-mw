package net.vpc.scholar

import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadruplot.Plot

object CustomFunctions {
  def main(args: Array[String]): Unit = {
    val dom = domain(0.0 -> 2 * PI, 0.0 -> 2 * PI)

    var DDX=define("DDX",(x: Double)=>x*x)
    var CCX=define("CCX",(x: Complex)=>sin(x)*(x-1))
    var DCX=define("DCX",(x: Double)=>î*sin(x)+(x-1))

    var DDXY=define("DDXY",(x: Double,y: Double)=>x*y)
    var CCXY=define("CCXY",(x: Complex,y: Complex)=>sin(x)*(y-1))
    var DCXY=define("DCXY",(x: Double,y: Double)=>î*sin(x)+(y-1))

    Plot.title("DDXY").asHeatMap().plot(DDXY(X+Y,Y)*dom)
    Plot.title("CCXY").asHeatMap().plot(CCXY(X,Y)*dom)
    Plot.title("DCXY").asHeatMap().plot(DCXY(X,Y)*dom)

    val console = Plot.console()
//    console.newPlot().title("DDXY").asHeatMap().plot(DDXY(X+Y,Y)*dom)
//    console.newPlot().title("CCXY").asHeatMap().plot(CCXY(X,Y)*dom)
//    console.newPlot().title("DCXY").asHeatMap().plot(DCXY(X,Y)*dom)
  }
}
