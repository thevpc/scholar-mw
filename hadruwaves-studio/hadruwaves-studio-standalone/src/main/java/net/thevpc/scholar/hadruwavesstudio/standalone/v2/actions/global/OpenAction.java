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
public class OpenAction extends HAction{
    
    private final HadruwavesStudio outer;

    public OpenAction(Application aplctn, final HadruwavesStudio outer) {
        super(aplctn, "Open");
        this.outer = outer;
    }

    
    @Override
    public void actionPerformedImpl(ActionEvent e) {
        outer.open();
    }
        @Override
    public void refresh() {

    }

}
