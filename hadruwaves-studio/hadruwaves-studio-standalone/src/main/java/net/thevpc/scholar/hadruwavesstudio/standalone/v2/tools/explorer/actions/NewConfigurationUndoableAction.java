/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions;

import java.util.ArrayList;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import net.thevpc.echo.api.AppEvent;
import net.thevpc.echo.api.UndoableAction;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationFolder;
import net.thevpc.echo.swing.helpers.actions.SwingAppUndoableAction;
import net.thevpc.scholar.hadruwaves.project.HWProject;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.components.HWProjectFolder;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;
import net.thevpc.common.msg.Message;

/**
 *
 * @author vpc
 */
public class NewConfigurationUndoableAction extends SwingAppUndoableAction {

    private final HWSSolutionExplorerTool explorer;

    public NewConfigurationUndoableAction(final HWSSolutionExplorerTool outer) {
        super(outer.app(), "NewConfiguration");
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
                    if (v instanceof HWProjectFolder && ((HWProjectFolder) v).path.equals("/Configurations")) {
                    } else if (v instanceof HWConfigurationFolder) {
                    }
                    //                    if (v instanceof HWSolution) {
                    //                        created = ((HWSolution) v).addProject(null, "/");
                    //                        return new FormattedAppMessage(Level.INFO, "Add Configuration {0}", new Object[]{created.name().get()});
                    //                    } else if (v instanceof HWSolutionFolder) {
                    //                        HWSolutionFolder f = (HWSolutionFolder) v;
                    //                        created = ((HWSolution) v).addProject(null, PropsHelper.buildPath(f.parentPath().get() + "/" + f.name()));
                    //                        return new FormattedAppMessage(Level.INFO, "Add Configuration {0}", new Object[]{created.name().get()});
                    //                    }
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
        Object[] oo = getNewConfigurationItems();
        setAccessible(oo.length == 1);
    }

    public Object[] getNewConfigurationItems() {
        java.util.List<Object> all = new ArrayList();
        for (Object selectedItem : explorer.getSelectedItemValues()) {
            if ((selectedItem instanceof HWProjectFolder && ((HWProjectFolder) selectedItem).path.equals("/Configurations"))
                    || (selectedItem instanceof HWConfigurationFolder)) {
                all.add(selectedItem);
            } else {
                break;
            }
        }
        return all.toArray(new Object[0]);
    }

}
