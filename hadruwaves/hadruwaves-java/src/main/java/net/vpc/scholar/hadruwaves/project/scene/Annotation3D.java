package net.vpc.scholar.hadruwaves.project.scene;

import net.vpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;

import java.util.Arrays;
import java.util.List;
import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.common.tson.TsonSerializable;

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
        return Tson.obj(getClass().getSimpleName())
                //TODO add elements here! 
                .build();
    }
    
}
