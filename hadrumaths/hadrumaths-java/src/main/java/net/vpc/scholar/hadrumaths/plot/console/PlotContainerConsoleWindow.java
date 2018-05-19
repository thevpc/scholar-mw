package net.vpc.scholar.hadrumaths.plot.console;

import net.vpc.scholar.hadrumaths.plot.PlotComponent;
import net.vpc.scholar.hadrumaths.plot.PlotContainer;
import net.vpc.scholar.hadrumaths.plot.SimplePlotComponent;

import javax.swing.*;

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
