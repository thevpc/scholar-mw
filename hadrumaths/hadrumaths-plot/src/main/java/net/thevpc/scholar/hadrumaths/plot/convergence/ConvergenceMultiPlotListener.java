/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.plot.convergence;

import net.thevpc.common.swing.SwingUtilities3;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadruplot.Plot;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author vpc
 */
public class ConvergenceMultiPlotListener implements ConvergenceListener {

    private final static Object[] Z_ARR = new Object[0];
    private final String title;
    private final Map<String, ArrayList<Object>> valuesMap = new HashMap<String, ArrayList<Object>>();
    private JPanel valuesPanel;
    private WindowManager windowManager;
    private boolean added = false;

    public ConvergenceMultiPlotListener(String title) {
        this.title = title;
    }

    @Override
    public void progress(ConvergenceResult result) {
        if (!added) {
            added = true;
            if (windowManager == null) {
                windowManager = new DefaultWindowManager(title);
            }
            windowManager.addComponent(title, getComponent());
        }
        //System.out.println(convergenceConfig.getLabel() + "[" + type + "]["+title+"]; err = " + err + "; threshold=" + threshold + "; val=" + value + "; pars=" + parameters);
        ArrayList<Object> values = valuesMap.get(result.getLabel());
        if (values == null) {
            values = new ArrayList<Object>();
            valuesMap.put(result.getLabel(), values);
        }
        values.add(result.getValue());
        final String[] all = valuesMap.keySet().toArray(new String[0]);
        Arrays.sort(all);
        int max = 0;
        for (int i = 0; i < all.length; i++) {
            max = Math.max(max, valuesMap.get(all[i]).size());
        }


        double[] x0 = Maths.dsteps(0.0, max - 1, 1);
        final double[][] x = new double[all.length][];
        final double[][] dblValues = new double[all.length][max];

        for (int j = 0; j < all.length; j++) {
            x[j] = x0;
            values = valuesMap.get(all[j]);
            for (int i = 0; i < dblValues[j].length; i++) {
                Object o = i < values.size() ? values.get(i) : 0;
                dblValues[j][i] = toDouble(o);
            }
        }

        try {
            SwingUtilities3.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    Container p = (Container) getComponent();
                    valuesPanel.removeAll();
                    valuesPanel.add(Plot.title(title)
                            .xname("iteration")
                            .yname(title)
                            .titles(all)
                            .nodisplay()
                            .plot(x, dblValues).toComponent());
                    //valuesPanel.invalidate();
                    //getFrame().getContentPane().invalidate();
                    //valuesPanel.repaint();
                    windowManager.dataChanged();
                }
            });

        } catch (Exception ex) {
            Logger.getLogger(ConvergenceMultiPlotListener.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    public Component getComponent() {
        if (valuesPanel == null) {
            valuesPanel = new JPanel(new BorderLayout());
        }
        return valuesPanel;
    }

    public double toDouble(Object o) {
        return (o == null) ? 0 : (o instanceof Complex) ? ((Complex) o).absdbl() : (o instanceof ComplexMatrix) ? ((ComplexMatrix) o).norm1() : ((Number) o).doubleValue();
    }

    public void setWindowManager(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    public Object[] getValues(String id) {
        ArrayList<Object> a = getValuesList(id);
        return a == null ? Z_ARR : a.toArray();
    }

    public ArrayList<Object> getValuesList(String id) {
        ArrayList<Object> values = valuesMap.get(id);
        if (values == null) {
            values = new ArrayList<Object>();
            valuesMap.put(id, values);
        }
        return values;
    }

    public interface WindowManager {

        void addComponent(String title, Component comp);

        void dataChanged();
    }

    public static class DefaultWindowManager implements WindowManager {

        private final String title;
        private JFrame frame;
        private JTabbedPane jtp;

        public DefaultWindowManager(String title) {
            this.title = title;
        }


        @Override
        public void addComponent(String title, Component comp) {
            getFrame();
            jtp.addTab(title, comp);
        }

        @Override
        public void dataChanged() {
            getFrame().getContentPane().repaint();
        }

        public synchronized JFrame getFrame() {
            if (frame == null) {
                frame = new JFrame(title);
                jtp = new JTabbedPane();
                frame.add(jtp);
                frame.setMinimumSize(new Dimension(600, 400));
                frame.pack();
                frame.setVisible(true);
            }
            return frame;
        }
    }

}
