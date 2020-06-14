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
public class PasteAction extends AbstractAppAction {
    
    private final HadruwavesStudio outer;

    public PasteAction(Application aplctn, final HadruwavesStudio outer) {
        super(aplctn, "Paste");
        this.outer = outer;
    }

    @Override
    public void actionPerformedImpl(ActionEvent e) {
    }
        @Override
    public void refresh() {

    }

}
