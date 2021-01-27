/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.mom;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadruplot.LibraryPlotType;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruplot.PlotType;
import net.thevpc.scholar.hadruplot.model.PlotModel;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.components.PlotResult;
import java.io.File;

import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwaves.solvers.HWSolver;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.AbstractHWSolverAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.dialogs.XyzSamplesDialog;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.HWSolverActionContext;

/**
 *
 * @author vpc
 */
public class MoMSourceAction extends AbstractHWSolverAction {

    XyzSamplesDialog xyzdialog;

    public MoMSourceAction(XyzSamplesDialog xyzdialog) {
        super("MoMSource", "Source XYZ", "/MoM Solver/Build/Source XYZ", "Build");
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
                String icon="Chart";
                Object result = null;
                String title2=title;
                if (r.zn == 1) {
                    ComplexMatrix x = solver.electricField().cartesian()
                            .evalMatrix(
                                    Axis.X,
                                    Maths.dtimes(r.xmin, r.xmax, r.xn),
                                    Maths.dtimes(r.ymin, r.ymax, r.yn),
                                    r.zmin
                            );
                    ComplexMatrix y = solver.electricField().cartesian()
                            .evalMatrix(
                                    Axis.Y,
                                    Maths.dtimes(r.xmin, r.xmax, r.xn),
                                    Maths.dtimes(r.ymin, r.ymax, r.yn),
                                    r.zmin
                            );
                    ComplexMatrix z = solver.electricField().cartesian()
                            .evalMatrix(
                                    Axis.Z,
                                    Maths.dtimes(r.xmin, r.xmax, r.xn),
                                    Maths.dtimes(r.ymin, r.ymax, r.yn),
                                    r.zmin
                            );
                    title2="Source 2D"+configuration.discriminatorStringSuffix();
                    result = new AxisVector<>(Coordinates.CARTESIAN, Maths.$MATRIX,x, y, z);
                    icon="Result2d";
                }
                if (result == null) {
                    result = solver.electricField().cartesian()
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
