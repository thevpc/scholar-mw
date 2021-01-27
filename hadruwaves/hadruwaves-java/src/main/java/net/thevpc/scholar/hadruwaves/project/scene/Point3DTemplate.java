package net.thevpc.scholar.hadruwaves.project.scene;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.tson.TsonSerializable;
import net.thevpc.scholar.hadrumaths.geom.Point;
import net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.Point3D;
import net.thevpc.scholar.hadruwaves.project.Props2;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwaves.props.WritablePExpression;

public class Point3DTemplate implements TsonSerializable {

    private WritablePExpression<Double> x = Props2.of("x").exprLenOf(0.0);
    private WritablePExpression<Double> y = Props2.of("y").exprLenOf(0.0);
    private WritablePExpression<Double> z = Props2.of("z").exprLenOf(0.0);

    public Point3DTemplate() {
    }

    public Point3DTemplate(double x, double y, double z) {
        this(String.valueOf(x), String.valueOf(y), String.valueOf(z));
    }

    public Point3DTemplate(String x, String y, String z) {
        this.x.set(x);
        this.y.set(y);
        this.z.set(z);
    }

    public Point3DTemplate(Point3DTemplate o) {
        set(o);
    }

    public Point3DTemplate(Point o) {
        set(o);
    }

    public Point3DTemplate(Point3D o) {
        set(o);
    }

    public void set(Point3DTemplate o) {
        this.x.set(o.x.get());
        this.y.set(o.y.get());
        this.z.set(o.z.get());
    }

    public void set(Point3D o) {
        this.x.set(String.valueOf(o.getX()));
        this.y.set(String.valueOf(o.getY()));
        this.z.set(String.valueOf(o.getZ()));
    }

    public void set(Point o) {
        this.x.set(String.valueOf(o.getX()));
        this.y.set(String.valueOf(o.getY()));
        this.z.set(String.valueOf(o.getZ()));
    }

    public WritablePExpression<Double> x() {
        return x;
    }

    public WritablePExpression<Double> y() {
        return y;
    }

    public WritablePExpression<Double> z() {
        return z;
    }

    public Point3D eval(HWConfigurationRun configuration) {
        return new Point3D(
                x.eval(configuration),
                y.eval(configuration),
                z.eval(configuration)
        );
    }

    public Point evalPoint(HWConfigurationRun configuration) {
        return new Point(
                x.eval(configuration),
                y.eval(configuration),
                z.eval(configuration)
        );
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ')';
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.obj()
                .add("x", x.get())
                .add("y", y.get())
                .add("z", z.get())
                .build();
    }

}
