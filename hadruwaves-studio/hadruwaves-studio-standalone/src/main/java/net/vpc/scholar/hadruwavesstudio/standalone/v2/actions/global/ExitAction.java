/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.actions.global;

import java.awt.event.ActionEvent;
import net.vpc.common.app.AbstractAppAction;
import net.vpc.common.app.Application;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;

/**
 *
 * @author vpc
 */
public class ExitAction extends AbstractAppAction {
    
    private final HadruwavesStudio outer;

    public ExitAction(Application aplctn, final HadruwavesStudio outer) {
        super(aplctn, "Exit");
        this.outer = outer;
    }

    @Override
    protected boolean confirmation(ActionEvent e) {
        return outer.confirmSaveCurrent();
    }

    @Override
    public void actionPerformedImpl(ActionEvent e) {
        System.exit(0);
    }
        @Override
    public void refresh() {

    }

}
