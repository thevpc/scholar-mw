package net.vpc.scholar.hadruwaves.project.scene;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadruwaves.project.HWProjectEnv;
import net.vpc.scholar.hadruwaves.project.Props2;
import net.vpc.scholar.hadruwaves.props.WritablePExpression;

public class DomainTemplate {
    private WritablePExpression<Double> xmin = Props2.of("xmin").doubleOf(Double.NEGATIVE_INFINITY);
    private WritablePExpression<Double> xmax = Props2.of("xmax").doubleOf(Double.POSITIVE_INFINITY);
    private WritablePExpression<Double> ymin = Props2.of("ymin").doubleOf(Double.NEGATIVE_INFINITY);
    private WritablePExpression<Double> ymax = Props2.of("ymax").doubleOf(Double.POSITIVE_INFINITY);
    private WritablePExpression<Double> zmin = Props2.of("zmin").doubleOf(Double.NEGATIVE_INFINITY);
    private WritablePExpression<Double> zmax = Props2.of("zmax").doubleOf(Double.POSITIVE_INFINITY);

    public DomainTemplate() {
    }

    public DomainTemplate(Domain d) {
        set(d);
    }

    public void set(Domain d) {
        this.xmin.set(String.valueOf(d.xmin()));
        this.xmax.set(String.valueOf(d.xmax()));
        this.ymin.set(String.valueOf(d.ymin()));
        this.ymax.set(String.valueOf(d.ymax()));
        this.zmin.set(String.valueOf(d.zmin()));
        this.zmax.set(String.valueOf(d.zmax()));
    }

    public WritablePExpression<Double> xmin() {
        return xmin;
    }

    public WritablePExpression<Double> xmax() {
        return xmax;
    }

    public WritablePExpression<Double> ymin() {
        return ymin;
    }

    public WritablePExpression<Double> ymax() {
        return ymax;
    }

    public WritablePExpression<Double> zmin() {
        return zmin;
    }

    public WritablePExpression<Double> zmax() {
        return zmax;
    }

    public void setDomain(Domain d) {
        xmin.set(String.valueOf(d.xmin()));
        xmax.set(String.valueOf(d.xmax()));
        ymin.set(String.valueOf(d.ymin()));
        ymax.set(String.valueOf(d.ymax()));
        zmin.set(String.valueOf(d.zmin()));
        zmax.set(String.valueOf(d.zmax()));
    }

    public Domain eval(HWProjectEnv env) {
        return Domain.ofBounds(
                xmin().eval(env),
                xmax().eval(env),
                ymin().eval(env),
                ymax().eval(env),
                zmin().eval(env),
                zmax().eval(env)
        );
    }
}
