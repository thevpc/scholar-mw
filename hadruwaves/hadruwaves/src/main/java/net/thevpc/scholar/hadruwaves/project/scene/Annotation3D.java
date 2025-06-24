package net.thevpc.scholar.hadruwaves.project.scene;

import net.thevpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;

import java.util.Arrays;
import java.util.List;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.tson.TsonSerializable;

public class Annotation3D implements TsonSerializable{
    private Element3D element;

    public Annotation3D(Element3D element) {
        this.element = element;
    }

    public List<Element3D> toElements3D() {
        return Arrays.asList(element);
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.ofObjectBuilder(getClass().getSimpleName())
                //TODO add elements here! 
                .build();
    }
    
}
