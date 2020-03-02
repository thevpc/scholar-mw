package net.vpc.scholar.mentoring.ch02_hadrumaths

import net.vpc.scholar.hadrumaths._
import net.vpc.scholar.hadrumaths.MathScala._

object T12_CustomFunctions {
  def main(args: Array[String]): Unit = {
    val dom = domain(0.0 -> 2 * PI, 0.0 -> 2 * PI)

    var DDX = define("DDX", (x: Double) => x * x)
    var CCX = define("CCX", (x: Complex) => sin(x) * (x - 1))
    var DCX = define("DCX", (x: Double) => î * sin(x) + (x - 1))

    var DDXY = define("DDXY", (x: Double, y: Double) => x * y)
    var CCXY = define("CCXY", (x: Complex, y: Complex) => sin(x) * (y - 1))
    var DCXY = define("DCXY", (x: Double, y: Double) => î * sin(x) + (y - 1))

    val vv = DDXY.evalDouble(2, 3)

    val console = Plot.console()
    console.Plot.title("DDX").asHeatMap().plot(DDX * dom) //, CCX, DCX
    console.Plot.title("CCX").asHeatMap().plot(CCX * dom) //, CCX, DCX
    console.Plot.title("DCX").asHeatMap().plot(DCX * dom) //, CCX, DCX
    console.Plot.title("CCXY").asHeatMap().plot(CCXY * dom) //, CCX, DCX
    console.Plot.title("DCXY").asHeatMap().plot(DCXY * dom) //, CCX, DCX

    console.Plot.title("DDXY").asHeatMap().plot(DDXY * dom) //, CCX, DCX
    console.Plot.title("DDXY.composeX").asHeatMap().plot(DDXY.compose(X+Y) * dom)
    console.Plot.title("DDXY.composeXY").asHeatMap().plot(DDXY.compose(X + Y) * dom)
    console.Plot.title("CCXY").asHeatMap().plot(CCXY * dom)
    console.Plot.title("DCXY").asHeatMap().plot(DCXY * dom)


    console.Plot.title("DDX+DDXY").asHeatMap().plot(DDX * dom,DDXY*dom) //, CCX, DCX
  }
}
