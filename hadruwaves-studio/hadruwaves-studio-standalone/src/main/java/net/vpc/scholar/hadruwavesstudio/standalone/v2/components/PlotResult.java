/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.components;

import net.vpc.common.app.AppContentWindow;
import net.vpc.common.app.AppDockingWorkspace;
import net.vpc.scholar.hadruplot.Plot;
import net.vpc.scholar.hadruplot.PlotComponent;
import net.vpc.scholar.hadruplot.model.PlotModel;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.AbstractHWSolverActionResult;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.HWSolverActionContext;

/**
 *
 * @author vpc
 */
public class PlotResult extends AbstractHWSolverActionResult {

    private final PlotModel model;

    public PlotResult(String actionId, String name, String path, PlotModel model,String icon) {
        super(actionId, "plot", name, path, name, path,icon);
        this.model = model;
    }

    @Override
    public void loadFileImpl(HWSolverActionContext context, String path) {
//        result.toString();
    }

    @Override
    protected void saveFileImpl(HWSolverActionContext context, String path) {
    }

    @Override
    public void show(HWSolverActionContext context) {
        PlotComponent r = Plot.display(false).plot(model);
        context.app().runFront(() -> {
            AppDockingWorkspace ws = context.studio().workspace();
            AppContentWindow plot = ws.getContent("Plot");
            if (plot == null) {
                plot = ws.addContent("Plot", "Plot " + defaultName(), null, r.toComponent());
                plot.closable().set(false);
                plot.active().set(true);
            } else {
                plot.title().set("Plot " + defaultName());
                plot.component().set(r.toComponent());
                plot.active().set(true);
            }
        });
    }

}
