package net.thevpc.scholar.mentoring.extra.imen

import net.thevpc.common.mon.{ProgressMonitor, ProgressMonitors}
import net.thevpc.scholar.hadrumaths.MathScala._
import net.thevpc.scholar.hadrumaths._
import net.thevpc.scholar.hadrumaths.cache.{CacheEvaluator, PersistenceCacheBuilder}
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector
import net.thevpc.scholar.hadrumaths.util.dump.Dumper
import net.thevpc.scholar.hadruwaves.Material
import net.thevpc.scholar.hadruwaves.mom._

object d_TestPlot {

  def main(args: Array[String]): Unit = {
      Plot.plot(sin(X*Y)*II(0.0->20*PI,0.0->PI))
  }



}
