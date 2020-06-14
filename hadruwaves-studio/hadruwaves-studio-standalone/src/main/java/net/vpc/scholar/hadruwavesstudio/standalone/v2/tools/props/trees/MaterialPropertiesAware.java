/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.props.trees;

import net.vpc.common.props.Props;
import net.vpc.common.app.swing.core.PValueViewProperty;
import net.vpc.scholar.hadruwaves.Material;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;
import net.vpc.common.app.swing.core.DefaultPropertiesNodeFolder;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.util.DefaultHWPropertiesTree;

/**
 *
 * @author vpc
 */
public class MaterialPropertiesAware extends DefaultHWPropertiesTree {

    private final Material wp;
    private final HWProjectItem outer;
    private DefaultPropertiesNodeFolder root;

    public MaterialPropertiesAware(Material wp, final HWProjectItem outer) {
        super(outer);
        this.outer = outer;
        this.wp = wp;

        refresh();
    }

    public void refresh() {
        root = new DefaultPropertiesNodeFolder("folder", "Material", wp);
        setRoot(root);
        DefaultPropertiesNodeFolder folder = root.addFolder("folder", "General");
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), Props.of("name").valueOf(String.class, wp.getName()).readOnly()));
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), Props.of("description").valueOf(String.class, "").readOnly()));

        folder = root.addFolder("folder", "Values");
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), Props.of("permettivity").valueOf(Double.class, wp.getPermettivity()).readOnly()));
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), Props.of("permeability").valueOf(Double.class, wp.getPermeability()).readOnly()));
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), Props.of("electricConductivity").valueOf(Double.class, wp.getElectricConductivity()).readOnly()));
    }

}
