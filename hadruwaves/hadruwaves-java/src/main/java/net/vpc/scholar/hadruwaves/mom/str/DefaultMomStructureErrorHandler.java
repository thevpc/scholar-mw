/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadruwaves.mom.str;

import net.vpc.common.util.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.plot.ExpressionsPlotModel;
import net.vpc.scholar.hadrumaths.plot.ExpressionsPlotPanel;
import net.vpc.scholar.hadrumaths.plot.PlotComponent;
import net.vpc.scholar.hadrumaths.plot.PlotType;
import net.vpc.scholar.hadrumaths.plot.console.PlotConsole;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpAdaptiveMesh;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.PolygonPlot;
import net.vpc.scholar.hadruwaves.str.MWStructure;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Set;

/**
 * @author vpc
 */
public class DefaultMomStructureErrorHandler implements MWStructureErrorHandler {

    @Override
    public void showError(String title, Throwable e, Matrix m, MWStructure structure) {
        if (PlotConsole.debugFramesCount > 3) {
            return;
        }
        Set<ExternalLibrary> preferredLibraries = null;
        MomStructure str = (MomStructure) structure;
        JTextArea a = new JTextArea(str.dump());
        TMatrix<Complex> sp = str.getTestModeScalarProducts(ProgressMonitorFactory.none());
        PlotComponent aplot = Plot.nodisplay().asMatrix().title("Matrix A")
                .plot(str.matrixA().computeMatrix());
        PlotComponent bplot = Plot.nodisplay().asMatrix().title("Matrix B")
                .plot(str.matrixB().computeMatrix());
        PlotComponent gfplot = Plot.nodisplay().asMatrix().title("<f,g>").plot(sp);

        JComponent gplot = Plot.create(
                new ExpressionsPlotModel()
                        .setExpressions(str.getTestFunctions().arr())
                        .setPlotType(PlotType.CURVE)
                        .setPreferredLibraries(null)
                        .setTitle("gp")
                , Plot.getDefaultWindowManager()).toComponent();

        JComponent fplot = Plot.create(
                new ExpressionsPlotModel()
                        .setExpressions(str.getModeFunctions().arr())
                        .setPlotType(PlotType.CURVE)
                        .setPreferredLibraries(null)
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
        net.vpc.scholar.hadruwaves.mom.TestFunctions gtf = str.getTestFunctions();
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
