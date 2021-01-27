/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruplot.containers;

import net.thevpc.scholar.hadruplot.PlotComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
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

    private JFrame addFrame(int index) {
        JFrame f = new JFrame();
        f.getContentPane().setLayout(new BorderLayout());
        f.addWindowListener(windowMonitor);
        synchronized (frames) {
            if (index < 0) {
                frames.add(f);
            } else {
                frames.add(index, f);
            }
        }
        return f;
    }

    public void addComponentImpl(PlotComponent component, int index) {
        JFrame frame = null;
        if (index < frames.size()) {
            frame = addFrame(index);
            frame.setTitle(component.getPlotTitle());
        } else {
            frame = getFrame(index);
        }

        frame.getContentPane().removeAll();
        frame.getContentPane().add(toComponent(component));
        frame.pack();
        frame.setVisible(true);
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

    @Override
    public BufferedImage getImage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveImageFile(String file) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
