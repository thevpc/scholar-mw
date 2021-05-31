/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.view3d.actions;

import java.awt.event.ActionEvent;
import net.thevpc.echo.Application;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.actions.HAction;

/**
 *
 * @author vpc
 */
public class ZoomOutAction extends HAction {

    private final HadruwavesStudio outer;

    public ZoomOutAction(Application application, String id, final HadruwavesStudio outer) {
        super(application, id);
        this.outer = outer;
    }

    @Override
    protected void actionPerformedImpl(ActionEvent e) {
        outer.get3DView().getChartPanel().zoomOut();
    }

    @Override
    public void refresh() {

    }

}
