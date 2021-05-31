/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.trees;

import java.io.File;
import net.thevpc.echo.Application;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;
import net.thevpc.echo.impl.DefaultPropertiesNodeFolder;
import net.thevpc.echo.impl.PValueViewPropertyReadOnly;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.components.HWProjectFolder;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.DefaultHWPropertiesTree;

/**
 *
 * @author vpc
 */
public class HWProjectFolderPropertiesAware extends DefaultHWPropertiesTree {

    private final HWProjectFolder wp;
    private final HWProjectItem projectItem;
    private DefaultPropertiesNodeFolder root;

    public HWProjectFolderPropertiesAware(HWProjectFolder wp, final HWProjectItem outer) {
        super(outer);
        this.projectItem = outer;
        this.wp = wp;

        refresh();
    }

    public void refresh() {
        root = new DefaultPropertiesNodeFolder("folder", "Project Folder", wp);
        setRoot(root);
        DefaultPropertiesNodeFolder folder = root.addFolder("folder", "General");
        Application app = projectItem.getStudio().app();
        folder.add(new PValueViewPropertyReadOnly("property", app, "name", new File(wp.path).getName()));
        String description = "";
        switch (wp.path) {
            case "/Volumes": {
                description = "Volumes Elements (Boxes,...)";
                break;
            }
            case "/Surfaces": {
                description = "Surfaces Elements (Polygons,...)";
                break;
            }
            case "/Curves": {
                description = "Curve Elements (Lines, Poylines,,...)";
                break;
            }
            case "/Sources": {
                description = "Sources and Ports (Planar, Modal,...)";
                break;
            }
        }
        folder.add(new PValueViewPropertyReadOnly("property", app, "description", description));
    }

}
