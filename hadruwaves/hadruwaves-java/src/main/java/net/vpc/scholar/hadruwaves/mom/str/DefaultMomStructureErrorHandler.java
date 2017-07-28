/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadruwaves.mom.str;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Set;
import javax.swing.*;

import net.vpc.scholar.hadrumaths.ComputationMonitorFactory;
import net.vpc.scholar.hadrumaths.ExternalLibrary;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.plot.ExpressionsPlotPanel;
import net.vpc.scholar.hadrumaths.Plot;
import net.vpc.scholar.hadrumaths.plot.ExpressionsPlotModel;
import net.vpc.scholar.hadrumaths.plot.PlotComponent;
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductCache;
import net.vpc.scholar.hadrumaths.plot.console.PlotConsole;
import net.vpc.scholar.hadruwaves.str.MWStructure;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpAdaptiveMesh;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.PolygonPlot;

/**
 *
 * @author vpc
 */
public class DefaultMomStructureErrorHandler implements MWStructureErrorHandler {

    @Override
    public void showError(String title, Throwable e, Matrix m, MWStructure structure) {
        if (PlotConsole.debugFramesCount > 3) {
            return;
        }
        Set<ExternalLibrary> preferredLibraries=null;
        MomStructure str=(MomStructure) structure;
        JTextArea a = new JTextArea(str.dump());
        ScalarProductCache sp = str.getTestModeScalarProducts(ComputationMonitorFactory.none());
        PlotComponent aplot = Plot.nodisplay().asSurface().title("Matrix A")
                .plot(str.matrixA().computeMatrix());
        PlotComponent bplot = Plot.nodisplay().asSurface().title("Matrix B")
                .plot(str.matrixB().computeMatrix());
        PlotComponent gfplot = Plot.nodisplay().asSurface().title("<f,g>").plot(sp.toMatrix());

        JComponent gplot = Plot.create(
                new ExpressionsPlotModel()
                .setTitle("gp").setExpressions(str.getTestFunctions().arr())
                .setShowType(ExpressionsPlotPanel.ShowType.CURVE_FX)
                .setPreferredLibraries(null)
                ,Plot.getDefaultWindowManager()).toComponent();

        JComponent fplot = Plot.create(
                new ExpressionsPlotModel()
                .setTitle("fn").setExpressions(str.getModeFunctions().arr())
                .setShowType(ExpressionsPlotPanel.ShowType.CURVE_FX)
                .setPreferredLibraries(null)
                ,Plot.getDefaultWindowManager()).toComponent();

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
            p.addTab("str", new PolygonPlot(gam.getPolygons(str.getCircuitType()), gam.getMeshAlgo(),gam.getPattern(),str.getDomain()));
        }
        if (m != null) {
            p.addTab("Matrice", Plot.nodisplay().title("Matrix").plot(m.getArray()).toComponent());
        }
        f.getContentPane().add(p);
        f.pack();
        f.setVisible(true);
        PlotConsole.debugFramesCount++;
    }

}
