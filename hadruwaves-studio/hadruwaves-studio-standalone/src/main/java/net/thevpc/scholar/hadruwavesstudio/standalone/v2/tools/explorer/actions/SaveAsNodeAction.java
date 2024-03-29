/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions;

import net.thevpc.echo.api.AppEvent;
import net.thevpc.echo.api.UndoableAction;
import net.thevpc.common.msg.Message;
import net.thevpc.common.props.FileObject;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 *
 * @author vpc
 */
public class SaveAsNodeAction extends HWUnduableAction {


    HWSSolutionExplorerTool outer;

    public SaveAsNodeAction(HWSSolutionExplorerTool outer) {
        super(outer.app(), "SaveAs");
        this.outer = outer;

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
                FileObject i = getSelectedFileObject();
                if(i!=null){
                    outer.studio().saveFileObjectAs2((FileObject) i);
                }
                return null;
            }

            @Override
            public void redoAction(AppEvent event) {

            }

            @Override
            public void undoAction(AppEvent event) {

            }
        };
    }

    @Override
    public void refresh() {
        FileObject i = getSelectedFileObject();
        setAccessible(i != null);
    }

    private FileObject getSelectedFileObject() {
        return (FileObject) outer.getSelectedItemsOneValue(x->x.getItem() instanceof FileObject);
    }


}
