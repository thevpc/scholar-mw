/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.props.trees;

import net.vpc.common.app.Application;
import net.vpc.common.app.swing.core.PValueViewProperty;
import net.vpc.common.app.swing.core.PValueViewPropertyReadOnly;
import net.vpc.scholar.hadrumaths.geom.Dimension;
import net.vpc.scholar.hadruwaves.Boundary;
import net.vpc.scholar.hadruwaves.project.scene.Element3DTemplate;
import net.vpc.scholar.hadruwaves.project.scene.HWMaterialTemplate;
import net.vpc.scholar.hadruwaves.project.scene.HWProjectBrick;
import net.vpc.scholar.hadruwaves.project.scene.Point3DTemplate;
import net.vpc.scholar.hadruwaves.project.scene.elem.Element3DParallelipipedTemplate;
import net.vpc.scholar.hadruwaves.project.scene.elem.Element3DPolygonTemplate;
import net.vpc.scholar.hadruwaves.util.ProjectFormatter;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.util.PValueViewPropertyE;
import net.vpc.common.app.swing.core.DefaultPropertiesNodeFolder;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.util.DefaultHWPropertiesTree;
import net.vpc.scholar.hadruwaves.project.scene.HWProjectPiece;

/**
 *
 * @author vpc
 */
public class HWProjectElementBrickPropertiesAware extends DefaultHWPropertiesTree {

    private final HWProjectPiece wp;
    private final HWProjectItem outer;
    private DefaultPropertiesNodeFolder root;

    public HWProjectElementBrickPropertiesAware(HWProjectPiece wp, final HWProjectItem outer) {
        super(outer);
        this.outer = outer;
        this.wp = wp;
        refresh();
    }

    @Override
    public void refresh() {
        root = new DefaultPropertiesNodeFolder("folder", "Box", wp);
        setRoot(root);
        Application app = outer.getStudio().app();
        DefaultPropertiesNodeFolder folder = root.addFolder("folder", "General");
        folder.add(new PValueViewProperty("property", app, wp.name()));
        folder.add(new PValueViewProperty("property", app, wp.visible()));
        folder.add(new PValueViewPropertyE("property", wp.enabled()));
        folder.add(new PValueViewProperty("property", app, wp.description()));
        folder = root.addFolder("folder", "Material");
        folder.add(new PValueViewProperty("property", app, wp.material(), availableMaterials(false)));

        folder = root.addFolder("folder", "Geometry");
        Element3DTemplate g = wp.geometry().get();
        if (g instanceof Element3DParallelipipedTemplate) {
            buildElement3DParallelipipedTemplate(folder, app, g, outer, (HWProjectBrick) wp);
        } else if (g instanceof Element3DPolygonTemplate) {
            buildElement3DPolygonTemplate(folder, app, g, outer);
        } else {
            folder.add(new PValueViewPropertyReadOnly("property", app, "type", "Unknown", null));
        }
    }

    public void buildElement3DPolygonTemplate(DefaultPropertiesNodeFolder folder, Application app, Element3DTemplate g, final HWProjectItem outer1) {
        folder.add(new PValueViewPropertyReadOnly("property", app, "category", "Surface", null));
        folder.add(new PValueViewPropertyReadOnly("property", app, "type", "Polygon", null));
        Element3DPolygonTemplate p = (Element3DPolygonTemplate) g;
        HWConfigurationRun configuration = outer1.getStudio().proc().selectedConfiguration().get();
        int pindex = 1;
        DefaultPropertiesNodeFolder points = folder.addFolder("folder", "Points");
        for (Point3DTemplate point : p.getPoints()) {
            points.add(new PValueViewPropertyReadOnly("property", app, "point " + pindex, configuration.formatPoint(point, ProjectFormatter.Mode.SHORT_NOSUFFIX), null));
            pindex++;
        }
    }

    public void buildElement3DParallelipipedTemplate(DefaultPropertiesNodeFolder folder, Application app, Element3DTemplate g, final HWProjectItem outer1, HWProjectBrick wp1) {
        folder.add(new PValueViewPropertyReadOnly("property", app, "type", "Box", null));
        Element3DParallelipipedTemplate p = (Element3DParallelipipedTemplate) g;
        HWConfigurationRun configuration = outer1.getStudio().proc().selectedConfiguration().get();
        Dimension dim = null;
        try {
            dim = p.evalDimension(configuration);
        } catch (Exception ex) {
            //
        }
        if (dim != null) {
            folder.add(new PValueViewPropertyReadOnly("property", app, "dimension", ProjectFormatter.formatDimension(outer1.getProject(), dim, ProjectFormatter.Mode.SHORT_NOSUFFIX), null));
        }
        DefaultPropertiesNodeFolder points = folder.addFolder("folder", "Points");
        int pindex = 1;
        for (Point3DTemplate point : p.getPoints()) {
            points.add(new PValueViewPropertyReadOnly("property", app, "point " + pindex, configuration.formatPoint(point, ProjectFormatter.Mode.SHORT_NOSUFFIX), null));
            pindex++;
        }
        DefaultPropertiesNodeFolder faces = folder.addFolder("folder", "Faces");
        for (HWProjectBrick.Face factIndex : HWProjectBrick.Face.values()) {
            DefaultPropertiesNodeFolder faceFolder = faces.addFolder("folder", factIndex.name());
            faceFolder.add(new PValueViewProperty("property", app, wp1.face(factIndex).visible()));
            faceFolder.add(new PValueViewPropertyE("property", wp1.face(factIndex).boundary()));
        }
    }

    private HWMaterialTemplate[] availableMaterials(boolean nullable) {
        HWMaterialTemplate[] a = outer.getProject().materials().values().stream().toArray(HWMaterialTemplate[]::new);
        if (!nullable) {
            return a;
        }
        HWMaterialTemplate[] b = new HWMaterialTemplate[a.length + 1];
        System.arraycopy(a, 0, b, 1, a.length);
        return b;
    }

    private Boundary[] availableBoundaries(boolean nullable) {
        Boundary[] a = Boundary.values();
        if (!nullable) {
            return a;
        }
        Boundary[] b = new Boundary[a.length + 1];
        System.arraycopy(a, 0, b, 1, a.length);
        return b;
    }

}
