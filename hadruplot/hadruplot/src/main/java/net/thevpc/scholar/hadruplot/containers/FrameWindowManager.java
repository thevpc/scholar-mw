/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruplot.containers;

import net.thevpc.common.strings.StringUtils;

import javax.swing.*;
import java.awt.*;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruplot.PlotComponent;
import net.thevpc.scholar.hadruplot.PlotContainer;
import net.thevpc.scholar.hadruplot.PlotEvent;
import net.thevpc.scholar.hadruplot.PlotPropertyEvent;
import net.thevpc.scholar.hadruplot.PlotPropertyListener;

/**
 * @author vpc
 */
public class FrameWindowManager extends AbstractComponentPlotWindowManager {
    private JFrame frame;
    private PlotContainer rootContainer;

    public FrameWindowManager() {
        this(null);
    }

    public FrameWindowManager(PlotContainer rootContainer) {
        if (rootContainer == null) {
            this.rootContainer = TabbedPlotWindowContainerFactory.INSTANCE.create();
        }
        if (StringUtils.isBlank(this.rootContainer.getPlotTitle())) {
            this.rootContainer.setPlotTitle(Plot.Config.getDefaultWindowTitle());
        }
        this.rootContainer.setPlotWindowManager(this);
        this.rootContainer.addPlotPropertyListener(new PlotPropertyListener() {
            @Override
            public void onPropertyChange(PlotPropertyEvent event) {
                if ("title".equals(event.getProperty())) {
                    if (frame != null) {
                        frame.setTitle((String) event.getNewValue());
                    }
                }
            }

            @Override
            public void onRemoved(PlotEvent event) {

            }
        });
    }

    public PlotContainer getRootContainer() {
        return rootContainer;
    }

    @Override
    public PlotContainer getContainer(String[] path) {
        PlotContainer container = super.getContainer(path);
        showFrame();
        return container;
    }

    public void addPlotComponentImpl(PlotComponent component, String[] path) {
        super.addPlotComponentImpl(component, path);
        showFrame();
    }

    public JFrame getOrCreateFrame() {
        if (frame == null) {
            frame = new JFrame();
            frame.setTitle(getRootContainer().getPlotTitle());
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add(getRootContainer().toComponent());
        }
        return frame;
    }

    public void showFrame() {
        JFrame frame = getOrCreateFrame();
        frame.pack();
        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }
}
