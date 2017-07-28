package net.vpc.scholar.hadrumaths.plot.console;

import javax.swing.*;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 10 janv. 2007 20:53:11
 */
public interface ConsoleWindow {
    WindowPath getWindowPath();

    JComponent getComponent();

    void setComponent(JComponent compoent);

    void setTitle(String title);

    void addChild(String title, JComponent component);
}
