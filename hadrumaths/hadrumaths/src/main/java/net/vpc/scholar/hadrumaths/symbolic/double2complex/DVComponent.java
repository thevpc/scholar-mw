package net.vpc.scholar.hadrumaths.symbolic.double2complex;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.util.InternalUnmodifiableSingletonList;

import java.util.List;

/**
 * User: taha Date: 2 juil. 2003 Time: 10:39:39
 */
public class DVComponent implements DoubleToComplex {

    private static final long serialVersionUID = 1L;

    static {
        FormatFactory.register(DVComponent.class, new ObjectFormat<DVComponent>() {
            @Override
            public void format(DVComponent o, ObjectFormatContext context) {
                context.append("dvcomp(");
                context.append(o.getChild(0));
                context.append(",");
                context.append(o.getComponentIndex());
                context.append(")");
            }
        });
    }

    protected DoubleToVector vector;
    protected int componentIndex;

    public DVComponent(DoubleToVector vector, int componentIndex) {
        this.vector = vector;
        this.componentIndex = componentIndex;
    }

    public boolean isInvariant(Axis axis) {
        return false;
    }

    public List<Expr> getChildren() {
        return InternalUnmodifiableSingletonList.of(vector);
    }

    public Domain getDomain() {
        return vector.getDomain();
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return of(subExpressions[0].toDV(), componentIndex);
    }

    public static DVComponent of(DoubleToVector vector, int componentIndex) {
        return new DVComponent(vector, componentIndex);
    }

    public int getComponentIndex() {
        return componentIndex;
    }

    public Complex[] evalComplex(double[] x, Domain d0, Out<Range> ranges) {
        return v1toc1(vector.evalVector(x, d0, ranges));
    }

    private Complex[] v1toc1(ComplexVector[] cv) {
        Complex[] cc = new Complex[cv.length];
        for (int i = 0; i < cc.length; i++) {
            cc[i] = cv[i].get(componentIndex);
        }
        return cc;
    }

    public Complex[] evalComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
        return v1toc1(vector.evalVector(x, y, d0, ranges));
    }

    public Complex[] evalComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
        return v1toc1(vector.evalVector(x, y, d0, ranges));
    }

    @Override
    public Complex[][][] evalComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return v3toc3(vector.evalVector(x, y, z, d0, ranges));
    }

    private Complex[][][] v3toc3(ComplexVector[][][] cv) {
        Complex[][][] cc = new Complex[cv.length][][];
        for (int i = 0; i < cc.length; i++) {
            cc[i] = v2toc2(cv[i]);
        }
        return cc;
    }

    private Complex[][] v2toc2(ComplexVector[][] cv) {
        Complex[][] cc = new Complex[cv.length][];
        for (int i = 0; i < cc.length; i++) {
            cc[i] = v1toc1(cv[i]);
        }
        return cc;
    }

    public Complex[][] evalComplex(double[] x, double[] y, Domain d0) {
        return v2toc2(vector.evalVector(x, y, d0));
    }

    public Complex[][] evalComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return v2toc2(vector.evalVector(x, y, d0, ranges));
    }

    public Complex evalComplex(double x, BooleanMarker defined) {
        return v0toc0(vector.evalVector(x, defined));
    }

    private Complex v0toc0(ComplexVector cv) {
        return cv.get(componentIndex);
    }

    public Complex evalComplex(double x, double y, BooleanMarker defined) {
        return v0toc0(vector.evalVector(x, y, defined));
    }

    public Complex evalComplex(double x, double y, double z, BooleanMarker defined) {
        return v0toc0(vector.evalVector(x, y, z, defined));
    }

    @Override
    public ExprType getType() {
        return ExprType.DOUBLE_COMPLEX;
    }

    @Override
    public int hashCode() {
        int hash = getClass().hashCode();
        hash = 97 * hash + this.vector.hashCode();
        hash = 97 * hash + componentIndex;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof DVComponent)) {
            return false;
        }
        DVComponent c = (DVComponent) obj;
        return c.vector.equals(vector) && c.componentIndex == componentIndex;
    }

    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }

    @Override
    public String toLatex() {
        throw new UnsupportedOperationException("Not Implemented toLatex for "+getClass().getName());
    }

}
