/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions;

import net.vpc.common.app.AppEvent;
import net.vpc.common.app.AppUndoableAction;
import net.vpc.common.app.UndoableAction;
import net.vpc.common.msg.Message;
import net.vpc.common.props.FileObject;
import net.vpc.scholar.hadruwaves.project.HWProject;
import net.vpc.scholar.hadruwaves.project.HWSolution;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 *
 * @author vpc
 */
public class SaveAllNodeAction extends AppUndoableAction {


    HWSSolutionExplorerTool outer;

    public SaveAllNodeAction(HWSSolutionExplorerTool outer) {
        super(outer.app(), "SaveAll");
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
                HWSolution s = getSelectedFileObject();
                if(s!=null){
                    for (HWProject p : s.findProjects()) {
                        outer.studio().saveFileObject(p);
                    }
                    outer.studio().saveFileObject(s);
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
        FileObject i = getSelectedFileObject();
        setAccessible(i != null);
    }

    private HWSolution getSelectedFileObject() {
        return (HWSolution) outer.getSelectedItemsOneValue(x->x.getItem() instanceof HWSolution);
    }


}
