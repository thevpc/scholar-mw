package net.vpc.scholar.hadruwaves.project.scene;

import net.vpc.common.prpbind.PropertyEvent;
import net.vpc.common.prpbind.PropertyListener;
import net.vpc.common.prpbind.Props;
import net.vpc.common.prpbind.WritablePValue;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3D;
import net.vpc.scholar.hadruplot.backends.calc3d.vpc.Element3DRenderPrefs;
import net.vpc.scholar.hadruwaves.Material;
import net.vpc.scholar.hadruwaves.project.HWProjectEnv;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialNode extends AbstractStructureNode {
    private WritablePValue<HWMaterialTemplate> material = Props.of("material").valueOf(HWMaterialTemplate.class, null);
    private WritablePValue<Element3DTemplate> geometry = Props.of("geometry").valueOf(Element3DTemplate.class, null);
    private WritablePValue<String> name = Props.of("name").valueOf(String.class, null);

    public MaterialNode(HWMaterialTemplate material, Element3DTemplate geometry,String name) {
        this.geometry.set(geometry);
        this.material.set(material);
        this.name.set(name);
    }


    public WritablePValue<String> name() {
        return name;
    }

    public WritablePValue<HWMaterialTemplate> material() {
        return material;
    }

    public WritablePValue<Element3DTemplate> geometry() {
        return geometry;
    }

    @Override
    public List<Element3D> toElements3D(HWProjectEnv env) {
        List<Element3D> e = new ArrayList<>();
        Element3D geo = geometry().get().toElements3D(env).copy();
        Material m = material().get().eval(env);
        preparePrefs(m, geo.prefs());
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

    protected void preparePrefs(Material m, Element3DRenderPrefs p) {
        if (m.equals(Material.PEC)) {
            int alpha = 200;
            p.setFillColor(new Color(255,0,0,alpha));
            p.setBackColor(new Color(255,0,0,alpha).darker());
        } else if (m.equals(Material.PMC)) {
            int alpha = 200;
            p.setFillColor(new Color(0,0,255,alpha));
            p.setBackColor(new Color(0,0,255,alpha).darker());
        } else if (m.equals(Material.PERIODIC_FACE)) {
            int alpha = 200;
            p.setFillColor(new Color(0,255,0,alpha));
            p.setBackColor(new Color(0,255,0,alpha).darker());
        } else if (m.equals(Material.INFINITE_FACE)) {
            int alpha = 200;
            p.setFillColor(new Color(0,255,0,alpha));
            p.setBackColor(new Color(0,255,0,alpha).darker());
        } else if (m.equals(Material.VACUUM)) {
            int alpha = 200;
            p.setFillColor(new Color(255, 255, 255, alpha));
            p.setBackColor(new Color(128, 128, 128, alpha));
            //
        } else if (m.isSubstrate()) {
            int alpha = 255;
            p.setFillColor(new Color(255,255,0,alpha));
            p.setBackColor(new Color(255,255,0,alpha).YELLOW.darker());
        }
    }
}
