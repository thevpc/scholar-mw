package net.vpc.scholar.hadruplot.model;

import java.util.Collection;
import net.vpc.scholar.hadruplot.PlotHyperCube;

/**
 * Created by vpc on 6/4/17.
 */
public class PlotHyperCubePlotModel extends BasePlotModel {
    private PlotHyperCube[] hyperCubes;

    public PlotHyperCubePlotModel() {
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
