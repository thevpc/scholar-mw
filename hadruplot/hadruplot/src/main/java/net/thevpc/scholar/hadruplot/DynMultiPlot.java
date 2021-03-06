/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruplot;

import net.thevpc.common.swing.tab.JDraggableTabbedPane;
import net.thevpc.common.swing.SwingUtilities3;
import net.thevpc.common.util.ArrayUtils;
import net.thevpc.scholar.hadruplot.console.PlotConfigManager;

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
public class DynMultiPlot {

    private String ytitle;
    private String xtitle;
    private Map<Object, ArrayList<Object>> valuesMap = new HashMap<Object, ArrayList<Object>>();
    private JPanel valuesPanel;
    private WindowManager windowManager;
    private boolean added = false;

    public static interface WindowManager {

        void addComponent(String ytitle, Component comp);

        void dataChanged();
    }

    public static class DefaultWindowManager implements WindowManager {

        private String title;
        private JFrame frame;
        private JDraggableTabbedPane jtp;

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
                jtp = new JDraggableTabbedPane();
                frame.add(jtp);
                frame.setMinimumSize(new Dimension(600, 400));
                frame.pack();
                frame.setVisible(true);
            }
            return frame;
        }
    }

    public DynMultiPlot(String xtitle, String ytitle, WindowManager wm) {
        this.ytitle = ytitle;
        this.xtitle = xtitle;
        this.windowManager = wm;

    }

    public Component getComponent() {
        if (valuesPanel == null) {
            valuesPanel = new JPanel(new BorderLayout());
        }
        return valuesPanel;
    }

    public void progress(Object type, Object value) {
        if (!added) {
            added = true;
            if (windowManager == null) {
                windowManager = new DefaultWindowManager(ytitle);
            }
            windowManager.addComponent(ytitle, getComponent());
        }
        //System.out.println(convergenceParameters.getLabel() + "[" + type + "]["+title+"]; err = " + err + "; threshold=" + threshold + "; val=" + value + "; pars=" + parameters);
        ArrayList<Object> values = valuesMap.get(type);
        if (values == null) {
            values = new ArrayList<Object>();
            valuesMap.put(type, values);
        }
        values.add(value);
        final Object[] all = valuesMap.keySet().toArray(new Object[0]);
        Arrays.sort(all);
        int max = 0;
        for (int i = 0; i < all.length; i++) {
            max = Math.max(max, valuesMap.get(all[i]).size());
        }


        final double[] x0 = ArrayUtils.dsteps(0.0, max - 1, 1);
//        final double[][] x = new double[all.length][];
        final double[][] dblValues = new double[all.length][max];

        for (int j = 0; j < all.length; j++) {
//            x[j] = x0;
            values = valuesMap.get(all[j]);
            for (int i = 0; i < dblValues[j].length; i++) {
                Object o = i < values.size() ? values.get(i) : 0;
                dblValues[j][i] = PlotConfigManager.Numbers.toDouble(o);
            }
        }

        try {
            SwingUtilities3.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    Container p = (Container) getComponent();
                    valuesPanel.removeAll();
                    String[] titles = new String[all.length];
                    for (int i = 0; i < titles.length; i++) {
                        titles[i] = String.valueOf(all[i]);
                    }
                    valuesPanel.add(Plot.asCurve().title(ytitle).titles(titles).xname(xtitle).yname(ytitle).samples(PlotSamples.absolute(x0)).plot(dblValues).toComponent());
                    //valuesPanel.invalidate();
                    //getFrame().getContentPane().invalidate();
                    //valuesPanel.repaint();
                    windowManager.dataChanged();
                }
            });

        } catch (Exception ex) {
            Logger.getLogger(DynMultiPlot.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }



    public void setWindowManager(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    private final static Object[] Z_ARR = new Object[0];

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

}
