/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import net.thevpc.echo.AppEvent;
import net.thevpc.echo.AppUndoableAction;
import net.thevpc.echo.UndoableAction;
import net.thevpc.echo.swing.actions.PropUndoableAction;
import net.thevpc.common.msg.Message;
import net.thevpc.common.props.FileObject;
import net.thevpc.scholar.hadruwaves.project.HWProject;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;

/**
 *
 * @author vpc
 */
public class SelectProjectAction extends AppUndoableAction {
    HWSSolutionExplorerTool outer;

    public SelectProjectAction(HWSSolutionExplorerTool outer) {
        super(outer.app(), "SelectProject");
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
                () -> getSelectedProject(),
                () -> outer.studio().proc().solution().get().activeProject(),
                "Select project {0}"
        );
    }

    @Override
    public void refresh() {
        Object i = getSelectedProject();
        setAccessible(i != null  && i!=outer.studio().proc().solution().get().activeProject().get());
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
