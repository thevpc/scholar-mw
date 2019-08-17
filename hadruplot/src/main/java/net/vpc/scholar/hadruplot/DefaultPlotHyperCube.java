package net.vpc.scholar.hadruplot;

public class DefaultPlotHyperCube implements PlotHyperCube{
    private PlotCube[] values;
    private String title;

    public DefaultPlotHyperCube(PlotCube ... values) {
        this.values = values;
    }

    @Override
    public int getCubesCount() {
        return values.length;
    }

    @Override
    public PlotCube getCube(int index) {
        return values[index];
    }

    @Override
    public String getTitle() {
        return title;
    }

    public DefaultPlotHyperCube setTitle(String title) {
        this.title = title;
        return this;
    }
}
