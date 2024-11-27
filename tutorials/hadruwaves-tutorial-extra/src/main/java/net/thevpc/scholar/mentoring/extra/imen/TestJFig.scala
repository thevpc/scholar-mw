package net.thevpc.scholar.mentoring.extra.imen

import net.thevpc.scholar.hadruplot.{Plot, PlotComponent}

import java.io.File

object TestJFig extends App{
  private val p: PlotComponent = Plot.loadPlot(new File("/data/tmp/ExRevision.jfig"))
  Plot.plot(p.getModel)
}
