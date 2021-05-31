/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import net.thevpc.echo.api.AppEvent;
import net.thevpc.echo.swing.helpers.actions.SwingAppUndoableAction;
import net.thevpc.echo.api.UndoableAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.HWSProjectResultsTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.HWSolverResult;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.HWSolverResultLocationType;
import net.thevpc.common.msg.Message;

/**
 *
 * @author vpc
 */
public class RemoveDefaultFileResultAction extends SwingAppUndoableAction {

    HWSProjectResultsTool outer;

    public RemoveDefaultFileResultAction(HWSProjectResultsTool outer) {
        super(outer.app(), "RemoveDefaultFileResult");
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
                    i.deleteFile();
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
        if (r!=null && (!r.isSaved() || r.locationType() != HWSolverResultLocationType.ACTION)) {
            return null;
        }
        return r;
    }

}
