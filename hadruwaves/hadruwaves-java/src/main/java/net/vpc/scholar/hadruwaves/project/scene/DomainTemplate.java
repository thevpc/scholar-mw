package net.vpc.scholar.hadruwaves.project.scene;

import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.common.tson.TsonSerializable;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadruwaves.project.Props2;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwaves.props.WritablePExpression;

public class DomainTemplate implements TsonSerializable {

    private final WritablePExpression<Double> xmin = Props2.of("xmin").exprLenOf(Double.NEGATIVE_INFINITY);
    private final WritablePExpression<Double> xmax = Props2.of("xmax").exprLenOf(Double.POSITIVE_INFINITY);
    private final WritablePExpression<Double> ymin = Props2.of("ymin").exprLenOf(Double.NEGATIVE_INFINITY);
    private final WritablePExpression<Double> ymax = Props2.of("ymax").exprLenOf(Double.POSITIVE_INFINITY);
    private final WritablePExpression<Double> zmin = Props2.of("zmin").exprLenOf(Double.NEGATIVE_INFINITY);
    private final WritablePExpression<Double> zmax = Props2.of("zmax").exprLenOf(Double.POSITIVE_INFINITY);

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

    public Domain eval(HWConfigurationRun configuration) {
        return Domain.ofBounds(
                xmin().eval(configuration),
                xmax().eval(configuration),
                ymin().eval(configuration),
                ymax().eval(configuration),
                zmin().eval(configuration),
                zmax().eval(configuration)
        );
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.obj("domain")
                .add("xmin", xmin().get())
                .add("xmax", xmax().get())
                .add("ymin", ymin().get())
                .add("ymax", ymax().get())
                .add("zmin", zmin().get())
                .add("zmax", zmax().get())
                .build();
    }

}
