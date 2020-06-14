/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.view3d.actions;

import java.awt.event.ActionEvent;
import net.vpc.common.app.AbstractAppAction;
import net.vpc.common.app.Application;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;

/**
 *
 * @author vpc
 */
public class ZoomInAction extends AbstractAppAction {

    private final HadruwavesStudio studio;

    public ZoomInAction(Application application, String id, final HadruwavesStudio studio) {
        super(application, id);
        this.studio = studio;
    }

    @Override
    protected void actionPerformedImpl(ActionEvent e) {
        studio.get3DView().getChartPanel().zoomIn();
    }
    @Override
    public void refresh() {

    }

}
