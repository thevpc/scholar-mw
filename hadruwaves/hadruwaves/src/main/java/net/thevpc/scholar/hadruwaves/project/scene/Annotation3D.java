package net.thevpc.scholar.hadruwaves.project.scene;

import net.thevpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;

import java.util.Arrays;
import java.util.List;

import net.thevpc.nuts.elem.NElement;

import net.thevpc.nuts.elem.NToElement;

public class Annotation3D implements NToElement{
    private Element3D element;

    public Annotation3D(Element3D element) {
        this.element = element;
    }

    public List<Element3D> toElements3D() {
        return Arrays.asList(element);
    }

    @Override
    public NElement toElement() {
        return NElement.ofObjectBuilder(getClass().getSimpleName())
                //TODO add elements here! 
                .build();
    }
    
}
