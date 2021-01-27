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
import net.thevpc.echo.AppEvent;
import net.thevpc.common.msg.FormattedMessage;
import net.thevpc.echo.UndoableAction;
import net.thevpc.echo.AppUndoableAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;
import net.thevpc.scholar.hadruwaves.project.HWProjectComponent;
import net.thevpc.common.msg.Message;

/**
 *
 * @author vpc
 */
public class NonVisibleUndoableAction extends AppUndoableAction {

    private final HWSSolutionExplorerTool explorer;

    public NonVisibleUndoableAction(final HWSSolutionExplorerTool outer) {
        super(outer.app(), "NonVisible");
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
            java.util.List<Object> toProcess = new ArrayList<Object>();

            @Override
            public Message doAction(AppEvent event) {
                toProcess.addAll(Arrays.asList(getDisablableItems()));
                for (Object enablableItem : toProcess) {
                    if (enablableItem instanceof HWProjectComponent) {
                        ((HWProjectComponent) enablableItem).visible().set(false);
                        explorer.refreshTools();
                    }
                }
                return new FormattedMessage(Level.INFO, "Disable {0}", new Object[]{String.join(",", toProcess.stream().map((x) -> x.toString()).collect(Collectors.toList()))});
            }

            @Override
            public void undoAction(AppEvent event) {
                for (Object enablableItem : toProcess) {
                    if (enablableItem instanceof HWProjectComponent) {
                        ((HWProjectComponent) enablableItem).visible().set(true);
                        explorer.refreshTools();
                    }
                }
            }
@Override
            public void redoAction(AppEvent event) {
            
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
                boolean e = o.visible().get();
                if (e) {
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
