/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions;

import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import net.vpc.common.props.Props;
import net.vpc.common.props.WritablePNamedNode;
import net.vpc.common.app.AppEvent;
import net.vpc.common.msg.FormattedMessage;
import net.vpc.common.app.UndoableAction;
import net.vpc.scholar.hadruwaves.project.*;
import net.vpc.common.app.AppUndoableAction;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;
import net.vpc.common.msg.Message;

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
                        return new FormattedMessage(Level.INFO, "New Solution Folder {0}", new Object[]{folderName});
                    } else if (parent.getItemValue() instanceof DefaultHWSolutionFolder) {
                        WritablePNamedNode<HWSolutionElement> newNode = Props.of(folderName).nnodeOf(HWSolutionElement.class, added);
                        WritablePNamedNode<HWSolutionElement> parentNode = (WritablePNamedNode<HWSolutionElement>) parent.getItem();
                        parentNode.children().put(folderName, newNode);
                        return new FormattedMessage(Level.INFO, "New Solution Folder {0}", new Object[]{folderName});
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
                    WritablePNamedNode<HWSolutionElement> parentNode = (WritablePNamedNode<HWSolutionElement>) parent.getItem();
                    parentNode.children().remove(folderName);
                }
                explorer.refreshTools();
            }

            @Override
            public void redoAction(AppEvent event) {
                if (parent.getItemValue() instanceof HWSolution) {
                    ((HWSolution) parent.getItemValue()).children().add(added);
                } else if (parent.getItemValue() instanceof DefaultHWSolutionFolder) {
                    WritablePNamedNode<HWSolutionElement> newNode = Props.of(folderName).nnodeOf(HWSolutionElement.class, added);
                    WritablePNamedNode<HWSolutionElement> parentNode = (WritablePNamedNode<HWSolutionElement>) parent.getItem();
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
