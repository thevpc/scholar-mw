/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.trees;

import net.thevpc.echo.swing.core.PValueViewProperty;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.PValueViewPropertyE;
import net.thevpc.echo.swing.core.DefaultPropertiesNodeFolder;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.DefaultHWPropertiesTree;
import net.thevpc.scholar.hadruwaves.project.HWProjectComponent;

/**
 *
 * @author vpc
 */
public class HWProjectElementPropertiesAware extends DefaultHWPropertiesTree {

    private final HWProjectComponent wp;
    private final HWProjectItem outer;
    private DefaultPropertiesNodeFolder root;

    public HWProjectElementPropertiesAware(HWProjectComponent wp, final HWProjectItem outer) {
        super(outer);
        this.outer = outer;
        this.wp = wp;

        refresh();
    }

    public void refresh() {
        root = new DefaultPropertiesNodeFolder("folder",
                "Project Element",
                wp);
        setRoot(root);
        DefaultPropertiesNodeFolder folder = root.addFolder("folder", "General");
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.name()));
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.visible()));
        folder.add(new PValueViewPropertyE("property", wp.enabled()));
    }

}
