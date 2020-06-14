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
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.HWSProjectResultsTool;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.DefaultHWSolverActionContext;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.HWSolverActionResultRegistry;
import net.vpc.common.msg.Message;

/**
 *
 * @author vpc
 */
public class RunSolverAction extends AppUndoableAction {

    HWSProjectResultsTool outer;

    public RunSolverAction(HWSProjectResultsTool outer) {
        super(outer.app(), "RunSolver");
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
                HWSolverAction i = getSelectedAction();
                HWConfigurationRun conf = outer.proc().selectedConfiguration().get();
                if (conf != null) {
                    i.run(new DefaultHWSolverActionContext(outer.studio(), conf, HWSolverActionResultRegistry.DEFAULT));
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
        HWSolverAction i = getSelectedAction();
        setAccessible(i != null);
    }

    private HWSolverAction getSelectedAction() {
        return (HWSolverAction) outer.getSelectedItem(x -> x instanceof HWSolverAction);
    }

}
