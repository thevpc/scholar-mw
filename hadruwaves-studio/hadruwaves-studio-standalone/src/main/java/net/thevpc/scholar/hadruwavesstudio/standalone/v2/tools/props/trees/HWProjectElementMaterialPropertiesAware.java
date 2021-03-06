/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.trees;

import net.thevpc.echo.impl.PValueViewProperty;
import net.thevpc.scholar.hadruwaves.project.scene.HWMaterialTemplate;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.PValueViewPropertyE;
import net.thevpc.echo.impl.DefaultPropertiesNodeFolder;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.DefaultHWPropertiesTree;
import net.thevpc.scholar.hadruwaves.project.scene.HWProjectPiece;

/**
 *
 * @author vpc
 */
public class HWProjectElementMaterialPropertiesAware extends DefaultHWPropertiesTree {

    private final HWProjectPiece wp;
    private final HWProjectItem outer;
    private DefaultPropertiesNodeFolder root;

    public HWProjectElementMaterialPropertiesAware(HWProjectPiece wp, final HWProjectItem outer) {
        super(outer);
        this.outer = outer;
        this.wp = wp;

        refresh();
    }

    public void refresh() {
        root = new DefaultPropertiesNodeFolder("folder", "Project Element Material", wp);
        setRoot(root);
        DefaultPropertiesNodeFolder folder = root.addFolder("folder", "General");
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.name()));
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.visible()));
        folder.add(new PValueViewPropertyE("property", wp.enabled()));
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.description()));
        HWMaterialTemplate[] vals = outer.getProject().materials().values().stream().toArray(HWMaterialTemplate[]::new);
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.material(), vals));
    }

}
