package net.thevpc.scholar.mentoring.ch02_hadrumaths

import com.formdev.flatlaf.themes.FlatMacDarkLaf
import net.thevpc.scholar.hadrumaths.MathScala._
import net.thevpc.scholar.hadruplot.PlotUIManager

import javax.swing.JComponent

object T11_Plot {

  def main(args: Array[String]): Unit = {


//    PlotUIManager.set("FlatMacDark");
//    Plot.plot(sin(X)*II(0.0->4*PI))
    Plot.title("My title").domain(domain(0.0->10*PI,0.0->10*PI)).asArea().plot(sin(X)*cos(Y))

//    val c = Plot.console()
//    c.Plot.plot(sin(X));
//
//    Plot.cd("/Folder/SubFolder").title("My title 1").asArea().plot(sin(X)*II(0.0->4*PI))
//    Plot.cd("/Folder/SubFolder").title("My title 2").asArea().plot(cos(X)*II(0.0->4*PI))
//    Thread.sleep(5000);
//    println("hello")
//    Plot.getWindowManager.getRootContainer.toComponent.updateUI();
  }
}
