/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions;

import java.awt.Component;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import net.thevpc.echo.AppEvent;
import net.thevpc.common.msg.JFormattedMessage;
import net.thevpc.echo.UndoableAction;
import net.thevpc.echo.AppUndoableAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;
import net.thevpc.scholar.hadruwaves.project.scene.HWProjectComponentGroup;
import net.thevpc.common.msg.Message;

/**
 *
 * @author vpc
 */
public class NewMaterial3DBoxAction extends AppUndoableAction {

    private final HWSSolutionExplorerTool explorer;

    public NewMaterial3DBoxAction(final HWSSolutionExplorerTool outer) {
        super(outer.app(), "NewMaterial3DBox");
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
            @Override
            public Message doAction(AppEvent event) {
                HWProjectItem t = getItem();
                Object itemValue = t.getItemValue();
                if (itemValue instanceof HWProjectComponentGroup) {

                } else {

                }
                JOptionPane.showConfirmDialog((Component) explorer.app().mainWindow().get().component(), "Will Enter values here");
                explorer.refreshTools();
                return new JFormattedMessage(Level.INFO, "Add {0}", new Object[]{"Polygon"});
            }

            @Override
            public void undoAction(AppEvent event) {
                explorer.refreshTools();
            }

            @Override
            public void redoAction(AppEvent event) {

            }
        };
    }

    @Override
    public void refresh() {
        HWProjectItem oo = getItem();
        setAccessible(oo != null);
    }

    public HWProjectItem getItem() {
        java.util.List<HWProjectItem> all = new ArrayList();
        for (HWProjectItem selectedItem : explorer.getSelectedItems()) {
            if (selectedItem.isProjectFolder("Volumes") || selectedItem.isElementGroup()) {
                all.add(selectedItem);
            }
        }
        if (all.size() == 1) {
            return all.get(0);
        }
        return null;
    }
}
