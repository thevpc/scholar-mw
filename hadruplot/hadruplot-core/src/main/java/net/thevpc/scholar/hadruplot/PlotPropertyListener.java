package net.thevpc.scholar.hadruplot;

public interface PlotPropertyListener {
    String COMPONENT_DISPLAY_TYPE = "COMPONENT_DISPLAY_TYPE";
    String SUB_COMPONENT_DISPLAY_TYPE = "SUB_COMPONENT_DISPLAY_TYPE";

    void onPropertyChange(PlotPropertyEvent event);

    void onRemoved(PlotEvent event);
}
