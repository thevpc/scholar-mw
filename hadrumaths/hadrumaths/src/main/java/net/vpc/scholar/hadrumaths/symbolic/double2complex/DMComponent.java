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
public class DMComponent implements DoubleToComplex {

    private static final long serialVersionUID = 1L;

    static {
        FormatFactory.register(DMComponent.class, new ObjectFormat<DMComponent>() {
            @Override
            public void format(DMComponent o, ObjectFormatContext context) {
                context.append("dmcomp(");
                context.append(o.getChild(0));
                context.append(",");
                context.append(o.getRow());
                context.append(",");
                context.append(o.getColumn());
                context.append(")");
            }
        });
    }

    protected DoubleToMatrix matrix;
    protected int row;
    protected int column;

    public DMComponent(DoubleToMatrix matrix, int row, int column) {
        this.matrix = matrix;
        this.row = row;
        this.column = column;
    }

    public boolean isInvariant(Axis axis) {
        return false;
    }

    public List<Expr> getChildren() {
        return InternalUnmodifiableSingletonList.of(matrix);
    }

    public Domain getDomain() {
        return matrix.getDomain();
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return of(subExpressions[0].toDM(), row, column);
    }

    public static DMComponent of(DoubleToMatrix matrix, int row, int col) {
        return new DMComponent(matrix, row, col);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Complex[] evalComplex(double[] x, Domain d0, Out<Range> ranges) {
        return v1toc1(matrix.evalMatrix(x, d0, ranges));
    }

    private Complex[] v1toc1(ComplexMatrix[] cv) {
        Complex[] cc = new Complex[cv.length];
        for (int i = 0; i < cc.length; i++) {
            cc[i] = cv[i].get(row);
        }
        return cc;
    }

    public Complex[] evalComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
        return v1toc1(matrix.evalMatrix(x, y, d0, ranges));
    }

    public Complex[] evalComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
        return v1toc1(matrix.evalMatrix(x, y, d0, ranges));
    }

    @Override
    public Complex[][][] evalComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return v3toc3(matrix.evalMatrix(x, y, z, d0, ranges));
    }

    private Complex[][][] v3toc3(ComplexMatrix[][][] cv) {
        Complex[][][] cc = new Complex[cv.length][][];
        for (int i = 0; i < cc.length; i++) {
            cc[i] = v2toc2(cv[i]);
        }
        return cc;
    }

    private Complex[][] v2toc2(ComplexMatrix[][] cv) {
        Complex[][] cc = new Complex[cv.length][];
        for (int i = 0; i < cc.length; i++) {
            cc[i] = v1toc1(cv[i]);
        }
        return cc;
    }

    public Complex[][] evalComplex(double[] x, double[] y, Domain d0) {
        return v2toc2(matrix.evalMatrix(x, y, d0));
    }

    public Complex[][] evalComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return v2toc2(matrix.evalMatrix(x, y, d0, ranges));
    }

    public Complex evalComplex(double x, BooleanMarker defined) {
        return v0toc0(matrix.evalMatrix(x, defined));
    }

    private Complex v0toc0(ComplexMatrix cv) {
        return cv.get(row);
    }

    public Complex evalComplex(double x, double y, BooleanMarker defined) {
        return v0toc0(matrix.evalMatrix(x, y, defined));
    }

    public Complex evalComplex(double x, double y, double z, BooleanMarker defined) {
        return v0toc0(matrix.evalMatrix(x, y, z, defined));
    }

    @Override
    public ExprType getType() {
        return ExprType.DOUBLE_COMPLEX;
    }

    @Override
    public int hashCode() {
        int hash = getClass().hashCode();
        hash = 97 * hash + this.matrix.hashCode();
        hash = 97 * hash + row;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof DMComponent)) {
            return false;
        }
        DMComponent c = (DMComponent) obj;
        return c.matrix.equals(matrix) && c.row == row;
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
