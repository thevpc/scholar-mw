/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.mom;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadruplot.LibraryPlotType;
import net.vpc.scholar.hadruplot.Plot;
import net.vpc.scholar.hadruplot.PlotType;
import net.vpc.scholar.hadruplot.model.PlotModel;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.components.PlotResult;
import java.io.File;

import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwaves.solvers.HWSolver;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.AbstractHWSolverAction;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.dialogs.XyzSamplesDialog;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.HWSolverActionContext;

/**
 *
 * @author vpc
 */
public class MoMCurrentAction extends AbstractHWSolverAction {

    XyzSamplesDialog xyzdialog;

    public MoMCurrentAction(XyzSamplesDialog xyzdialog) {
        super("MoMCurrent", "Current XYZ", "/MoM Solver/Current/Current XYX", "Build");
        this.xyzdialog=xyzdialog;
    }

    @Override
    public boolean acceptResultFile(File file) {
        return file.getName().endsWith(".plot.hwr");
    }

    @Override
    public void run(HWSolverActionContext context) {
        HWConfigurationRun configuration = context.configuration();
        String title = name()+configuration.discriminatorStringSuffix();
        xyzdialog.setTitle(title);
        if (xyzdialog.show()) {
            HWSolver solver = context.studio().buildSolver(configuration);
            
            XyzSamplesDialog.Result r = xyzdialog.get(configuration);
            context.app().runBack(() -> {
                Object result = null;
                String title2=title;
                String icon="Chart";
                if (r.zn == 1) {
                    if (r.yn == 1) {
                        ComplexVector x = solver.current()
                                .evalVector(
                                        Axis.Z,
                                        Maths.dtimes(r.xmin, r.xmax, r.xn),
                                        r.ymin,
                                        r.zmin
                                );
                        ComplexVector y = solver.current()
                                .evalVector(
                                        Axis.Y,
                                        Maths.dtimes(r.xmin, r.xmax, r.xn),
                                        r.ymin,
                                        r.zmin
                                );
                        ComplexVector z = solver.current()
                                .evalVector(
                                        Axis.Z,
                                        Maths.dtimes(r.xmin, r.xmax, r.xn),
                                        r.ymin,
                                        r.zmin
                                );
                        title2="Current 1D"+configuration.discriminatorStringSuffix();
                        result = new AxisVector<>(Coordinates.CARTESIAN, Maths.$VECTOR,x, y, z);
                        icon="Result1d";
                    } else {
                        ComplexMatrix x = solver.current()
                                .evalMatrix(
                                        Axis.X,
                                        Maths.dtimes(r.xmin, r.xmax, r.xn),
                                        Maths.dtimes(r.ymin, r.ymax, r.yn),
                                        r.zmin
                                );
                        ComplexMatrix y = solver.current()
                                .evalMatrix(
                                        Axis.Y,
                                        Maths.dtimes(r.xmin, r.xmax, r.xn),
                                        Maths.dtimes(r.ymin, r.ymax, r.yn),
                                        r.zmin
                                );
                        ComplexMatrix z = solver.current()
                                .evalMatrix(
                                        Axis.Z,
                                        Maths.dtimes(r.xmin, r.xmax, r.xn),
                                        Maths.dtimes(r.ymin, r.ymax, r.yn),
                                        r.zmin
                                );
                        title2="Current 2D"+configuration.discriminatorStringSuffix();
                        result = new AxisVector<>(Coordinates.CARTESIAN, Maths.$MATRIX,x, y, z);
                        icon="Result2d";
                    }
                }
                if (result == null) {
                    result = solver.current()
                            .evalVDiscrete(
                                    Maths.dtimes(r.xmin, r.xmax, r.xn),
                                    Maths.dtimes(r.ymin, r.ymax, r.yn),
                                    Maths.dtimes(r.zmin, r.zmax, r.zn)
                            );
                    icon="Result3d";
                }
                PlotModel model = Plot.builder().createModel(result);
                model.setPlotType(new LibraryPlotType(PlotType.HEATMAP));
                PlotResult plotResult = new PlotResult(id(), title2, path(), model,icon);
                context.studio().results().addResult(plotResult);
                plotResult.show(context);
            });

        }
    }

}
