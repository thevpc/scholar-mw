package net.thevpc.scholar.hadruwaves.project.scene.elem;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.geom.Dimension;
import net.thevpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.Point3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.element3d.Element3DParallelipiped;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwaves.project.scene.Point3DTemplate;

public class Element3DParallelipipedTemplate extends AbstractElement3DTemplate {

    private Point3DTemplate a = new Point3DTemplate();
    private Point3DTemplate b = new Point3DTemplate();
    private Point3DTemplate c = new Point3DTemplate();
    private Point3DTemplate d = new Point3DTemplate();

    public Element3DParallelipipedTemplate(Point3D a, Point3D b, Point3D c, Point3D d) {
        this.a.set(a);
        this.b.set(b);
        this.c.set(c);
        this.d.set(d);
    }

    public Element3DParallelipipedTemplate(String xmin, String xmax,
            String ymin, String ymax,
            String zmin, String zmax) {
        this(
                new Point3DTemplate(xmin, ymin, zmin),
                new Point3DTemplate(xmax, ymin, zmin),
                new Point3DTemplate(xmin, ymax, zmin),
                new Point3DTemplate(xmin, ymin, zmax)
        );
    }
    public Element3DParallelipipedTemplate(Point3DTemplate a, Point3DTemplate b, Point3DTemplate c, Point3DTemplate d) {
        this.a.set(a);
        this.b.set(b);
        this.c.set(c);
        this.d.set(d);
    }

    public Point3DTemplate[] getPoints() {
        return new Point3DTemplate[]{
            a, b, c, d
        };
    }

    public Dimension evalDimension(HWConfigurationRun configuration) {
        Point3D pa = a.eval(configuration);
        Point3D pb = b.eval(configuration);
        Point3D pc = c.eval(configuration);
        Point3D pd = d.eval(configuration);
        return Dimension.create(
                new Vector3D(pa, pb).getLength(),
                new Vector3D(pa, pc).getLength(),
                new Vector3D(pa, pd).getLength()
        );
    }

    @Override
    public Element3D toElements3D(HWConfigurationRun configuration) {
        Element3DParallelipiped e = new Element3DParallelipiped(
                a.eval(configuration),
                b.eval(configuration),
                c.eval(configuration),
                d.eval(configuration),
                false
        );
        return e;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.ofObjectBuilder(getClass().getSimpleName())
                .add("a",a.toTsonElement(context))
                .add("b",b.toTsonElement(context))
                .add("c",c.toTsonElement(context))
                .add("d",d.toTsonElement(context))
                .build()
                ;
    }
    
}
