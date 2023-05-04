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
import net.thevpc.echo.api.AppEvent;
import net.thevpc.common.msg.JFormattedMessage;
import net.thevpc.echo.api.UndoableAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.components.HWProjectFolder;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;
import net.thevpc.scholar.hadruwaves.project.scene.HWProjectComponentGroup;
import net.thevpc.common.msg.Message;

/**
 *
 * @author vpc
 */
public class NewPlanarPortRectangleAction extends HWUnduableAction {

    private final HWSSolutionExplorerTool explorer;

    public NewPlanarPortRectangleAction(final HWSSolutionExplorerTool outer) {
        super(outer.app(), "NewPlanarPortRectangle");
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
                explorer.refreshTools();
                JOptionPane.showConfirmDialog((Component) explorer.app().mainFrame().get().peer().toolkitComponent(), "Will Enter values here");
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
            if (isComponentsFolder(selectedItem) || isStructureNodeGroup(selectedItem)) {
                all.add(selectedItem);
            }
        }
        if (all.size() == 1) {
            return all.get(0);
        }
        return null;
    }

    public static boolean isStructureNodeGroup(HWProjectItem selectedItem) {
        return selectedItem.getItemValue() instanceof HWProjectComponentGroup;
    }

    private static boolean isComponentsFolder(HWProjectItem selectedItem) {
        return selectedItem.getItemValue() instanceof HWProjectFolder && ((HWProjectFolder) (selectedItem.getItemValue())).path.equals("/Sources");
    }

}
