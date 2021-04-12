/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions;

import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritableNamedNode;
import net.thevpc.echo.AppEvent;
import net.thevpc.common.msg.JFormattedMessage;
import net.thevpc.echo.UndoableAction;
import net.thevpc.scholar.hadruwaves.project.*;
import net.thevpc.echo.AppUndoableAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;
import net.thevpc.common.msg.Message;

/**
 *
 * @author vpc
 */
public class NewFolderUndoableAction extends AppUndoableAction {

    private final HWSSolutionExplorerTool explorer;

    public NewFolderUndoableAction(final HWSSolutionExplorerTool outer) {
        super(outer.app(), "NewFolder");
        this.explorer = outer;
        this.explorer.tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                refresh();
            }
        });
    }

    @Override
    protected UndoableAction createUndo() {
        return new UndoableAction() {
            String folderName;
            HWProjectItem parent;
            DefaultHWSolutionFolder added;

            @Override
            public Message doAction(AppEvent event) {
                folderName = JOptionPane.showInputDialog(null, "Folder Name");
                if (folderName != null) {
                    parent = getNewFolderItem();
                    added = new DefaultHWSolutionFolder(folderName);
                    if (parent.getItemValue() instanceof HWSolution) {
                        ((HWSolution) parent.getItemValue()).children().add(added);
                        return new JFormattedMessage(Level.INFO, "New Solution Folder {0}", new Object[]{folderName});
                    } else if (parent.getItemValue() instanceof DefaultHWSolutionFolder) {
                        WritableNamedNode<HWSolutionElement> newNode = Props.of(folderName).nnodeOf(HWSolutionElement.class, added);
                        WritableNamedNode<HWSolutionElement> parentNode = (WritableNamedNode<HWSolutionElement>) parent.getItem();
                        parentNode.children().put(folderName, newNode);
                        return new JFormattedMessage(Level.INFO, "New Solution Folder {0}", new Object[]{folderName});
                    }
                    explorer.refreshTools();
                }
                return null;
            }

            @Override
            public void undoAction(AppEvent event) {
                if (parent.getItemValue() instanceof HWSolution) {
                    ((HWSolution) parent.getItemValue()).children().remove(folderName);
                } else if (parent.getItemValue() instanceof DefaultHWSolutionFolder) {
                    WritableNamedNode<HWSolutionElement> parentNode = (WritableNamedNode<HWSolutionElement>) parent.getItem();
                    parentNode.children().remove(folderName);
                }
                explorer.refreshTools();
            }

            @Override
            public void redoAction(AppEvent event) {
                if (parent.getItemValue() instanceof HWSolution) {
                    ((HWSolution) parent.getItemValue()).children().add(added);
                } else if (parent.getItemValue() instanceof DefaultHWSolutionFolder) {
                    WritableNamedNode<HWSolutionElement> newNode = Props.of(folderName).nnodeOf(HWSolutionElement.class, added);
                    WritableNamedNode<HWSolutionElement> parentNode = (WritableNamedNode<HWSolutionElement>) parent.getItem();
                    parentNode.children().put(folderName, newNode);
                }
            }

        };
    }

    @Override
    public void refresh() {
        Object i = null;
        i = getNewFolderItem();
        setAccessible(i != null);
    }

    public HWProjectItem getNewFolderItem() {
        java.util.List<HWProjectItem> all = new ArrayList();
        for (HWProjectItem selectedItem : explorer.getSelectedItems()) {
            if ((selectedItem.getItemValue() instanceof HWSolution)
                    || (selectedItem.getItemValue() instanceof DefaultHWSolutionFolder)) {
                all.add(selectedItem);
            } else {
                break;
            }
        }
        if (all.size() == 1) {
            return all.get(0);
        }
        return null;
    }

}
