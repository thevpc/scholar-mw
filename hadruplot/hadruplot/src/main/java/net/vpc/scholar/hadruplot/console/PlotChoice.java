package net.vpc.scholar.hadruplot.console;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 17 janv. 2007 12:24:34
 */
public abstract class PlotChoice {
    private String id;
    private String title;
    private boolean enabled;

    public String getTitle() {
        return title;
    }

    protected PlotChoice(String id) {
        this(id, id, true);
    }

    protected PlotChoice(String id, String title, boolean enabled) {
        this.id = id;
        this.title = title;
        this.enabled = enabled;
    }

    public String getId() {
        return id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public abstract void runStudy(PlotConsoleProjectTemplate template);
}
