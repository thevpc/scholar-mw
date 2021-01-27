package net.thevpc.scholar.hadruplot.console;

import net.thevpc.common.swing.win.WindowPath;
import net.thevpc.scholar.hadruplot.PlotComponent;
import net.thevpc.scholar.hadruplot.PlotContainer;
import net.thevpc.scholar.hadruplot.SimplePlotComponent;
import net.thevpc.scholar.hadruplot.containers.DefaultPlotWindowContainerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 10 janv. 2007 21:07:34
 */
public class PlotContainerConsoleWindow implements ConsoleWindow {
    private WindowPath windowPath;
    private PlotContainer pane;
//    private int index;

    public PlotContainerConsoleWindow(WindowPath windowPath, PlotContainer pane/*,int index*/) {
        this.pane = pane;
//        this.index = index;
        this.windowPath = windowPath;
    }
    public PlotContainerConsoleWindow(WindowPath windowPath, JInternalFrame internalFrame) {
        Component[] components = internalFrame.getContentPane().getComponents();
        //JTabbedPane pane;
        PlotContainer tcontainer = null;
        if (components.length == 0) {
            tcontainer = DefaultPlotWindowContainerFactory.INSTANCE.create();
            internalFrame.getContentPane().add(tcontainer.toComponent());
        } else {
            Component old = components[0];
            if (old instanceof JComponent) {
                tcontainer = (PlotContainer) ((JComponent) old).getClientProperty(PlotComponent.class.getName());
            }
            if (tcontainer == null) {
                internalFrame.getContentPane().remove(old);
//                tcontainer = new ListCardPlotContainer();// PanelPlotWindowContainerFactory.INSTANCE.create();
                tcontainer = DefaultPlotWindowContainerFactory.INSTANCE.create();
                internalFrame.getContentPane().add(tcontainer.toComponent());
            }
        }

        this.pane = tcontainer;
//        this.index = index;
        this.windowPath = windowPath;
    }

    public JComponent getComponent() {
        return pane.toComponent();
//        JComponent component = (JComponent) pane.getComponentAt(index);
//        return component instanceof DummyLabel?null:component;
    }

    public String getTitle() {
        return pane.getPlotTitle();//.getTitleAt(index);
    }

    public WindowPath getWindowPath() {
        return windowPath;
    }

    public void setComponent(JComponent compoent) {
        pane.add(getPlotComponent(compoent));
    }

    public void setTitle(String title) {
        pane.setPlotTitle(title);
    }

    public void addChild(String title, JComponent component2) {
        PlotComponent pc = getPlotComponent(component2);
        if(title==null||title.isEmpty()){
            title="/";
        }
        if(!title.startsWith("/")){
            title="/"+title;
        }
        pane.add(pc, title);
//        JComponent component = getComponent();
//        if(component==null){
//            component = new JListCardPanel();
//            setComponent(component);
//        }
//        ((JCardPanel)component).addPage(
//                title,title,null, component2
//        );
    }

    private PlotComponent getPlotComponent(JComponent component2) {
        if (component2 instanceof PlotComponent) {
            return (PlotComponent) component2;
        }
        PlotComponent pc = (PlotComponent) component2.getClientProperty(PlotComponent.class);
        if (pc == null) {
            pc = new SimplePlotComponent(component2);
        }
        return pc;
    }
}
