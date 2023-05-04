/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import net.thevpc.echo.Application;
import net.thevpc.echo.api.components.AppComponent;
import net.thevpc.echo.api.components.AppContainer;
import net.thevpc.scholar.hadruwaves.Material;
import net.thevpc.scholar.hadruwaves.project.HWSolutionElement;
import net.thevpc.scholar.hadruwaves.project.scene.HWMaterialTemplate;
import net.thevpc.scholar.hadruwaves.project.scene.HWProjectElementBrickFace;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationElement;
import net.thevpc.scholar.hadruwaves.project.HWProjectComponent;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.actions.HAction;

/**
 *
 * @author vpc
 */
public class PropertiesSimpleActionImpl extends HAction {

    private final HWSSolutionExplorerTool explorer;

    public PropertiesSimpleActionImpl(Application aplctn, final HWSSolutionExplorerTool outer) {
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
        AppContainer ws = (AppContainer) getApplication().mainFrame().get().content().get();
        //TODO
                explorer.refreshTools();
        ws.children().get("Properties").active().set(true);
    }
    @Override
    public void refresh() {

    }

}
