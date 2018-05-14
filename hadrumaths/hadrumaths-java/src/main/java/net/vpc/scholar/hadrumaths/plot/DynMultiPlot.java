/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.SwingUtils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author vpc
 */
public class DynMultiPlot  {

    private String ytitle;
    private String xtitle;
    private Map<Object, ArrayList<Object>> valuesMap = new HashMap<Object, ArrayList<Object>>();
    private JPanel valuesPanel;
    private WindowManager windowManager;
    private boolean added=false;

    public static interface WindowManager {

        void addComponent(String ytitle, Component comp);

        void dataChanged();
    }

    public static class DefaultWindowManager implements WindowManager {

        private String title;
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

    public DynMultiPlot(String xtitle,String ytitle,WindowManager wm) {
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
        if(!added){
            added=true;
            if(windowManager==null){
                windowManager=new DefaultWindowManager(ytitle);
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
        final Object[] all = valuesMap.keySet().toArray(new Object[valuesMap.size()]);
        Arrays.sort(all);
        int max = 0;
        for (int i = 0; i < all.length; i++) {
            max = Maths.max(max, valuesMap.get(all[i]).size());
        }


        double[] x0 = Maths.dsteps(0.0, max - 1, 1);
//        final double[][] x = new double[all.length][];
        final double[][] dblValues = new double[all.length][max];

        for (int j = 0; j < all.length; j++) {
//            x[j] = x0;
            values = valuesMap.get(all[j]);
            for (int i = 0; i < dblValues[j].length; i++) {
                Object o = i < values.size() ? values.get(i) : 0;
                dblValues[j][i] = toDouble(o);
            }
        }

        try {
            SwingUtils.invokeAndWait(new Runnable() {

                        @Override
            public void run() {
                            Container p = (Container) getComponent();
                            valuesPanel.removeAll();
                            String[] titles=new String[all.length];
                            for(int i=0;i<titles.length;i++){
                                titles[i]=String.valueOf(all[i]);
                            }
                            valuesPanel.add(Plot.asCurve().title(ytitle).titles(titles).xname(xtitle).yname(ytitle).samples(Samples.absolute(x0)).plot(dblValues).toComponent());
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
    public double toDouble(Object o){
        double d=0;
        if(o == null){
            d=0;
        }else if(o instanceof Complex){
            d=((Complex) o).absdbl();
        } else if(o instanceof Matrix){
            d=((Matrix) o).norm1();
        }else {
            d = ((Number) o).doubleValue();
        }
        if(Double.isNaN(d)){
            d=0;
        }
        return d;
    }

    public void setWindowManager(WindowManager windowManager) {
        this.windowManager = windowManager;
    }
    private final static Object[] Z_ARR=new Object[0];
    public Object[] getValues(String id){
      ArrayList<Object> a=getValuesList(id);
      return a==null?Z_ARR : a.toArray();
    }
    
    public ArrayList<Object> getValuesList(String id){
        ArrayList<Object> values = valuesMap.get(id);
        if (values == null) {
            values = new ArrayList<Object>();
            valuesMap.put(id, values);
        }
        return values;
    }
    
}
