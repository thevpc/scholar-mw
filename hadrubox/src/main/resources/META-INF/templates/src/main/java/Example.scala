package ${{packageName(ProjectGroup)}}

import net.vpc.scholar.hadrumaths.Maths

import net.vpc.scholar.hadrumaths.MathScala._
import net.vpc.scholar.hadruwaves._
import net.vpc.scholar.hadruwaves.Physics._
import net.vpc.scholar.hadruplot._

object ${{className(ObjectName)}} {
  def main(args: Array[String]): Unit = {
    Plot.plot(Array(1,2,3,4))
    Plot.plot(Sin(X)*II())

  }
}
