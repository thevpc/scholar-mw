package net.vpc.scholar.hadruwaves.project.scene;

import net.vpc.common.prpbind.Props;
import net.vpc.common.prpbind.WritablePValue;
import net.vpc.scholar.hadruplot.backends.calc3d.vpc.Point3D;
import net.vpc.scholar.hadruwaves.project.HWProjectEnv;
import net.vpc.scholar.hadruwaves.project.Props2;
import net.vpc.scholar.hadruwaves.props.WritablePExpression;

public class Point3DTemplate {
    private WritablePExpression<Double> x = Props2.of("x").doubleOf(0.0);
    private WritablePExpression<Double> y = Props2.of("y").doubleOf(0.0);
    private WritablePExpression<Double> z = Props2.of("z").doubleOf(0.0);

    public Point3DTemplate() {
    }

    public Point3DTemplate(double x, double y, double z) {
        this(String.valueOf(x),String.valueOf(y),String.valueOf(z));
    }

    public Point3DTemplate(String x, String y, String z) {
        this.x.set(x);
        this.y.set(y);
        this.z.set(z);
    }


    public Point3DTemplate(Point3DTemplate o) {
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

    public WritablePExpression<Double> x() {
        return x;
    }

    public WritablePExpression<Double> y() {
        return y;
    }

    public WritablePExpression<Double> z() {
        return z;
    }

    public Point3D eval(HWProjectEnv env) {
        return new Point3D(
                x.eval(env),
                y.eval(env),
                z.eval(env)
        );
    }
}
