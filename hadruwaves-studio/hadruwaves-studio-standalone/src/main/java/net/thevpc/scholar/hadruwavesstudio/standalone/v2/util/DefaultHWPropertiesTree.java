/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.util;

import net.thevpc.echo.api.AppPropertiesNodeFolder;
import net.thevpc.echo.impl.DefaultPropertiesTree;
import net.thevpc.scholar.hadruwaves.project.HWSolution;
import net.thevpc.scholar.hadruwaves.project.HWProject;

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
