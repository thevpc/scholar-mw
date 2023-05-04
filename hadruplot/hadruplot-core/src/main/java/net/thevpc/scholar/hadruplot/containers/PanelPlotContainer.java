/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruplot.containers;


import net.thevpc.scholar.hadruplot.PlotComponent;

import javax.swing.*;
import java.awt.*;

/**
 * @author vpc
 */
public class PanelPlotContainer extends AbstractPlotContainer {

    private JPanel panel;

    public PanelPlotContainer() {
        super();
    }

    public PanelPlotContainer(JPanel panel) {
        if (panel != null) {
            this.panel = panel;
        } else {
            this.panel = new JPanel(new MyGrid());
        }
        panel.putClientProperty(PlotComponent.class.getName(), this);
        panel.setPreferredSize(new Dimension(600, 400));
        setPlotWindowContainerFactory(PanelPlotWindowContainerFactory.INSTANCE);
    }

    public int indexOfPlotComponent(PlotComponent plotComponent) {
        if (panel != null) {
            int tabCount = panel.getComponentCount();
            for (int i = 0; i < tabCount; i++) {
                Component ii = panel.getComponent(i);
                if (ii != null && plotComponent.toComponent() == ii) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public PlotComponent getPlotComponent(int index) {
        if (panel != null) {
            Component c = panel.getComponent(index);
            if (c instanceof JComponent) {
                return toPlotComponent((JComponent) c);
            }
        }
        return null;
    }


    @Override
    public int getPlotComponentsCount() {
        if (panel != null) {
            return panel.getComponentCount();
        }
        return 0;
    }

    @Override
    public void removePlotComponentImpl(PlotComponent component) {
        int index = indexOfPlotComponent(component);
        panel.remove(index);
    }

    public void addComponentImpl(PlotComponent component, int index) {
        JComponent jcomp = toComponent(component);
        String title = validateTitle(component.getPlotTitle());//how to use this?
        if (index < 0) {
            panel.add(jcomp);
        } else {
            panel.add(jcomp, index);
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
        if (panel != null) {
            Component[] components = panel.getComponents();
            for (Component component : components) {
                if (component instanceof JComponent) {
                    PlotComponent t = toPlotComponent((JComponent) component);
                    remove(t);
                }
            }
            panel.removeAll();
        }
    }

    @Override
    public JComponent toComponent() {
        if (panel == null) {
            panel = new JPanel(new MyGrid());
            panel.putClientProperty(PlotComponent.class.getName(), this);
            panel.setPreferredSize(new Dimension(600, 400));
        }
        return panel;
    }

    public JPanel getPanel() {
        return panel;
    }

    private static class MyGrid extends GridLayout {
        public MyGrid() {
        }

        protected void reevel(Container parent) {
            int r = parent.getComponentCount();
            switch (r) {
                case 0:
                case 1: {
                    setColumns(0);
                    setRows(1);
                    break;
                }
                case 2: {
                    setColumns(2);
                    setRows(0);
                }
                default: {
                    double cols = Math.ceil(Math.sqrt(r));
                    setColumns((int) cols);
                    setRows(0);
                }
            }
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            reevel(parent);
            return super.preferredLayoutSize(parent);
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            reevel(parent);
            return super.minimumLayoutSize(parent);
        }

        @Override
        public void layoutContainer(Container parent) {
            reevel(parent);
            super.layoutContainer(parent);
        }
    }

}
