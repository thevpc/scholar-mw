/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import net.vpc.common.app.AppEvent;
import net.vpc.common.app.AppUndoableAction;
import net.vpc.common.app.UndoableAction;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.HWSProjectResultsTool;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.HWSolverResult;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.HWSolverResultLocationType;
import net.vpc.common.msg.Message;

/**
 *
 * @author vpc
 */
public class SaveResultAction extends AppUndoableAction {

    HWSProjectResultsTool outer;

    public SaveResultAction(HWSProjectResultsTool outer) {
        super(outer.app(), "SaveResult");
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
                HWSolverResult i = getSelectedResult();
                if (i != null) {
                    i.saveDefault(outer.solverContext());
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
        HWSolverResult i = getSelectedResult();
        setAccessible(i != null);
    }

    private HWSolverResult getSelectedResult() {
        HWSolverResult r = (HWSolverResult) outer.getSelectedItem(x -> x instanceof HWSolverResult);
        if (r!=null && (r.isSaved() || r.locationType() != HWSolverResultLocationType.ACTION)) {
            return null;
        }
        if (outer.configuration() == null || !outer.configuration().project().get().isPersistent()) {
            return null;
        }
        return r;
    }

}
