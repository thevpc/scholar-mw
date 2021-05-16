/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.extra;

import java.awt.event.ActionEvent;

import net.thevpc.echo.Application;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.actions.HAction;

/**
 *
 * @author thevpc
 */
public class UndoAction extends HAction {

    public UndoAction(Application aplctn) {
        super(aplctn, "Undo");
    }

    @Override
    public void actionPerformedImpl(ActionEvent e) {
        getApplication().history().undoAction();
    }
    @Override
    public void refresh() {

    }

}
