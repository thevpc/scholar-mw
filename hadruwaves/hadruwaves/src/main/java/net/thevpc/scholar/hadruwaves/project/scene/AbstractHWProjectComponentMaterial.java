package net.thevpc.scholar.hadruwaves.project.scene;

import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritableValue;
import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.Element3DRenderPrefs;
import net.thevpc.scholar.hadruwaves.Material;
import net.thevpc.scholar.hadruwaves.project.AbstractHWProjectComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import net.thevpc.nuts.elem.NElement;


import net.thevpc.scholar.hadrumaths.plot.d3.BoundDomain;
import net.thevpc.scholar.hadruwaves.Boundary;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;

public abstract class AbstractHWProjectComponentMaterial extends AbstractHWProjectComponent implements HWProjectPiece {

    private WritableValue<HWMaterialTemplate> material = Props.of("material").valueOf(HWMaterialTemplate.class, null);
    private WritableValue<Element3DTemplate> geometry = Props.of("geometry").valueOf(Element3DTemplate.class, null);

    public AbstractHWProjectComponentMaterial(String name, HWMaterialTemplate material, Element3DTemplate geometry) {
        this.geometry.set(geometry);
        this.material.set(material);
        this.name().set(name);
    }

    @Override
    public WritableValue<HWMaterialTemplate> material() {
        return material;
    }

    public WritableValue<Element3DTemplate> geometry() {
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
    public NElement toElement() {
        NObjectElementBuilder obj = (NObjectElementBuilder) super.toElement().builder();
        if (material.get() != null) {
            obj.add("material", material.get().toElement());
        }
        if (geometry.get() != null) {
            obj.add("geometry", geometry.get().toElement());
        }
        return obj.build();
    }

}
