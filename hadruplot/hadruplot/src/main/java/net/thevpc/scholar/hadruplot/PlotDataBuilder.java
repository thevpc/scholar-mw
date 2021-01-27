package net.thevpc.scholar.hadruplot;

import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruplot.console.ConsoleAxis;
import net.thevpc.scholar.hadruplot.console.ConsoleAxisList;
import net.thevpc.scholar.hadruplot.console.PlotData;
import net.thevpc.scholar.hadruplot.console.params.ParamSet;
import net.thevpc.scholar.hadruplot.console.yaxis.PlotAxis;

import java.util.ArrayList;
import java.util.List;

public class PlotDataBuilder {
    private String title = "Unknown";
    private ConsoleAxisList axisList = new ConsoleAxisList();
    private ConsoleAwareObject structure;
    private ConsoleAwareObject structure2;
    private List<ParamSet> params = new ArrayList<ParamSet>();
    private List<PlotDataBuilderListener> listeners = new ArrayList<>();

    public PlotDataBuilder windowTitle(String title) {
        this.title = title;
        return this;
    }

    public PlotDataBuilder structure(ConsoleAwareObject structure) {
        this.structure = structure;
        return this;
    }

    public PlotDataBuilder structure2(ConsoleAwareObject structure) {
        this.structure = structure;
        return this;
    }

    public PlotDataBuilder addAxis() {
        axisList.addAxis();
        return this;
    }

    public PlotDataBuilder addXAxis(ConsoleAxis axis) {
        axisList.addAxis(axis);
        return this;
    }

    public PlotDataBuilder addYAxis(PlotAxis axis) {
        axisList.addY(axis);
        return this;
    }

    public PlotDataBuilder addYAxis(ConsoleAxisList axisList) {
        axisList.addAll(axisList);
        return this;
    }

    public PlotDataBuilder addParamSet(ParamSet paramSet) {
        params.add(paramSet);
        return this;
    }

    public void addListener(PlotDataBuilderListener listener) {
        listeners.add(listener);
    }

    public PlotData run() {
        return build();
    }

    public PlotData build() {
        PlotData plotData = new PlotData(
                title, structure, structure2, axisList, params.toArray(new ParamSet[0])
        );
        for (PlotDataBuilderListener listener : listeners) {
            listener.onBuild(plotData, this);
        }
        return plotData;
    }
}
