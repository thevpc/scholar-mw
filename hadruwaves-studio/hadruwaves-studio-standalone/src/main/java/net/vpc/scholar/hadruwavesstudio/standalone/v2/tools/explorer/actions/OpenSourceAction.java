/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions;

import net.vpc.common.app.*;
import net.vpc.common.msg.Message;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.components.SourceFileEditor;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.components.HWProjectSource;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.components.HWProjectSourceFile;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 * @author vpc
 */
public class OpenSourceAction extends AppUndoableAction {

    HWSSolutionExplorerTool outer;

    public OpenSourceAction(HWSSolutionExplorerTool outer) {
        super(outer.app(), "OpenSource");
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
                    if (selectedItem.getItem() instanceof HWProjectSourceFile) {
                        HWProjectSourceFile s = (HWProjectSourceFile) selectedItem.getItem();
                        outer.studio().openSourceFile(s);
                    }
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
        boolean i = outer.getSelectedItems(x -> (x.getItem() instanceof HWProjectSource && ((HWProjectSource) x.getItem()).file.isFile())).length == 1;
        setAccessible(i);
    }

}
