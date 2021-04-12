/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.components;

import java.awt.Component;
//import java.io.File;
import javax.swing.JTree;
import net.thevpc.common.iconset.IconSets;

import net.thevpc.echo.AppIconSet;
//import net.thevpc.common.iconset.PIconSet;
import net.thevpc.echo.swing.core.swing.LazyTree;
import net.thevpc.scholar.hadruwaves.project.*;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationFolder;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwaves.project.scene.Element3DTemplate;
import net.thevpc.scholar.hadruwaves.project.scene.HWMaterialTemplate;
import net.thevpc.scholar.hadruwaves.project.scene.AbstractHWProjectComponentMaterial;
import net.thevpc.scholar.hadruwaves.project.scene.elem.Element3DPolygonTemplate;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.DefaultAppTreeCellRenderer;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;
import net.thevpc.scholar.hadruwaves.project.HWProjectComponent;
import net.thevpc.scholar.hadruwaves.project.scene.HWProjectPort;

/**
 *
 * @author vpc
 */
public class SolutionAppTreeCellRendererImpl extends DefaultAppTreeCellRenderer {

    private final HWSSolutionExplorerTool outer;

    public SolutionAppTreeCellRendererImpl(HadruwavesStudio studio, final HWSSolutionExplorerTool outer) {
        super(studio);
        this.outer = outer;
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component u = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        HWProjectItem item = (HWProjectItem) LazyTree.resolveLazyTreeNodeValue(value);
        Object o = item.getItemValue();
        IconSets iconSet = studio().app().iconSets();
        if (o instanceof HWSolution) {
            super.setIcon(iconSet.icon("Solution").get());
            setText(((HWSolution) o).name().get());
        } else if (o instanceof HWMaterialTemplate) {
            setText(((HWMaterialTemplate) o).name().get());
            super.setIcon(iconSet.icon("Material").get());
        } else if (o instanceof AbstractHWProjectComponentMaterial) {
            Element3DTemplate e = ((AbstractHWProjectComponentMaterial) o).geometry().get();
            if (e instanceof Element3DPolygonTemplate) {
                super.setIcon(iconSet.icon("Elem2d").get());
            } else {
                super.setIcon(iconSet.icon("Elem3d").get());
            }
        } else if (o instanceof HWProject) {
            if (studio().proc().activeProjectValue() == o) {
                super.setForeground(getActivatedColor(sel));
                super.setIcon(iconSet.icon("SelectedProject").get());
            } else {
                super.setIcon(iconSet.icon("Project").get());
            }
            HWSolutionElement se = (HWSolutionElement) o;
            setText(se.name().get());
        } else if (o instanceof DefaultHWSolutionFolder) {
            HWSolutionElement se = (HWSolutionElement) o;
            setText(se.name().get());
            super.setIcon(expanded ? iconSet.icon("OpenFolder").get() : iconSet.icon("Folder").get());
        } else if (o instanceof HWProjectPort) {
            super.setIcon(iconSet.icon("Port").get());
            setText(((HWProjectComponent) o).name().get());
        } else if (o instanceof HWConfigurationRun) {
            HWConfigurationRun t = item.getProject().configurations().activeConfiguration().get();
            if (o == t) {
                super.setIcon(iconSet.icon("ActiveConfig").get());
            } else {
                super.setIcon(iconSet.icon("Config").get());
            }
        } else if (o instanceof HWConfigurationFolder) {
            super.setIcon(expanded ? iconSet.icon("OpenFolder").get() : iconSet.icon("Folder").get());
        } else if (o instanceof HWProjectSource) {
            HWProjectSource s=(HWProjectSource) o;
            super.setIcon(outer.studio().app().iconSets().iconForFile(s.file,selected, expanded).get());
        } else if (!leaf) {
            super.setIcon(expanded ? iconSet.icon("OpenFolder").get() : iconSet.icon("Folder").get());
        }
        if (o instanceof HWProjectComponent) {
            if (!((HWProjectComponent) o).visible().get()
                    // || non enabled (TODO eval config)
                    ) {
                super.setForeground(getDisabledColor(sel));
            }
        }
        return u;
    }

}
