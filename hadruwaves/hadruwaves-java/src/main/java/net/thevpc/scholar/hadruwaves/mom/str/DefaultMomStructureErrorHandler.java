/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadruwaves.mom.str;

import net.thevpc.scholar.hadrumaths.plot.model.ExpressionsPlotModel;
import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.plot.*;
import net.thevpc.scholar.hadruplot.console.PlotConsole;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruplot.PlotComponent;
import net.thevpc.scholar.hadruplot.PlotType;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpAdaptiveMesh;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.PolygonPlot;
import net.thevpc.scholar.hadruwaves.str.MWStructure;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import net.thevpc.scholar.hadruplot.LibraryPlotType;

/**
 * @author vpc
 */
public class DefaultMomStructureErrorHandler implements MWStructureErrorHandler {

    @Override
    public void showError(String title, Throwable e, ComplexMatrix m, MWStructure structure) {
        if (PlotConsole.debugFramesCount > 3) {
            return;
        }
        MomStructure str = (MomStructure) structure;
        JTextArea a = new JTextArea(str.dump());
        ComplexMatrix sp = str.getTestModeScalarProducts(ProgressMonitors.none());
        PlotComponent aplot = Plot.nodisplay().asMatrix().title("Matrix A")
                .plot(str.matrixA().evalMatrix());
        PlotComponent bplot = Plot.nodisplay().asMatrix().title("Matrix B")
                .plot(str.matrixB().evalMatrix());
        PlotComponent gfplot = Plot.nodisplay().asMatrix().title("<f,g>").plot(sp);

        JComponent gplot = Plot.create(
                new ExpressionsPlotModel()
                        .setExpressions(str.testFunctions().arr())
                        .setPlotType(new LibraryPlotType(PlotType.CURVE))
                        .setTitle("gp")
                , Plot.getDefaultWindowManager()).toComponent();

        JComponent fplot = Plot.create(
                new ExpressionsPlotModel()
                        .setExpressions(str.modeFunctions().arr())
                        .setPlotType(new LibraryPlotType(PlotType.CURVE))
                        .setTitle("fn")
                , Plot.getDefaultWindowManager()).toComponent();

        JFrame f = new JFrame(title + " : " + e.toString());
        f.addWindowListener(new WindowListener() {

            public void windowOpened(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                PlotConsole.debugFramesCount--;
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowActivated(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }
        });
        JTabbedPane p = new JTabbedPane();
        p.addTab("A", aplot.toComponent());
        p.addTab("B", bplot.toComponent());
        p.addTab("<f,g>", gfplot.toComponent());
        p.addTab("fn", fplot);
        p.addTab("gn", gplot);
        p.addTab("dump", new JScrollPane(a));
        net.thevpc.scholar.hadruwaves.mom.TestFunctions gtf = str.testFunctions();
        if (gtf instanceof GpAdaptiveMesh) {
            GpAdaptiveMesh gam = (GpAdaptiveMesh) gtf;
            p.addTab("str", new PolygonPlot(gam.getPolygons(str.getCircuitType()), gam.getMeshAlgo(), gam.getPattern(), str.getDomain()));
        }
        if (m != null) {
            p.addTab("Matrix", Plot.nodisplay().title("Matrix").plot(m.getArray()).toComponent());
        }
        f.getContentPane().add(p);
        f.pack();
        f.setVisible(true);
        PlotConsole.debugFramesCount++;
    }

}
