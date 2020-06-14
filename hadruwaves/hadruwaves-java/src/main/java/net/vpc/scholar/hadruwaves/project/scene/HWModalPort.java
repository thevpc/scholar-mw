package net.vpc.scholar.hadruwaves.project.scene;

import net.vpc.common.props.Props;
import net.vpc.common.props.WritablePValue;
import net.vpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.vpc.Element3DRenderPrefs;
import net.vpc.scholar.hadruwaves.project.AbstractHWProjectComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import net.vpc.scholar.hadrumaths.plot.d3.BoundDomain;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;

public class HWModalPort extends AbstractHWProjectComponent implements HWProjectPort {

    private WritablePValue<Element3DTemplate> geometry = Props.of("geometry").valueOf(Element3DTemplate.class, null);

    public HWModalPort(Element3DTemplate geometry, String name) {
        this.geometry.set(geometry);
        this.name().set(name);
    }

    @Override
    public List<Element3D> toElements3DImpl(HWConfigurationRun configuration) {
        List<Element3D> e = new ArrayList<>();
        Element3D geo = geometry().get().toElements3D(configuration).copy();
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

    protected void preparePrefs(Element3DRenderPrefs p) {
        int alpha = 255;
        p.setFillColor(new Color(255, 255, 0, alpha));
        p.setBackColor(new Color(255, 255, 0, alpha).YELLOW.darker());
    }
}
