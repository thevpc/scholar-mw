/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.trees;

import net.thevpc.echo.impl.PValueViewProperty;
import net.thevpc.echo.impl.PValueViewPropertyReadOnly;
import net.thevpc.scholar.hadruwaves.Boundary;
import net.thevpc.scholar.hadruwaves.project.scene.HWProjectElementBrickFace;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;
import net.thevpc.echo.impl.DefaultPropertiesNodeFolder;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.DefaultHWPropertiesTree;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.PValueViewPropertyE;

/**
 *
 * @author vpc
 */
public class MaterialBrickFacePropertiesAware extends DefaultHWPropertiesTree {

    private final HWProjectElementBrickFace wp;
    private final HWProjectItem outer;
    private DefaultPropertiesNodeFolder root;

    public MaterialBrickFacePropertiesAware(HWProjectElementBrickFace wp, final HWProjectItem outer) {
        super(outer);
        this.outer = outer;
        this.wp = wp;

        refresh();
    }

    public void refresh() {
        root = new DefaultPropertiesNodeFolder("folder", "Box", wp);
        setRoot(root);
        DefaultPropertiesNodeFolder folder = root.addFolder("folder", "General");
        folder.add(new PValueViewPropertyReadOnly("property", outer.getStudio().app(), "name", wp.name()));
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.visible()));
        folder = root.addFolder("folder", "Values");
        folder.add(new PValueViewPropertyE("property", wp.boundary()));
    }

}
