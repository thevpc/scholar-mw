/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions;

import net.vpc.common.app.AppEvent;
import net.vpc.common.app.AppUndoableAction;
import net.vpc.common.app.UndoableAction;
import net.vpc.common.app.swing.actions.PropUndoableAction;
import net.vpc.common.msg.Message;
import net.vpc.scholar.hadruwaves.project.HWProject;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;

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
