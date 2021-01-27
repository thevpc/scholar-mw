package ${{packageName(ProjectGroup)}}

import net.thevpc.scholar.hadrumaths.Maths

import net.thevpc.scholar.hadrumaths.MathScala._
import net.thevpc.scholar.hadruwaves._
import net.thevpc.scholar.hadruwaves.Physics._
import net.thevpc.scholar.hadruplot._

object ${{className(ObjectName)}} {
  def main(args: Array[String]): Unit = {
    Plot.plot(Array(1,2,3,4))
    Plot.plot(Sin(X)*II())

  }
}
