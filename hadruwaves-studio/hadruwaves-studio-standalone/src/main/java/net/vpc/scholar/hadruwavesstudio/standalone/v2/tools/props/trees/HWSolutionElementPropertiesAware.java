/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.props.trees;

import net.vpc.common.app.swing.core.PValueViewProperty;
import net.vpc.scholar.hadruwaves.project.DefaultHWSolutionFolder;
import net.vpc.scholar.hadruwaves.project.HWSolutionElement;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;
import net.vpc.common.app.swing.core.DefaultPropertiesNodeFolder;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.util.DefaultHWPropertiesTree;

/**
 *
 * @author vpc
 */
public class HWSolutionElementPropertiesAware extends DefaultHWPropertiesTree {

    private final HWSolutionElement wp;
    private final HWProjectItem outer;
    private DefaultPropertiesNodeFolder root;

    public HWSolutionElementPropertiesAware(HWSolutionElement wp, final HWProjectItem outer) {
        super(outer);
        this.outer = outer;
        this.wp = wp;

        refresh();
    }

    public void refresh() {

        root = new DefaultPropertiesNodeFolder("folder",
                (wp instanceof DefaultHWSolutionFolder)
                        ? "Solution Folder"
                        : "Solution Element", wp);
        setRoot(root);
        DefaultPropertiesNodeFolder folder = root.addFolder("folder", "General");
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.name()));
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.description()));
    }

}
