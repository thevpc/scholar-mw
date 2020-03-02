package net.vpc.scholar.hadruwaves.project.scene;

import net.vpc.common.prpbind.Props;
import net.vpc.common.prpbind.WritablePValue;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3D;
import net.vpc.scholar.hadruplot.backends.calc3d.vpc.Element3DRenderPrefs;
import net.vpc.scholar.hadruwaves.project.HWProjectEnv;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModalPortNode extends AbstractStructureNode implements StructureNodePort{
    private WritablePValue<Element3DTemplate> geometry = Props.of("geometry").valueOf(Element3DTemplate.class, null);
    private WritablePValue<String> name = Props.of("name").valueOf(String.class, null);

    public ModalPortNode(Element3DTemplate geometry, String name) {
        this.geometry.set(geometry);
        this.name.set(name);
    }


    public WritablePValue<String> name() {
        return name;
    }

    @Override
    public List<Element3D> toElements3D(HWProjectEnv env) {
        List<Element3D> e = new ArrayList<>();
        Element3D geo = geometry().get().toElements3D(env).copy();
        preparePrefs(geo.prefs());
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

    public WritablePValue<Element3DTemplate> geometry() {
        return geometry;
    }

    protected void preparePrefs(Element3DRenderPrefs p) {
        int alpha = 255;
        p.setFillColor(new Color(255, 255, 0, alpha));
        p.setBackColor(new Color(255, 255, 0, alpha).YELLOW.darker());
    }
}
