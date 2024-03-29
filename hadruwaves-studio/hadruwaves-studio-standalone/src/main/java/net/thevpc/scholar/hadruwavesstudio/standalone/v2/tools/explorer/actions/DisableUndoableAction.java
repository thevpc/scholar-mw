/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import net.thevpc.echo.api.AppEvent;
import net.thevpc.common.msg.JFormattedMessage;
import net.thevpc.echo.api.UndoableAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;
import net.thevpc.scholar.hadruwaves.project.HWProjectComponent;
import net.thevpc.common.msg.Message;

/**
 *
 * @author vpc
 */
public class DisableUndoableAction extends HWUnduableAction {

    private final HWSSolutionExplorerTool explorer;

    public DisableUndoableAction(final HWSSolutionExplorerTool toolWinPanel) {
        super(toolWinPanel.app(), "Disable");
        this.explorer = toolWinPanel;
        toolWinPanel.tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                refresh();
            }
        });
    }

    @Override
    protected UndoableAction createUndo() {
        return new UndoableAction() {
            java.util.List<Object> toProcess = new ArrayList<Object>();

            @Override
            public Message doAction(AppEvent event) {
                toProcess.addAll(Arrays.asList(getDisablableItems()));
                for (Object enablableItem : toProcess) {
                    if (enablableItem instanceof HWProjectComponent) {
                        ((HWProjectComponent) enablableItem).enabled().set("false");
                        explorer.refreshTools();
                    }
                }
                return new JFormattedMessage(Level.INFO, "Disable {0}", new Object[]{String.join(",", toProcess.stream().map((x) -> x.toString()).collect(Collectors.toList()))});
            }

            @Override
            public void undoAction(AppEvent event) {
                for (Object enablableItem : toProcess) {
                    if (enablableItem instanceof HWProjectComponent) {
                        ((HWProjectComponent) enablableItem).enabled().set("true");
                        explorer.refreshTools();
                    }
                }
            }

            @Override
            public void redoAction(AppEvent event) {
                for (Object enablableItem : toProcess) {
                    if (enablableItem instanceof HWProjectComponent) {
                        ((HWProjectComponent) enablableItem).enabled().set("false");
                        explorer.refreshTools();
                    }
                }
            }

        };
    }

    @Override
    public void refresh() {
        Object[] oo = getDisablableItems();
        setAccessible(oo.length > 0);
    }

    public Object[] getDisablableItems() {
        java.util.List<HWProjectComponent> all = new ArrayList();
        for (Object selectedItem : explorer.getSelectedItemValues()) {
            if (selectedItem instanceof HWProjectComponent) {
                HWProjectComponent o = (HWProjectComponent) selectedItem;
                String e = o.enabled().get();
                if (e == null || e.trim().isEmpty() || e.trim().equalsIgnoreCase("true")) {
                    all.add(o);
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return all.toArray(new Object[0]);
    }

}
