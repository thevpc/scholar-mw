/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions;

import net.thevpc.echo.AppEvent;
import net.thevpc.echo.AppUndoableAction;
import net.thevpc.echo.UndoableAction;
import net.thevpc.common.msg.Message;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.components.HWProjectSource;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 *
 * @author vpc
 */
public class BuildNodeAction extends AppUndoableAction {


    HWSSolutionExplorerTool outer;

    public BuildNodeAction(HWSSolutionExplorerTool outer) {
        super(outer.app(), "Build");
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
        HWProjectSource i = getSelected();
        setAccessible(i != null);
    }

    private HWProjectSource getSelected() {
        return (HWProjectSource) outer.getSelectedItemsOneValue(x->{
            if(x.getItem() instanceof HWProjectSource){
                HWProjectSource s=(HWProjectSource) x.getItem();
                if(s.path.equals("/java")){
                    return true;
                }
                if(s.path.equals("/scala")){
                    return true;
                }
                if(s.path.equals("/hadra")){
                    return true;
                }
            }
            return false;
        });
    }


}
