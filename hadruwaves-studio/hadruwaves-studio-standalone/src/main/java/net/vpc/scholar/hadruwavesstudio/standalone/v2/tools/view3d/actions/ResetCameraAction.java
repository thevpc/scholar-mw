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
public class ResetCameraAction extends AbstractAppAction {
    
    private final HadruwavesStudio outer;

    public ResetCameraAction(Application application, String id, final HadruwavesStudio outer) {
        super(application, id);
        this.outer = outer;
    }

    @Override
    protected void actionPerformedImpl(ActionEvent e) {
        outer.get3DView().getChartPanel().resetCamera();
    }
        @Override
    public void refresh() {

    }

}
