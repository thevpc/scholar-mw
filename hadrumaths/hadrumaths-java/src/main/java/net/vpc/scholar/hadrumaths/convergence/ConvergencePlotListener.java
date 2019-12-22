/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.convergence;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.MathsBase;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadruplot.Plot;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author vpc
 */
public class ConvergencePlotListener implements ConvergenceListener {

    private String title;
    private JFrame frame;
    private ArrayList<Object> values = new ArrayList<Object>();
    private ArrayList<Double> errors = new ArrayList<Double>();
    private JPanel valuesPanel;
    private JPanel errorsPanel;

    public ConvergencePlotListener(String title) {
        this.title = title;
    }

    @Override
    public void progress(ConvergenceResult result) {
        System.out.println(result.getLabel() + ": err = " + result.getRelativeError()
                + "; threshold=" + result.getStabilityThreshold() + "; val=" + result.getValue()
                + "; pars=" + result.getParameters());
        errors.add(result.getRelativeError() / result.getConfig().getThreshold());
        values.add(result.getValue());
        getFrame();
        valuesPanel.removeAll();
        errorsPanel.removeAll();

        double[] x = MathsBase.dsteps(0.0, errors.size(), 1);
        double[] dblValues = new double[values.size()];
        double[] dblErrors = new double[values.size()];
        for (int i = 0; i < dblValues.length; i++) {
            Object o = values.get(i);
            dblValues[i] = (o instanceof Complex) ? ((Complex) o).absdbl() : (o instanceof ComplexMatrix) ? ((ComplexMatrix) o).norm1() : ((Number) o).doubleValue();
            dblErrors[i] = errors.get(i);
        }

        valuesPanel.add(Plot.asCurve().nodisplay().title("Values").xname("iter").yname("val").xsamples(x).plot(new double[][]{dblValues}).toComponent());
        errorsPanel.add(Plot.asCurve().nodisplay().title("Values").xname("iter").yname("val").xsamples(x).plot(new double[][]{dblErrors}).toComponent());
        valuesPanel.invalidate();
        errorsPanel.invalidate();
        getFrame().getContentPane().repaint();
    }

    public JFrame getFrame() {
        if (frame == null) {
            frame = new JFrame(title);
            JTabbedPane jtp = new JTabbedPane();
            jtp.addTab("Values", valuesPanel = new JPanel(new BorderLayout()));
            jtp.addTab("Error", errorsPanel = new JPanel(new BorderLayout()));
            frame.add(jtp);
            frame.setMinimumSize(new Dimension(600, 400));
            frame.pack();
            frame.setVisible(true);
        }
        return frame;
    }
}
