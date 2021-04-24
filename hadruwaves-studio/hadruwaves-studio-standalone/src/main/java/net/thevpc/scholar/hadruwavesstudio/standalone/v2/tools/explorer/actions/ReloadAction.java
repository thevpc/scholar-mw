/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions;

import net.thevpc.echo.AppEvent;
import net.thevpc.echo.AppUndoableAction;
import net.thevpc.echo.UndoableAction;
import net.thevpc.echo.swing.actions.PropUndoableAction;
import net.thevpc.common.msg.Message;
import net.thevpc.scholar.hadruwaves.project.HWProject;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 *
 * @author vpc
 */
public class ReloadAction extends AppUndoableAction {

    HWSSolutionExplorerTool outer;

    public ReloadAction(HWSSolutionExplorerTool outer) {
        super(outer.app(), "Reload");
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
                for (HWProjectItem selectedItem : outer.getSelectedItems()) {
                    outer.reload(selectedItem);
                }
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
        boolean i = outer.getSelectedItems().length>0;
        setAccessible(i);
    }

}