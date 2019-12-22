package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.common.util.TypeName;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.dump.Dumpable;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;

import java.util.Arrays;

/**
 * Created by vpc on 5/30/14.
 */
public class ParamExprList extends AbstractTVector<Expr> implements Dumpable, Cloneable {

    private static final long serialVersionUID = 1L;
    private double[][] values;
    private Expr pattern;
    private DoubleParam[] vars;
    private String valuesDesc;

    ParamExprList(boolean row, Expr pattern, DoubleParam[] vars, double[][] values, String valuesDesc) {
        super(row);
        this.pattern = pattern;
        this.vars = vars;
        this.values = values;
        this.valuesDesc = valuesDesc;
        if (vars.length < 1) {
            throw new IllegalArgumentException("Missing vars");
        }
        for (int i = 0; i < vars.length; i++) {
            if (vars[i] == null) {
                throw new IllegalArgumentException("Missing var at " + i);
            }
        }
    }

    public static ParamExprList create(boolean row, Expr pattern, DoubleParam[] vars, int[] max) {
        StringBuilder valuesDesc = new StringBuilder();
        if (vars.length < 1) {
            throw new IllegalArgumentException("Missing vars");
        }
        if (max.length != vars.length) {
            throw new IllegalArgumentException("Invalid vars values");
        }
        for (int i = 0; i < max.length; i++) {
            if (vars[i] == null) {
                throw new IllegalArgumentException("Missing var at " + i);
            }
            if (max[i] < 1) {
                throw new IllegalArgumentException("Invalid max value for " + vars[i] + ". Should be >=1");
            }
            if (valuesDesc.length() == 0) {
                valuesDesc.append("0..").append(max[i] - 1);
            } else {
                valuesDesc.append(", ").append("0..").append(max[i] - 1);
            }
        }
        double[][] values;
        switch (vars.length) {
            case 1: {
                values = new double[max[0]][vars.length];
                for (int i = 0; i < values.length; i++) {
                    values[i][0] = i;
                }
                break;
            }
            case 2: {
                values = MathsBase.cross(MathsBase.dsteps(0, max[0] - 1, 1), MathsBase.dsteps(0, max[1] - 1, 1));
                break;
            }
            default: {
                throw new IllegalArgumentException("Too many vars " + vars.length + ">2, unsupported yet");
            }
        }
        return new ParamExprList(row, pattern, vars, values, valuesDesc.toString());
    }

    public static ParamExprList create(boolean row, Expr pattern, DoubleParam[] vars, double[][] values) {
        return new ParamExprList(row, pattern, vars, values, MathsBase.dump(values));
    }

    @Override
    public TypeName<Expr> getComponentType() {
        return MathsBase.$EXPR;
    }

    //    @Override
    public ExprVector toList() {
        ArrayExprVector list = new ArrayExprVector(isRow(), values.length);
        String[] vname = new String[vars.length];
        for (int i = 0; i < vname.length; i++) {
            vname[i] = vars[i].getParamName();
        }
        for (double[] value : values) {
            Expr e = pattern;
            for (int i = 0; i < vname.length; i++) {
                e = e.setParam(vname[i], value[i]);
            }
            for (int i = 0; i < vname.length; i++) {
                e = e.setProperty(vname[i], value[i]);
            }
            list.append(e);
        }
        return list;
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public Expr get(int index) {
        Expr e = pattern;
        String[] vname = new String[vars.length];
        for (int i = 0; i < vname.length; i++) {
            vname[i] = vars[i].getParamName();
        }
        double[] value = values[index];
        for (int i = 0; i < vname.length; i++) {
            e = e.setParam(vname[i], value[i]);
        }
        for (int i = 0; i < vname.length; i++) {
            e = e.setProperty(vname[i], value[i]);
        }
        return e;
    }

    public Dumper getDumpStringHelper() {
        Dumper h = new Dumper(getClass().getSimpleName());
        h.add("pattern", pattern);
        StringBuilder varsString = new StringBuilder();
        for (DoubleParam var : vars) {
            if (varsString.length() > 0) {
                varsString.append(", ");
            }
            varsString.append(var.getParamName());
        }
        h.add("vars", varsString);
        h.add("values", valuesDesc);
        return h;
    }

    @Override
    public TVector<Expr> set(int index, Expr e) {
        throw new IllegalArgumentException("Unmodifiable");
    }

    @Override
    public String dump() {
        return getDumpStringHelper().toString();
    }

    @Override
    public String toString() {
        return "ParamExprList{"
                + "values=" + Arrays.deepToString(values)
                + ", pattern=" + pattern
                + ", vars=" + Arrays.toString(vars)
                + ", valuesDesc='" + valuesDesc + '\''
                + '}';
    }

//    @Override
//    public TList<T> sort() {
//        return copy().sort();
//    }
//
//    @Override
//    public TList<T> removeDuplicates() {
//        return copy().removeDuplicates();
//    }
//    @Override
//    public TList<Expr> copy() {
//        try {
//            return (TList<Expr>) super.clone();
//        } catch (CloneNotSupportedException e) {
//            throw new IllegalArgumentException("Unsupported clone");
//        }
//    }
    @Override
    public TVector<Expr> sort() {
        return copy().sort();
    }

    @Override
    public TVector<Expr> removeDuplicates() {
        return copy().removeDuplicates();
    }
}
