/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.params.actions;

import net.thevpc.echo.api.AppEvent;
import net.thevpc.common.msg.JFormattedMessage;
import net.thevpc.echo.api.UndoableAction;
import net.thevpc.scholar.hadruwaves.project.HWProject;
import net.thevpc.scholar.hadruwaves.project.parameter.HWParameterFolder;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions.HWUnduableAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.params.HWSProjectParametersTool;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.util.logging.Level;
import net.thevpc.scholar.hadruwaves.project.parameter.HWParameterElement;
import net.thevpc.common.msg.Message;

/**
 * @author vpc
 */
public class RemoveParameterOrFolderAction extends HWUnduableAction {

    HWSProjectParametersTool toolWindow;

    public RemoveParameterOrFolderAction(HWSProjectParametersTool outer) {
        super(outer.studio().app(), "RemoveParameter");
        this.toolWindow = outer;
        outer.tree().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                refresh();
            }
        });

    }

    @Override
    protected UndoableAction createUndo() {
        return new UndoableAction() {
            HWParameterElement removed;
            HWParameterFolder parent;
            HWProject project;

            @Override
            public Message doAction(AppEvent event) {
                removed = (HWParameterElement) getSelectedElement();
                if (removed != null) {
                    parent = removed.parent().get();
                    project = removed.project().get();
                    removed.remove();
                    toolWindow.updateRoot();
                    return new JFormattedMessage(Level.WARNING, "Removed {0}", new Object[]{removed.name().get()});
                }
                return null;
            }

            @Override
            public void undoAction(AppEvent event) {
                if (removed != null) {
                    if (parent != null) {
                        parent.children().add(removed);
                    } else {
                        project.parameters().children().add(removed);
                        toolWindow.updateRoot();
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
        Object i = getSelectedElement();
        setAccessible(i != null);
    }

    private Object getSelectedElement() {
        Object[] selectedItems = toolWindow.getSelectedElements();
        if (selectedItems.length == 1) {
            Object s = selectedItems[0];
            if (s instanceof HWParameterElement) {
                return s;
            }
        }
        return null;
    }

}
