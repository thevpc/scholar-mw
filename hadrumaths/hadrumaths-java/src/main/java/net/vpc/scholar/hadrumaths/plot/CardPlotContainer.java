/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.plot;

import net.vpc.common.swings.JListCardPanel;

import javax.swing.*;
import java.awt.*;

/**
 * @author vpc
 */
public class CardPlotContainer extends AbstractPlotContainer {

    private JListCardPanel jTabbedPane;

    public CardPlotContainer() {
        super();
        setPlotWindowContainerFactory(TabbedPlotWindowContainerFactory.INSTANCE);
    }


    public int indexOfPlotComponent(PlotComponent plotComponent) {
        if (jTabbedPane != null) {
            int tabCount = jTabbedPane.getPageComponentsCount();
            for (int i = 0; i < tabCount; i++) {
                Component ii = jTabbedPane.getPageComponent(i);
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
            Component c = jTabbedPane.getPageComponent(index);
            if (c instanceof JComponent) {
                return toPlotComponent((JComponent) c);
            }
        }
        return null;
    }

    @Override
    public int getPlotComponentsCount() {
        if (jTabbedPane != null) {
            return jTabbedPane.getPageComponentsCount();
        }
        return 0;
    }

    @Override
    public void removePlotComponentImpl(PlotComponent component) {
        int index = indexOfPlotComponent(component);
        jTabbedPane.removePageAt(index);
    }

    public void addComponentImpl(PlotComponent component, int index) {
        JComponent component1 = toComponent(component);
        if (index < 0) {
            jTabbedPane.addPage(validateTitle(component.getPlotTitle()), validateTitle(component.getPlotTitle()), null, component1);
        } else {
            jTabbedPane.setPageAt(index, validateTitle(component.getPlotTitle()), validateTitle(component.getPlotTitle()), null, component1);
        }
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
            Component[] components = jTabbedPane.getPageComponents();
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
            jTabbedPane = new JListCardPanel();
            jTabbedPane.putClientProperty(PlotComponent.class.getName(), this);
            jTabbedPane.setPreferredSize(new Dimension(600, 400));
        }
        return jTabbedPane;
    }


}
