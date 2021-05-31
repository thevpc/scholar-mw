/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.actions;

import net.thevpc.echo.api.AppUndoableActionSupplier;
import net.thevpc.echo.Application;
import net.thevpc.echo.swing.helpers.actions.SwingAbstractAppAction;

/**
 *
 * @author vpc
 */
public abstract class HActionUnduable extends SwingAbstractAppAction implements AppUndoableActionSupplier{

    public HActionUnduable(Application application, String id) {
        super(application, id);
    }

    public HActionUnduable(Application application, String id, String name, String iconId, String description) {
        super(application, id, name, iconId, description);
    }
    
}
