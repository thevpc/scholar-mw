/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.actions.global;

import java.awt.event.ActionEvent;
import net.thevpc.echo.Application;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.actions.HAction;

/**
 *
 * @author vpc
 */
public class ExitAction extends HAction {
    
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
