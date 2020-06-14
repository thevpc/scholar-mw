/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.util;

import net.vpc.common.app.AppPropertiesNodeFolder;
import net.vpc.common.app.swing.core.DefaultPropertiesTree;
import net.vpc.scholar.hadruwaves.project.HWSolution;
import net.vpc.scholar.hadruwaves.project.HWProject;

/**
 *
 * @author vpc
 */
public class DefaultHWPropertiesTree extends DefaultPropertiesTree implements HWPropertiesTree {

    private HWProjectItem item;

    public DefaultHWPropertiesTree(AppPropertiesNodeFolder root, HWProjectItem item) {
        super(root);
        this.item = item;
    }

    public DefaultHWPropertiesTree(HWProjectItem item) {
        this.item = item;
    }

    @Override
    public HWSolution getSolution() {
        return item == null ? null : item.getSolution();
    }

    @Override
    public HWProject getProject() {
        return item == null ? null : item.getProject();
    }

   
}
