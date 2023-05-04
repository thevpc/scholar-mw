/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions;

import net.thevpc.echo.api.AppEvent;
import net.thevpc.echo.api.UndoableAction;
import net.thevpc.common.msg.Message;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 *
 * @author vpc
 */
public class CopyNodeAction extends HWUnduableAction {


    HWSSolutionExplorerTool outer;

    public CopyNodeAction(HWSSolutionExplorerTool outer) {
        super(outer.app(), "Copy");
        this.outer = outer;

        outer.tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                refresh();
            }
        });

    }

    @Override
    protected UndoableAction createUndo() {
        return new UndoableAction() {
            @Override
            public Message doAction(AppEvent event) {
                Object i = getSelected();

                return null;
            }

            @Override
            public void redoAction(AppEvent event) {

            }

            @Override
            public void undoAction(AppEvent event) {

            }
        };
    }

    @Override
    public void refresh() {
        Object i = getSelected();
        setAccessible(i != null);
    }

    private Object getSelected() {
        return (Object) outer.getSelectedItemsOneValue();
    }


}
