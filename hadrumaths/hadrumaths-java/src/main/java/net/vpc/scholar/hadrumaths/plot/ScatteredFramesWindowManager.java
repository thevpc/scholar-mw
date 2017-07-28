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
public class ScatteredFramesWindowManager extends AbstractPlotWindowManager {
    public void addPlotComponentImpl(PlotComponent component) {
        createFrame(component.toComponent(), validateTitle(component.getPlotTitle()));
    }

    @Override
    public void removePlotComponentImpl(PlotComponent component) {
        JFrame f = (JFrame) component.toComponent().getClientProperty(JFrame.class.getName());
        if (f != null) {
            f.dispose();
        }
    }

    private JFrame createFrame(JComponent cc,String title){
        JFrame frame = new JFrame();
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(cc);
        frame.pack();
        frame.setVisible(true);
        cc.putClientProperty(JFrame.class.getName(), frame);
        return frame;
    }
}
