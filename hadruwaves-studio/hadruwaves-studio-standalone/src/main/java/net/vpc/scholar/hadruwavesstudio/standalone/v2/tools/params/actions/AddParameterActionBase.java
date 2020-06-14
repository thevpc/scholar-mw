/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.params.actions;

import net.vpc.common.app.AppEvent;
import net.vpc.common.msg.FormattedMessage;
import net.vpc.common.app.UndoableAction;
import net.vpc.common.app.AppUndoableAction;
import net.vpc.scholar.hadruwaves.project.parameter.HWParameterFolder;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.params.HWSProjectParametersTool;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.util.logging.Level;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwaves.project.parameter.HWParameterElement;
import net.vpc.common.msg.Message;

/**
 * @author vpc
 */
public abstract class AddParameterActionBase extends AppUndoableAction {

    HWSProjectParametersTool outer;

    public AddParameterActionBase(HWSProjectParametersTool outer, String id) {
        super(outer.app(), id);
        this.outer = outer;
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
            HWParameterElement added;

            @Override
            public Message doAction(AppEvent event) {
                if (added == null) {
                    Object selectedElement = getSelectedElement();
                    added = read();
                    if (added != null) {
                        if (selectedElement instanceof HWParameterFolder) {
                            HWParameterFolder folder = (HWParameterFolder) selectedElement;
                            folder.children().add(added);
                            outer.updateRoot();
                            return new FormattedMessage(Level.INFO, "Created {0}", new Object[]{added.name().get()});
                        } else if (selectedElement instanceof HWConfigurationRun) {
                            HWConfigurationRun p = (HWConfigurationRun) selectedElement;
                            p.project().get().parameters().children().add(added);
                            outer.updateRoot();
                            return new FormattedMessage(Level.INFO, "Created {0}", new Object[]{added.name().get()});
                        }
                    }
                    return null;
                } else {
                    return new FormattedMessage(Level.INFO, "Created {0}", new Object[]{added.name().get()});
                }
            }

            @Override
            public void undoAction(AppEvent event) {
                if (added != null) {
                    added.remove();
                    outer.updateRoot();
                }
            }

            @Override
            public void redoAction(AppEvent event) {

            }
        };
    }

    protected abstract HWParameterElement read();

    @Override
    public void refresh() {
        Object i = getSelectedElement();
        setAccessible(i != null);
    }

    private Object getSelectedElement() {
        Object[] selectedItems = outer.getSelectedElements();
        if (selectedItems.length == 1) {
            Object s = selectedItems[0];
            if (s instanceof HWConfigurationRun) {
                HWConfigurationRun ss = (HWConfigurationRun) s;
                if (ss.project().get() == null) {
                    return null;
                }
            }
            return s;
        }
        return null;
    }

}
