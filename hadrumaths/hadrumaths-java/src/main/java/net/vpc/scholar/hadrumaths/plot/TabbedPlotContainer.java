/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.plot;

import javax.swing.*;
import java.awt.*;

/**
 * @author vpc
 */
public class TabbedPlotContainer extends AbstractPlotContainer {

    private JTabbedPane jTabbedPane;

    public TabbedPlotContainer() {
        super();
        setPlotWindowContainerFactory(PanelPlotWindowContainerFactory.INSTANCE);
    }


    public int indexOfPlotComponent(PlotComponent plotComponent) {
        if (jTabbedPane != null) {
            int tabCount = jTabbedPane.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                Component ii = jTabbedPane.getComponentAt(i);
                if (ii != null && plotComponent.toComponent() == ii) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public PlotComponent getPlotComponent(int index) {
        if (jTabbedPane != null) {
            Component c = jTabbedPane.getComponentAt(index);
            if (c instanceof JComponent) {
                return toPlotComponent((JComponent) c);
            }
        }
        return null;
    }

    @Override
    public PlotContainer add(int index, String containerName) {
        if (index >= 0) {
            PlotComponent component = getPlotComponent(index);
            PlotContainer t = getPlotWindowContainerFactory().create();
            t.setPlotTitle(containerName);
            t.setPlotWindowManager(getPlotWindowManager());
            jTabbedPane.setTabComponentAt(index, toComponent(t));
            if (component != null) {
                t.add(component);
            }
            return t;
        }
        return null;
    }

    @Override
    public int getPlotComponentsCount() {
        if (jTabbedPane != null) {
            return jTabbedPane.getTabCount();
        }
        return 0;
    }

    @Override
    public void removePlotComponentImpl(PlotComponent component) {
        int index = indexOfPlotComponent(component);
        jTabbedPane.removeTabAt(index);
    }

    public void addPlotComponentImpl(PlotComponent component) {
        JComponent component1 = toComponent(component);
        jTabbedPane.addTab(validateTitle(component.getPlotTitle()), component1);
    }

//    private String id(Object o) {
//        if (o instanceof PlotComponent) {
//            return o.getClass().getSimpleName() + "@" + System.identityHashCode(o);
//        } else if (o instanceof JComponent) {
//            PlotComponent cc = (PlotComponent) ((JComponent) o).getClientProperty(PlotComponent.class.getName());
//            return id(cc);
//        } else {
//            throw new IllegalArgumentException("Unsupported");
//        }
//    }

    public void clear() {
        if (jTabbedPane != null) {
            Component[] components = jTabbedPane.getComponents();
            for (Component component : components) {
                if (component instanceof JComponent) {
                    PlotComponent t = toPlotComponent((JComponent) component);
                    remove(t);
                }
            }
            jTabbedPane.removeAll();
        }
    }

    @Override
    public JComponent toComponent() {
        if (jTabbedPane == null) {
            jTabbedPane = new JTabbedPane();
            jTabbedPane.putClientProperty(PlotComponent.class.getName(), this);
            jTabbedPane.setPreferredSize(new Dimension(600, 400));
        }
        return jTabbedPane;
    }

}
