/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.components;

import net.thevpc.echo.AppWorkspace;
import net.thevpc.echo.api.components.AppDock;
import net.thevpc.echo.api.components.AppWindow;
import net.thevpc.echo.Window;
import net.thevpc.echo.constraints.Anchor;
import net.thevpc.echo.model.WindowModel;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruplot.PlotComponent;
import net.thevpc.scholar.hadruplot.model.PlotModel;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.AbstractHWSolverActionResult;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.HWSolverActionContext;

/**
 * @author vpc
 */
public class PlotResult extends AbstractHWSolverActionResult {

    private final PlotModel model;

    public PlotResult(String actionId, String name, String path, PlotModel model, String icon) {
        super(actionId, "plot", name, path, name, path, icon);
        this.model = model;
    }

    @Override
    protected void saveFileImpl(HWSolverActionContext context, String path) {
    }

    @Override
    public void loadFileImpl(HWSolverActionContext context, String path) {
//        result.toString();
    }

    @Override
    public void show(HWSolverActionContext context) {
        PlotComponent r = Plot.display(false).plot(model);
        context.app().runUI(() -> {
            AppDock ws = context.studio().content();
            AppWindow plot = ws.children().get("Plot");
            if (plot == null) {
                plot = new Window(new WindowModel(
                        "Plot", title, Anchor.CENTER, context.studio().app().toolkit().createComponent(r.toComponent()),
                        context.app()
                ));
                ws.children().add(plot);
                plot.title().set("Plot " + defaultName());
                plot.closable().set(false);
                plot.active().set(true);
            } else {
                plot.title().set("Plot " + defaultName());
                plot.component().set(context.app().toolkit().createComponent(r.toComponent()));
                plot.active().set(true);
            }
        });
    }

}
