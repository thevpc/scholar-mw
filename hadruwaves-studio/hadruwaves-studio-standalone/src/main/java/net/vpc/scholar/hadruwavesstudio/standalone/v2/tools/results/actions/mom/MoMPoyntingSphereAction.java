/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.mom;

import net.vpc.scholar.hadruplot.LibraryPlotType;
import net.vpc.scholar.hadruplot.Plot;
import net.vpc.scholar.hadruplot.PlotType;
import net.vpc.scholar.hadruplot.model.PlotModel;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.components.PlotResult;
import java.io.File;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwaves.solvers.HWSolver;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.AbstractHWSolverAction;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.dialogs.ThetaPhiRSamplesDialog;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.HWSolverActionContext;

/**
 *
 * @author vpc
 */
public class MoMPoyntingSphereAction extends AbstractHWSolverAction {

    ThetaPhiRSamplesDialog polardialog;

    public MoMPoyntingSphereAction(ThetaPhiRSamplesDialog polardialog) {
        super("MoMPoyntingSpherical", "Poynting Spherical", "/MoM Solver/Power/Poynting Spherical", "Build");
        this.polardialog = polardialog;
    }

    @Override
    public boolean acceptResultFile(File file) {
        return file.getName().endsWith(".plot.hwr");
    }

    @Override
    public void run(HWSolverActionContext context) {
        HWConfigurationRun configuration = context.configuration();
        String title = name()+configuration.discriminatorStringSuffix();
        polardialog.setTitle(title);
        if (polardialog.show()) {
            HWSolver solver = context.studio().buildSolver(configuration);
            ThetaPhiRSamplesDialog.Result r = polardialog.get(configuration);
            context.app().runBack(() -> {
                String icon="Chart";
                Object result = solver.poyntingVector().spherical()
                        .evalModuleMatrix(
                                Maths.dtimes(r.theta_min, r.theta_max, r.theta_n),
                                Maths.dtimes(r.phi_min, r.phi_max, r.phi_n),
                                r.r_min
                        );
                ;
                PlotModel model = Plot.builder().createModel(result);
                model.setPlotType(new LibraryPlotType(PlotType.HEATMAP));
                PlotResult plotResult = new PlotResult(id(), name(), path(), model, icon);
                context.studio().results().addResult(plotResult);
                plotResult.show(context);
            });
        }
    }

}
