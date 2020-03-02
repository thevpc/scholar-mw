package net.vpc.scholar.hadruwaves.project.scene;

import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3D;

import java.util.Arrays;
import java.util.List;

public class Annotation3D {
    private Element3D element;

    public Annotation3D(Element3D element) {
        this.element = element;
    }

    public List<Element3D> toElements3D() {
        return Arrays.asList(element);
    }
}
