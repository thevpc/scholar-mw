/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.plot;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author vpc
 */
public class TabbedPlotContainer extends AbstractPlotContainer {

    private JTabbedPane jTabbedPane;

    public TabbedPlotContainer(PlotWindowManager plotWindowManager, String globalTitle) {
        super(plotWindowManager, globalTitle);
    }

    @Override
    public void removePlotComponentImpl(PlotComponent component) {
        if (jTabbedPane != null) {
            int tabCount = jTabbedPane.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                Component ii = jTabbedPane.getComponentAt(i);
//                Component ii = jTabbedPane.getTabComponentAt(i);
                if (component.toComponent() == ii) {
                    jTabbedPane.removeTabAt(i);
                    return;
                }
            }
        }
    }

    public void addPlotComponentImpl(PlotComponent component) {
        toComponent().add(validateTitle(component.getPlotTitle()), component.toComponent());
    }

    public void clear() {
        if (jTabbedPane != null) {
            Component[] components = jTabbedPane.getComponents();
            for (Component component : components) {
                if(component instanceof PlotComponent){
                    remove((PlotComponent) component);
                }else if(component instanceof JComponent){
                    Object a = ((JComponent) component).getClientProperty(PlotComponent.class.getName());
                    if(a!=null){
                        remove((PlotComponent) a);
                    }
                }
            }
        }
    }

    @Override
    public JComponent toComponent() {
        if (jTabbedPane == null) {
            jTabbedPane = new JTabbedPane();
            jTabbedPane.putClientProperty(PlotComponent.class.getName(),this);
            jTabbedPane.setPreferredSize(new Dimension(600,400));
        }
        return jTabbedPane;
    }

}
