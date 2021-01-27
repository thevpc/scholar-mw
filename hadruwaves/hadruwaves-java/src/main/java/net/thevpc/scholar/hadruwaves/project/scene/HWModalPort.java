package net.thevpc.scholar.hadruwaves.project.scene;

import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritablePValue;
import net.thevpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.Element3DRenderPrefs;
import net.thevpc.scholar.hadruwaves.project.AbstractHWProjectComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import net.thevpc.scholar.hadrumaths.plot.d3.BoundDomain;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;

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
