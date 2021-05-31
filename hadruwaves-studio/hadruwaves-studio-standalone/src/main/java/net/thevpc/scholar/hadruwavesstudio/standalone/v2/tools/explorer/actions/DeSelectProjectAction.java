/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import net.thevpc.echo.impl.action.PropUndoableAction;
import net.thevpc.echo.swing.helpers.actions.SwingAppUndoableAction;
import net.thevpc.echo.api.UndoableAction;
import net.thevpc.scholar.hadruwaves.project.HWProject;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;

/**
 *
 * @author vpc
 */
public class DeSelectProjectAction extends SwingAppUndoableAction {

    HWSSolutionExplorerTool outer;

    public DeSelectProjectAction(HWSSolutionExplorerTool outer) {
        super(outer.app(), "DeSelectProject");
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
        return new PropUndoableAction(
                () -> null,
                () -> outer.studio().proc().solution().get().activeProject(),
                "De-select project {0}"
        );
    }

    @Override
    public void refresh() {
        Object i = getSelectedProject();
        setAccessible(i != null && i==outer.studio().proc().solution().get().activeProject().get());
    }

    private HWProject getSelectedProject() {
        Object[] selectedItems = outer.getSelectedItemValues();
        if (selectedItems.length == 1) {
            Object wn = selectedItems[0];
            if (wn instanceof HWProject) {
                return (HWProject) wn;
            }
        }
        return null;
    }

}
