/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.trees;

import net.thevpc.echo.impl.PValueViewProperty;
import net.thevpc.scholar.hadruwaves.project.HWSolution;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;
import net.thevpc.echo.impl.DefaultPropertiesNodeFolder;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.DefaultHWPropertiesTree;

/**
 *
 * @author vpc
 */
public class HWSolutionPropertiesAware extends DefaultHWPropertiesTree {

    private final HWSolution wp;
    private final HWProjectItem outer;
    private DefaultPropertiesNodeFolder root;

    public HWSolutionPropertiesAware(HWSolution wp, final HWProjectItem outer) {
        super(outer);
        this.outer = outer;
        this.wp = wp;

        refresh();
    }

    public void refresh() {
        root = new DefaultPropertiesNodeFolder("folder", "Solution", wp);
        setRoot(root);
        DefaultPropertiesNodeFolder folder = root.addFolder("folder", "General");
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.name()));
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.description()));

        folder = root.addFolder("folder", "Paths");
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.filePath()));
    }

}
