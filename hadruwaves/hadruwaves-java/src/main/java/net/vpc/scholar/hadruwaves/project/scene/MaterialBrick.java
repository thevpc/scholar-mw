package net.vpc.scholar.hadruwaves.project.scene;

import net.vpc.common.prpbind.Props;
import net.vpc.common.prpbind.WritablePValue;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3D;
import net.vpc.scholar.hadruplot.backends.calc3d.vpc.Element3DRenderPrefs;
import net.vpc.scholar.hadruplot.backends.calc3d.vpc.element3d.Element3DParallelipiped;
import net.vpc.scholar.hadruwaves.Material;
import net.vpc.scholar.hadruwaves.project.HWProjectEnv;
import net.vpc.scholar.hadruwaves.project.scene.elem.Element3DParallelipipedTemplate;

import java.util.ArrayList;
import java.util.List;

public class MaterialBrick extends MaterialNode {
    private WritablePValue<Material>[] faces;
    private WritablePValue<Boolean>[] facesVisible;

    public MaterialBrick(HWMaterialTemplate material, Element3DParallelipipedTemplate geometry,String name) {
        super(material, geometry,name);
        this.faces = new WritablePValue[6];
        this.facesVisible = new WritablePValue[6];
        for (int i = 0; i < this.faces.length; i++) {
            this.faces[i] = Props.of("face" + (i + 1)).valueOf(Material.class, null);
            this.facesVisible[i] = Props.of("faceVisible" + (i + 1)).valueOf(Boolean.class, true);
        }
    }

    public void setFaceMaterial(int face, Material m) {
        this.faces[face].set(m);
    }

    public void setFacesVisible(int face, boolean m) {
        this.facesVisible[face].set(m);
    }

//    public Material getFaceMaterial(int face) {
//        Material m = faces[face].get();
//        if (m == null) {
//            return material().get();
//        }
//        return m;
//    }

    public WritablePValue<Material> faceMaterial(int face) {
        return faces[face];
    }

    @Override
    public List<Element3D> toElements3D(HWProjectEnv env) {
        List<Element3D> e = new ArrayList<>();
        Element3DParallelipiped geo = (Element3DParallelipiped) geometry().get().toElements3D(env).copy();
        Material m = material().get().eval(env);
        preparePrefs(m, geo.prefs());
        for (int i = 0; i < 6; i++) {
            Material fm = faces[i].get();
            if (fm != null) {
                Element3DRenderPrefs p = geo.prefs().copy();
                preparePrefs(fm, p);
                if (!facesVisible[i].get()) {
                    p.setVisible(false);
                }
                geo.setFacePrefs(i, p);
            }
        }
        e.add(geo);
        for (Annotation3D annotation : annotations()) {
            for (Element3D element3D : annotation.toElements3D()) {
                if (element3D != null) {
                    e.add(element3D);
                }
            }
        }
        return e;
    }
}
