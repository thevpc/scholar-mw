/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruplot.containers;

import javax.swing.*;
import java.awt.*;
import net.thevpc.common.swing.JTreeCardPanel;
import net.thevpc.scholar.hadruplot.PlotComponent;
import net.thevpc.scholar.hadruplot.PlotPath;

/**
 * @author vpc
 */
public class TreeCardPlotContainer extends AbstractPlotContainer {

    private JTreeCardPanel cardPanel;

    public TreeCardPlotContainer() {
        super();
        setPlotWindowContainerFactory(TabbedPlotWindowContainerFactory.INSTANCE);
    }

    @Override
    public void add(PlotComponent component, net.thevpc.scholar.hadruplot.PlotPath path) {
//        prepare(component);
        if(path==null){
            path=PlotPath.ROOT;
        }
        String validateTitle = validateTitle(component.getPlotTitle());
        cardPanel.addPage(path.append(component.getPlotTitle()).toString(), validateTitle, null, toComponent(component));
    }

    public int indexOfPlotComponent(PlotComponent plotComponent) {
        if (cardPanel != null) {
            int tabCount = cardPanel.getPageComponentsCount();
            for (int i = 0; i < tabCount; i++) {
                Component ii = cardPanel.getPageComponent(i);
                if (ii != null && plotComponent.toComponent() == ii) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public PlotComponent getPlotComponent(int index) {
        if (cardPanel != null) {
            Component c = cardPanel.getPageComponent(index);
            if (c instanceof JComponent) {
                return toPlotComponent((JComponent) c);
            }
        }
        return null;
    }

    @Override
    public int getPlotComponentsCount() {
        if (cardPanel != null) {
            return cardPanel.getPageComponentsCount();
        }
        return 0;
    }

    @Override
    public void removePlotComponentImpl(PlotComponent component) {
        JTreeCardPanel.PanelPage p = (JTreeCardPanel.PanelPage) component.toComponent().getClientProperty("$PanelPage");
        if (p != null) {
            cardPanel.removePageAt(p.getId());
        }
    }

    @Override
    public void addComponentImpl(PlotComponent component, int index) {
        JComponent component1 = toComponent(component);
        String validateTitle = validateTitle(component.getPlotTitle());
//        if (index < 0) {
        cardPanel.addPage(validateTitle, validateTitle, null, component1);
//        } else {
//            cardPanel.setPageAt(index, validateTitle(component.getPlotTitle()), validateTitle(component.getPlotTitle()), null, component1);
//        }
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
    @Override
    public void clear() {
        if (cardPanel != null) {
            Component[] components = cardPanel.getPageComponents();
            for (Component component : components) {
                if (component instanceof JComponent) {
                    PlotComponent t = toPlotComponent((JComponent) component);
                    remove(t);
                }
            }
            cardPanel.removeAll();
        }
    }

    @Override
    public JComponent toComponent() {
        if (cardPanel == null) {
            cardPanel = new JTreeCardPanel();
            cardPanel.putClientProperty(PlotComponent.class.getName(), this);
            cardPanel.setPreferredSize(new Dimension(600, 400));
        }
        return cardPanel;
    }

}
