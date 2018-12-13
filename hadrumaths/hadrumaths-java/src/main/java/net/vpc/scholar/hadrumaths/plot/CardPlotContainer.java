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

    private JListCardPanel cardPanel;

    public CardPlotContainer() {
        super();
        setPlotWindowContainerFactory(TabbedPlotWindowContainerFactory.INSTANCE);
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
        int index = indexOfPlotComponent(component);
        cardPanel.removePageAt(index);
    }

    public void addComponentImpl(PlotComponent component, int index) {
        JComponent component1 = toComponent(component);
        if (index < 0) {
            cardPanel.addPage(validateTitle(component.getPlotTitle()), validateTitle(component.getPlotTitle()), null, component1);
        } else {
            cardPanel.setPageAt(index, validateTitle(component.getPlotTitle()), validateTitle(component.getPlotTitle()), null, component1);
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
            cardPanel = new JListCardPanel();
            cardPanel.putClientProperty(PlotComponent.class.getName(), this);
            cardPanel.setPreferredSize(new Dimension(600, 400));
        }
        return cardPanel;
    }


}
