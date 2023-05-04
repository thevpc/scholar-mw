/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import net.thevpc.echo.api.AppEvent;
import net.thevpc.common.msg.JFormattedMessage;
import net.thevpc.echo.api.UndoableAction;
import net.thevpc.scholar.hadruwaves.Material;
import net.thevpc.scholar.hadruwaves.project.DefaultHWSolutionFolder;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationFolder;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwaves.project.scene.HWMaterialTemplate;
import net.thevpc.scholar.hadruwaves.project.HWSolutionElement;
import net.thevpc.scholar.hadruwaves.project.HWProject;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationElement;
import net.thevpc.scholar.hadruwaves.project.HWProjectComponent;
import net.thevpc.common.msg.Message;

/**
 *
 * @author vpc
 */
public class RemoveUndoableAction extends HWUnduableAction {

    private final HWSSolutionExplorerTool explorer;

    public RemoveUndoableAction(final HWSSolutionExplorerTool outer) {
        super(outer.app(), "Remove");
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
            java.util.List<HWProjectItem> toProcess = new ArrayList<HWProjectItem>();

            @Override
            public Message doAction(AppEvent event) {
                toProcess.clear();
                toProcess.addAll(Arrays.asList(explorer.getSelectedItems()));
                if (toProcess.size() > 0) {
                    String s = toProcess.stream().map(x -> x.getItemValue()).map(x -> x.toString()).collect(Collectors.joining(","));
                    int ov = JOptionPane.showConfirmDialog(getMainComponent(), "Are you sure you want to remove " + s, "Attention", JOptionPane.OK_CANCEL_OPTION);
                    if (ov == JOptionPane.OK_OPTION) {
                        for (HWProjectItem item : toProcess) {
                            Object t = item.getItemValue();
                            if (t instanceof HWMaterialTemplate) {
                                HWMaterialTemplate mt = (HWMaterialTemplate) t;
                                item.getProject().materials().remove(mt.name().get());
                                explorer.reload(item.getTreePath().getParentPath());
                            } else if (t instanceof HWProjectComponent) {
                                HWProjectComponent mt = (HWProjectComponent) t;
                                List<HWProjectComponent> a = item.getProject().scene().get().removeDeepComponents((x) -> x == mt);
                                if (a.size() == 1) {
                                    explorer.reload(item.getTreePath().getParentPath());
                                } else {
                                    explorer.reload();
                                }
                            } else if (t instanceof HWSolutionElement) {
                                HWSolutionElement mt = (HWSolutionElement) t;
                                List<HWProjectComponent> a = item.getProject().scene().get().removeDeepComponents((x) -> x == mt);
                                if (a.size() == 1) {
                                    explorer.reload(item.getTreePath().getParentPath());
                                } else {
                                    explorer.reload();
                                }
                            } else if (t instanceof HWConfigurationElement) {
                                HWConfigurationElement mt = (HWConfigurationElement) t;
                                List<HWConfigurationElement> a = item.getProject().configurations().removeDeepComponents((x) -> x == mt, false);
                                if (a.size() == 1) {
                                    explorer.reload(item.getTreePath().getParentPath());
                                } else {
                                    explorer.reload();
                                }
                            }
                        }
                        explorer.refreshTools();
                        return new JFormattedMessage(Level.INFO, "Remove {0}", new Object[]{String.join(",", toProcess.stream().map((x) -> x.toString()).collect(Collectors.toList()))});
                    }
                }
                return null;
            }

            @Override
            public void undoAction(AppEvent event) {
                for (Object enablableItem : toProcess) {
                    //
                }
                explorer.refreshTools();
            }
@Override
            public void redoAction(AppEvent event) {
            
            }
                    };
    }

    @Override
    public void refresh() {
        Object[] oo = getRemovableItems();
        setAccessible(oo.length > 0);
    }

    public Object[] getRemovableItems() {
        java.util.List<Object> all = new ArrayList();
        for (Object selectedItem : explorer.getSelectedItemValues()) {
            if ((selectedItem instanceof HWProjectComponent)
                    || (selectedItem instanceof DefaultHWSolutionFolder)
                    || (selectedItem instanceof HWProject)
                    || (selectedItem instanceof HWMaterialTemplate)
                    || (selectedItem instanceof Material)
                    || (selectedItem instanceof HWConfigurationFolder)
                    || (selectedItem instanceof HWConfigurationRun && !"default".equals(((HWConfigurationRun) selectedItem).name().get()))) {
                all.add(selectedItem);
            } else {
                break;
            }
        }
        return all.toArray(new Object[0]);
    }

}
