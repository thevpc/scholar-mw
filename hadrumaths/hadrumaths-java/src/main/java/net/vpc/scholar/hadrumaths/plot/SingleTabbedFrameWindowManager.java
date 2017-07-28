/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.Plot;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 *
 * @author vpc
 */
public class SingleTabbedFrameWindowManager extends AbstractPlotWindowManager {
    private String globalTitle= Plot.Config.getDefaultWindowTitle();
    private JFrame frame;
    private JTabbedPane jTabbedPane;

    public SingleTabbedFrameWindowManager() {
    }

    public SingleTabbedFrameWindowManager(String globalTitle) {
        this.globalTitle=globalTitle;
    }

    @Override
    public void removePlotComponentImpl(PlotComponent component) {
        if(jTabbedPane!=null){
            int tabCount = jTabbedPane.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                Component ii = jTabbedPane.getTabComponentAt(i);
                if(component.toComponent()==ii){
                    jTabbedPane.removeTabAt(i);
                    return;
                }
            }
        }
    }

    public void addPlotComponentImpl(PlotComponent component) {
        if (frame == null) {
            frame = new JFrame();
            frame.setTitle(globalTitle);
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            frame.getContentPane().setLayout(new BorderLayout());
            jTabbedPane = new JTabbedPane();
            frame.getContentPane().add(jTabbedPane);
        }
        jTabbedPane.add(validateTitle(component.getPlotTitle()), component.toComponent());
        frame.pack();
        frame.setVisible(true);
    }

}
