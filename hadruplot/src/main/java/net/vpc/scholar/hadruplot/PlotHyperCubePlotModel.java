package net.vpc.scholar.hadruplot;

import java.util.Collection;
import java.util.Set;

/**
 * Created by vpc on 6/4/17.
 */
public class PlotHyperCubePlotModel extends BasePlotModel {
    private PlotDoubleConverter converter;
    private PlotHyperCube[] hyperCubes;

    public PlotHyperCubePlotModel() {
    }

    public PlotHyperCubePlotModel setConverter(PlotDoubleConverter zDoubleFunction) {
        Object old = this.converter;
        this.converter = zDoubleFunction;
//        firePropertyChange("converter", old, this.converter);
        return this;
    }

    public PlotDoubleConverter getConverter() {
        return converter;
    }

    public PlotHyperCube[] getHyperCubes() {
        return hyperCubes;
    }

    public PlotHyperCubePlotModel setHyperCubes(Collection<PlotHyperCube> hyperCubes) {
        return setVdiscretes(hyperCubes.toArray(new PlotHyperCube[0]));
    }

    public PlotHyperCubePlotModel setVdiscretes(PlotHyperCube... vdiscretes) {
        this.hyperCubes = vdiscretes;
        return this;
    }
}
