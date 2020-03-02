package net.vpc.scholar.hadruwaves.project.scene.elem;

import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3D;
import net.vpc.scholar.hadruplot.backends.calc3d.vpc.Point3D;
import net.vpc.scholar.hadruplot.backends.calc3d.vpc.element3d.Element3DParallelipiped;
import net.vpc.scholar.hadruwaves.project.HWProjectEnv;
import net.vpc.scholar.hadruwaves.project.scene.Point3DTemplate;

public class Element3DParallelipipedTemplate extends AbstractElement3DTemplate{
    private Point3DTemplate a= new Point3DTemplate();
    private Point3DTemplate b= new Point3DTemplate();
    private Point3DTemplate c= new Point3DTemplate();
    private Point3DTemplate d= new Point3DTemplate();

    public Element3DParallelipipedTemplate(Point3D a, Point3D b, Point3D c, Point3D d) {
        this.a.set(a);
        this.b.set(b);
        this.c.set(c);
        this.d.set(d);
    }

    public Element3DParallelipipedTemplate(Point3DTemplate a, Point3DTemplate b, Point3DTemplate c, Point3DTemplate d) {
        this.a.set(a);
        this.b.set(b);
        this.c.set(c);
        this.d.set(d);
    }

    @Override
    public Element3D toElements3D(HWProjectEnv env) {
        Element3DParallelipiped e = new Element3DParallelipiped(
                a.eval(env),
                b.eval(env),
                c.eval(env),
                d.eval(env),
                false
        );
        return e;
    }
}
