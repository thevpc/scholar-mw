package net.vpc.scholar.hadrumaths.plot;

public interface PlotPropertyListener {
    String COMPONENT_DISPLAY_TYPE = "COMPONENT_DISPLAY_TYPE";
    String SUB_COMPONENT_DISPLAY_TYPE = "SUB_COMPONENT_DISPLAY_TYPE";

    void onPropertyChange(PlotPropertyEvent event);
}
