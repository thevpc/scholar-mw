package net.vpc.scholar.hadruwaves.project.scene;

import net.vpc.common.props.Props;
import net.vpc.common.props.WritablePValue;
import net.vpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.vpc.Element3DRenderPrefs;
import net.vpc.scholar.hadruwaves.Material;
import net.vpc.scholar.hadruwaves.project.AbstractHWProjectComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectBuilder;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.plot.d3.BoundDomain;
import net.vpc.scholar.hadruwaves.Boundary;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;

public abstract class AbstractHWProjectComponentMaterial extends AbstractHWProjectComponent implements HWProjectPiece {

    private WritablePValue<HWMaterialTemplate> material = Props.of("material").valueOf(HWMaterialTemplate.class, null);
    private WritablePValue<Element3DTemplate> geometry = Props.of("geometry").valueOf(Element3DTemplate.class, null);

    public AbstractHWProjectComponentMaterial(String name, HWMaterialTemplate material, Element3DTemplate geometry) {
        this.geometry.set(geometry);
        this.material.set(material);
        this.name().set(name);
    }

    @Override
    public WritablePValue<HWMaterialTemplate> material() {
        return material;
    }

    public WritablePValue<Element3DTemplate> geometry() {
        return geometry;
    }

    @Override
    public void updateBoundDomain(HWConfigurationRun configuration, BoundDomain domain) {
        domain.include(geometry().get().toElements3D(configuration));
        for (Annotation3D annotation : annotations()) {
            for (Element3D element3D : annotation.toElements3D()) {
                if (element3D != null) {
                    domain.include(element3D);
                }
            }
        }
    }

    @Override
    public List<Element3D> toElements3DImpl(HWConfigurationRun configuration) {
        List<Element3D> e = new ArrayList<>();
        Element3D geo = geometry().get().toElements3D(configuration).copy();
        Material m = material().get().eval(configuration);
        preparePrefs(m, geo.prefs(), selected().get());
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

    protected void preparePrefs(Material m, Element3DRenderPrefs p, boolean selected) {
        if (m.equals(Material.PEC)) {
            int alpha = 200;
            p.setFillColor(new Color(selected ? 255 : 150, 0, 0, alpha));
            p.setBackColor(new Color(selected ? 178 : 100, 0, 0));
        } else if (m.equals(Material.VACUUM)) {
            int alpha = 200;
            p.setFillColor(new Color(255, 255, selected ? 255 : 0, alpha));
            p.setBackColor(new Color(selected ? 128 : 70, selected ? 128 : 70, selected ? 128 : 70, alpha));
            //
        } else if (m.isSubstrate()) {
            int alpha = 255;
            p.setFillColor(new Color(selected ? 255 : 150, 100, 0, alpha));
            p.setBackColor(new Color(selected ? 150 : 100, 200, 0, alpha));
        }
    }

    protected void preparePrefs(Boundary m, Element3DRenderPrefs p, boolean selected) {
        if (m != null) {
            switch (m) {
                case ELECTRIC: {
                    int alpha = 200;
                    p.setFillColor(new Color(selected ? 255 : 150, 0, 0, alpha));
                    p.setBackColor(new Color(selected ? 178 : 100, 0, 0));
                    return;
                }
                case MAGNETIC: {
                    int alpha = 200;
                    p.setFillColor(new Color(0, 0, selected ? 255 : 150, alpha));
                    p.setBackColor(new Color(0, 0, selected ? 178 : 100));
                    return;
                }
                case PERIODIC: {
                    int alpha = 200;
                    p.setFillColor(new Color(0, selected ? 255 : 150, 0, alpha));
                    p.setBackColor(new Color(0, selected ? 178 : 100, 0, alpha));
                    return;
                }
                case INFINITE: {
                    int alpha = 200;
                    p.setFillColor(new Color(0, selected ? 255 : 150, 0, alpha));
                    p.setBackColor(new Color(0, selected ? 178 : 100, 0, alpha));
                    return;
                }
                default:
                    break;
            }
        }
        int alpha = 200;
        p.setFillColor(new Color(255, 255, 255, alpha));
        p.setBackColor(new Color(128, 128, 128, alpha));
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder obj = (TsonObjectBuilder) super.toTsonElement(context).builder();
        if (material.get() != null) {
            obj.add("material", material.get().toTsonElement(context));
        }
        if (geometry.get() != null) {
            obj.add("geometry", geometry.get().toTsonElement(context));
        }
        return obj.build();
    }

}
