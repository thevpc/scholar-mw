package net.thevpc.scholar.hadruplot.containers;

public class PlotContainerName {
    private String title;
    private String type;

    public PlotContainerName(String name) {
        if (name == null) {
            name = "";
        }
        int i = name.indexOf("::");
        String name0 = null;
        String type = null;
        if (i >= 0) {
            name0 = name.substring(0, i);
            type = name.substring(i + 2);
        } else {
            name0 = name;
            type = null;
        }

        this.title = name0;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }
}
