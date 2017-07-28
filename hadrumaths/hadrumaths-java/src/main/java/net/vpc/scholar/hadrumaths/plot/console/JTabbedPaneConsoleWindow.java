package net.vpc.scholar.hadrumaths.plot.console;

import net.vpc.scholar.hadrumaths.util.swingext.JCardPanel;
import net.vpc.scholar.hadrumaths.util.swingext.JListCardPanel;

import javax.swing.*;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 10 janv. 2007 21:07:34
 */
public class JTabbedPaneConsoleWindow implements ConsoleWindow{
    private WindowPath windowPath;
    private JTabbedPane pane;
    private int index;

    public JTabbedPaneConsoleWindow(WindowPath windowPath,JTabbedPane pane,int index) {
        this.pane = pane;
        this.index = index;
        this.windowPath = windowPath;
    }

    public JComponent getComponent() {
        JComponent component = (JComponent) pane.getComponentAt(index);
        return component instanceof DummyLabel?null:component;
    }

    public String getTitle() {
        return pane.getTitleAt(index);
    }

    public WindowPath getWindowPath() {
        return windowPath;
    }

    public void setComponent(JComponent compoent) {
        pane.setComponentAt(index,compoent);
    }

    public void setTitle(String title) {
        pane.setTitleAt(index,title);
    }

    public void addChild(String title, JComponent component2) {
        JComponent component = getComponent();
        if(component==null){
            component = new JListCardPanel();
            setComponent(component);
        }
        ((JCardPanel)component).addPage(
                title,title,null, component2
        );
    }
}
