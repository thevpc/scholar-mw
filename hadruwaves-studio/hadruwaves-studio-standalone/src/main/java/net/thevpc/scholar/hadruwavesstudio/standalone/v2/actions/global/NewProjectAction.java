/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.actions.global;

import java.awt.event.ActionEvent;
import net.thevpc.echo.AbstractAppAction;
import net.thevpc.echo.Application;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;

/**
 *
 * @author vpc
 */
public class NewProjectAction extends AbstractAppAction {
    
    private final HadruwavesStudio outer;

    public NewProjectAction(Application aplctn, final HadruwavesStudio outer) {
        super(aplctn, "NewProject");
        this.outer = outer;
    }

    @Override
    public void actionPerformedImpl(ActionEvent e) {
        if (outer.proc().activeSolutionValue() == null) {
            outer.proc().newSolution();
        }
        outer.proc().activeSolutionValue().addProject(null, null);
    }
        @Override
    public void refresh() {

    }

}
