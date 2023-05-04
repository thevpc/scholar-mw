/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions;

import java.util.List;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import net.thevpc.echo.api.AppEvent;
import net.thevpc.echo.api.UndoableAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions.HWUnduableAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.HWSProjectResultsTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.HWSolverResult;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.HWSolverResultLocationType;
import net.thevpc.common.msg.Message;

/**
 *
 * @author vpc
 */
public class RemoveResultAction extends HWUnduableAction {

    HWSProjectResultsTool outer;

    public RemoveResultAction(HWSProjectResultsTool outer) {
        super(outer.app(), "RemoveResult");
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
                    if (i.locationType() == HWSolverResultLocationType.ACTION) {
                        List<HWSolverResult> all = outer.getConfResults().getActionResults(i.actionId());
                        all.removeIf(x -> x == i);
                    }
                    outer.updateRoot();
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
        return r;
    }

}
