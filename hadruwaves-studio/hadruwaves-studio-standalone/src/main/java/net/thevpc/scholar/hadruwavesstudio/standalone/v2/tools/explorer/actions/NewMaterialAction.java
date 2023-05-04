/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions;

import java.awt.Component;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import net.thevpc.echo.api.AppEvent;
import net.thevpc.common.msg.JFormattedMessage;
import net.thevpc.echo.api.UndoableAction;
import net.thevpc.scholar.hadruwaves.project.HWProject;
import net.thevpc.scholar.hadruwaves.project.scene.HWMaterialTemplate;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;
import net.thevpc.common.msg.Message;

/**
 *
 * @author vpc
 */
public class NewMaterialAction extends HWUnduableAction {

    private final HWSSolutionExplorerTool explorer;

    public NewMaterialAction(final HWSSolutionExplorerTool outer) {
        super(outer.app(), "NewMaterial");
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
            HWMaterialTemplate created = null;
            HWProject parent = null;
            HWProjectItem oo;
            @Override
            public Message doAction(AppEvent event) {
                oo = getSelectedItem();
                if (oo != null) {
                    String s = JOptionPane.showInputDialog((Component) explorer.app().mainFrame().get().peer().toolkitComponent(), "New Name");
                    if (s != null) {
                        HWMaterialTemplate old = oo.getProject().materials().get(s);
                        if (old != null) {
                            JOptionPane.showConfirmDialog((Component) explorer.app().mainFrame().get().peer().toolkitComponent(), "Name exists");
                        } else {
                            HWMaterialTemplate m = new HWMaterialTemplate(oo.getProject());
                            m.name().set(s);
                            oo.getProject().materials().add(m);
                            parent = oo.getProject();
                            created = m;
                            explorer.reload(oo);
                            return new JFormattedMessage(Level.INFO, "Add Material {0}", new Object[]{created.name().get()});
                        }
                        explorer.refreshTools();
                    }
                }
                return null;
            }

            @Override
            public void undoAction(AppEvent event) {
                if (created != null) {
                    oo.getProject().materials().remove(created.name().get());
                    explorer.refreshTools();
                }
            }

            @Override
            public void redoAction(AppEvent event) {
                oo.getProject().materials().add(created);
                explorer.reload(oo);
            }
        };
    }

    @Override
    public void refresh() {
        HWProjectItem oo = getSelectedItem();
        setAccessible(oo != null);
    }

    public HWProjectItem getSelectedItem() {
        return explorer.getSelectedItemsOne(x -> x.isProjectFolder("Materials"));
    }

}
