package net.vpc.scholar.hadrumaths.plot.console;

import java.awt.event.ActionEvent;

public abstract class PlotConsoleMenuItem {
    private String name;
    private String title;
    private String path;
    private int order;
    private int initialIndex;

    public PlotConsoleMenuItem(String name, String title, String path, int order) {
        this.name = name;
        this.title = title;
        this.path = path;
        this.order = order;
    }

    public String getFullPath() {
        if (path.equals("/")) {
            return "/" + getName();
        }
        return path + "/" + getName();
    }

    public int getInitialIndex() {
        return initialIndex;
    }

    public void setInitialIndex(int initialIndex) {
        this.initialIndex = initialIndex;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public int getOrder() {
        return order;
    }

    public void actionPerformed(PlotConsole console, ActionEvent e) {

    }

}
