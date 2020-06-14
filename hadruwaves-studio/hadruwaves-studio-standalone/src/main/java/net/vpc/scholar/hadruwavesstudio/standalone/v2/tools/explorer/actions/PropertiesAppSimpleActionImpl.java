/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import net.vpc.common.app.AbstractAppAction;
import net.vpc.common.app.AppDockingWorkspace;
import net.vpc.common.app.Application;
import net.vpc.scholar.hadruwaves.Material;
import net.vpc.scholar.hadruwaves.project.HWSolutionElement;
import net.vpc.scholar.hadruwaves.project.scene.HWMaterialTemplate;
import net.vpc.scholar.hadruwaves.project.scene.HWProjectElementBrickFace;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationElement;
import net.vpc.scholar.hadruwaves.project.HWProjectComponent;

/**
 *
 * @author vpc
 */
public class PropertiesAppSimpleActionImpl extends AbstractAppAction {

    private final HWSSolutionExplorerTool explorer;

    public PropertiesAppSimpleActionImpl(Application aplctn, final HWSSolutionExplorerTool outer) {
        super(aplctn, "Properties");
        this.explorer = outer;
        outer.tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                setAccessible(getShowPropsItem() != null);
            }
        });
    }

    public Object getShowPropsItem() {
        java.util.List<Object> all = new ArrayList();
        for (Object selectedItem : explorer.getSelectedItemValues()) {
            if ((selectedItem instanceof HWProjectComponent)
                    || (selectedItem instanceof HWSolutionElement)
                    || (selectedItem instanceof HWMaterialTemplate)
                    || (selectedItem instanceof Material)
                    || (selectedItem instanceof HWProjectElementBrickFace)
                    || (selectedItem instanceof HWConfigurationElement)
                    )  {
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

    @Override
    protected void actionPerformedImpl(ActionEvent e) {
        AppDockingWorkspace ws = (AppDockingWorkspace) getApplication().mainWindow().get().workspace().get();
        //TODO
                explorer.refreshTools();
        ws.toolWindows().get("Properties").active().set(true);
    }
    @Override
    public void refresh() {

    }

}
