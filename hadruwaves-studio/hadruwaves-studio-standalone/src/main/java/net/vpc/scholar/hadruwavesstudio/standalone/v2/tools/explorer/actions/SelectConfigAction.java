/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions;

import java.util.function.Supplier;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import net.vpc.common.app.swing.actions.PropUndoableAction;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.common.app.AppUndoableAction;
import net.vpc.common.app.UndoableAction;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;

/**
 *
 * @author vpc
 */
public class SelectConfigAction extends AppUndoableAction {

    HWSSolutionExplorerTool explorer;

    public SelectConfigAction(HWSSolutionExplorerTool outer) {
        super(outer.app(), "SelectConfiguration");
        this.explorer = outer;
        outer.tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                refresh();
            }
        });

    }

    @Override
    protected UndoableAction createUndo() {
        return createSetConfigAction(() -> getSelectedConfiguration(), explorer);
    }

    @Override
    public void refresh() {
        Object i = null;
        i = getSelectedConfiguration();
        setAccessible(i != null);
    }

    public static PropUndoableAction createSetConfigAction(Supplier<HWConfigurationRun> newValue, HWSSolutionExplorerTool explorer) {
        return new PropUndoableAction(newValue, () -> {
            HWProjectItem[] iii = explorer.getSelectedItems();
            if (iii.length != 1) {
                return null;
            }
            if (iii[0].getItemValue() instanceof HWConfigurationRun) {
                return iii[0].getProject().configurations().activeConfiguration();
            }
            return null;
        }, "Select config {0}") {
            @Override
            protected void postDo() {
                explorer.refreshTools();
            }

            @Override
            protected void postUndo() {
                explorer.refreshTools();
            }

        };
    }

    private HWConfigurationRun getSelectedConfiguration() {
        Object[] selectedItems = explorer.getSelectedItemValues();
        if (selectedItems.length == 1) {
            Object wn = selectedItems[0];
            if (wn instanceof HWConfigurationRun) {
                return (HWConfigurationRun) wn;
            }
        }
        return null;
    }

}
