/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions;

import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import net.vpc.common.props.impl.PropsHelper;
import net.vpc.common.app.AppEvent;
import net.vpc.common.msg.FormattedMessage;
import net.vpc.common.app.UndoableAction;
import net.vpc.scholar.hadruwaves.project.*;
import net.vpc.common.app.AppUndoableAction;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;
import net.vpc.common.msg.Message;

/**
 *
 * @author vpc
 */
public class NewProjectUndoableAction extends AppUndoableAction {

    private final HWSSolutionExplorerTool explorer;

    public NewProjectUndoableAction(final HWSSolutionExplorerTool outer) {
        super(outer.app(), "NewProject");
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
        return new UndoableAction() {
            HWProject created = null;

            @Override
            public Message doAction(AppEvent event) {
                HWProjectItem[] s = explorer.getSelectedItems();
                if (s.length == 1) {
                    Object v = s[0].getItemValue();
                    if (v instanceof HWSolution) {
                        created = ((HWSolution) v).addProject(null, "/");
                        return new FormattedMessage(Level.INFO, "Add project {0}", new Object[]{created.name().get()});
                    } else if (v instanceof DefaultHWSolutionFolder) {
                        HWSolutionFolder f = (HWSolutionFolder) v;
                        created = ((HWSolution) v).addProject(null, PropsHelper.buildPath(f.parentPath().get() + "/" + f.name()));
                        return new FormattedMessage(Level.INFO, "Add project {0}", new Object[]{created.name().get()});
                    }
                    explorer.refreshTools();
                }
                return null;
            }

            @Override
            public void undoAction(AppEvent event) {
                if (created != null) {
                    explorer.refreshTools();
                }
            }

            @Override
            public void redoAction(AppEvent event) {

            }
        };
    }

    @Override
    public void refresh() {
        Object[] oo = getNewProjectItems();
        setAccessible(oo.length == 1);
    }

    public Object[] getNewProjectItems() {
        java.util.List<Object> all = new ArrayList();
        for (Object selectedItem : explorer.getSelectedItemValues()) {
            if ((selectedItem instanceof HWSolution)
                    || (selectedItem instanceof DefaultHWSolutionFolder)) {
                all.add(selectedItem);
            } else {
                break;
            }
        }
        return all.toArray(new Object[0]);
    }

}
