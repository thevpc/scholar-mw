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
import net.vpc.scholar.hadruwaves.mom.solver.HWSolverMoM;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.AbstractHWSolverAction;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.HWSolverActionContext;

/**
 *
 * @author vpc
 */
public class MoMTestModeScalarProducts extends AbstractHWSolverAction {

    public MoMTestModeScalarProducts() {
        super("TestModeScalarProducts", "Test Mode Scalar Products", "/MoM Solver/Build/Test Mode Scalar Products", "Build");
    }

    @Override
    public boolean acceptResultFile(File file) {
        return file.getName().endsWith(".plot.hwr");
    }

    @Override
    public void run(HWSolverActionContext context) {
        HWConfigurationRun configuration = context.configuration();
        String title = name()+configuration.discriminatorStringSuffix();
        context.app().runBack(() -> {
            String icon="Chart";
            HWSolverMoM solver = (HWSolverMoM) context.studio().buildSolver(configuration);
            Object result = solver.str().testModeScalarProducts();
            PlotModel model = Plot.builder().createModel(result);
            model.setPlotType(new LibraryPlotType(PlotType.MATRIX));
            icon="ResultMatrix";
            PlotResult plotResult = new PlotResult(id(), title, path(), model,icon);
            context.studio().results().addResult(plotResult);
            plotResult.show(context);
        });
    }

}
