/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.plot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author vpc
 */
public class FramePlotContainer extends AbstractPlotContainer {

    private final List<JFrame> frames = new ArrayList<>();
    private JComponent component;
    private WindowListener windowMonitor = new WindowListener() {
        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {

        }

        @Override
        public void windowClosed(WindowEvent e) {
            synchronized (frames) {
                for (Iterator<JFrame> iterator = frames.iterator(); iterator.hasNext(); ) {
                    JFrame frame = iterator.next();
                    if (frame == e.getWindow()) {
                        iterator.remove();
                        //fire event!
                    }
                }
            }
        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {

        }
    };

    public FramePlotContainer() {
        super();
        setPlotWindowContainerFactory(TabbedPlotWindowContainerFactory.INSTANCE);
    }


    public PlotComponent toPlotComponent(JFrame frame) {
        Component[] components = frame.getContentPane().getComponents();
        for (Component component : components) {
            if (component instanceof JComponent) {
                PlotComponent c = super.toPlotComponent((JComponent) component);
                if (c != null) {
                    return c;
                }
            }
        }
        return null;
    }

    public int indexOfPlotComponent(PlotComponent plotComponent) {
        for (int i = 0; i < frames.size(); i++) {
            JFrame frame = getFrame(i);
            PlotComponent c = toPlotComponent(frame);
            if (c != null) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public PlotComponent getPlotComponent(int index) {
        return toPlotComponent(getFrame(index));
    }

    @Override
    public PlotContainer add(int index, String containerName) {
        if (index >= 0) {
            PlotComponent plotComponent = getPlotComponent(index);
            PlotContainer t = getPlotWindowContainerFactory().create();
            t.setPlotTitle(containerName);
            t.setPlotWindowManager(getPlotWindowManager());
            JFrame frame=null;
            if(index<frames.size()) {
                frame = addFrame(index);
                frame.setTitle(containerName);
            }else{
                frame =getFrame(index);
            }
            frame.getContentPane().removeAll();
            frame.getContentPane().add(toComponent(t));
            if(plotComponent!=null) {
                t.add(plotComponent);
            }
            return t;
        }
        return null;
    }

    private JFrame addFrame(int index) {
        JFrame f=new JFrame();
        synchronized (frames) {
            frames.add(index,f);
        }
        return f;
    }

    private void showFrame(int i) {
        JFrame frame = getFrame(i);
        frame.pack();
        frame.setVisible(true);
    }

    private JFrame getFrame(int i) {
        synchronized (frames) {
            return frames.get(i);
        }
    }

    @Override
    public int getPlotComponentsCount() {
        synchronized (frames) {
            return frames.size();
        }
    }

    @Override
    public void removePlotComponentImpl(PlotComponent component) {
        int index = indexOfPlotComponent(component);
        getFrame(index).dispose();
    }

    public void addPlotComponentImpl(PlotComponent component) {
        JFrame f = new JFrame(validateTitle(component.getPlotTitle()));
        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(toComponent(component));
        f.addWindowListener(windowMonitor);
        f.pack();
        f.setVisible(true);
        frames.add(f);
    }

    public JComponent createComponent() {
        return new JButton("frames");
    }

    @Override
    public JComponent toComponent() {
        if (component == null) {
            component = createComponent();
        }
        return component;
    }

}
