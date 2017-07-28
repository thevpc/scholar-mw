package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.ArrayExprList;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;
import net.vpc.scholar.hadrumaths.util.dump.Dumpable;

import java.util.Arrays;

/**
 * Created by vpc on 5/30/14.
 */
public class ParamExprList extends AbstractExprList implements ExprList, Dumpable,Cloneable {
    double[][] values;
    private Expr pattern;
    private DoubleParam[] vars;
    private String valuesDesc;

    ParamExprList(Expr pattern, DoubleParam[] vars, double[][] values, String valuesDesc) {
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

    public static ParamExprList create(Expr pattern, DoubleParam[] vars, int[] max) {
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
                valuesDesc.append("0..").append(max[i]-1);
            } else {
                valuesDesc.append(", ").append("0..").append(max[i]-1);
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
                values = Maths.cross(Maths.dsteps(0, max[0] - 1, 1), Maths.dsteps(0, max[1] - 1, 1));
                break;
            }
            default: {
                throw new IllegalArgumentException("Too many vars " + vars.length + ">2, unsupported yet");
            }
        }
        return new ParamExprList(pattern, vars, values, valuesDesc.toString());
    }

    public static ParamExprList create(Expr pattern, DoubleParam[] vars, double[][] values) {
        return new ParamExprList(pattern, vars, values, Maths.dump(values));
    }

    @Override
    public ArrayExprList toList() {
        ArrayExprList list = new ArrayExprList(values.length);
        String[] vname = new String[vars.length];
        for (int i = 0; i < vname.length; i++) {
            vname[i] = vars[i].getName();
        }
        for (double[] value : values) {
            Expr e = pattern;
            for (int i = 0; i < vname.length; i++) {
                e = e.setParam(vname[i], value[i]);
            }
            for (int i = 0; i < vname.length; i++) {
                e = e.setProperty(vname[i], value[i]);
            }
            list.add(e);
        }
        return list;
    }

    @Override
    public int length() {
        return values.length;
    }

    @Override
    public Expr get(int index) {
        Expr e = pattern;
        String[] vname = new String[vars.length];
        for (int i = 0; i < vname.length; i++) {
            vname[i] = vars[i].getName();
        }
        double[] value=values[index];
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
        StringBuilder varsString=new StringBuilder();
        for (DoubleParam var : vars) {
            if(varsString.length()>0){
                varsString.append(", ");
            }
            varsString.append(var.getName());
        }
        h.add("vars", varsString);
        h.add("values", valuesDesc);
        return h;
    }

    @Override
    public void set(int index, Expr e) {
        throw new IllegalArgumentException("Unmodifiable");
    }

    @Override
    public String dump() {
        return getDumpStringHelper().toString();
    }

    @Override
    public String toString() {
        return "ParamExprList{" +
                "values=" + Arrays.deepToString(values) +
                ", pattern=" + pattern +
                ", vars=" + Arrays.toString(vars) +
                ", valuesDesc='" + valuesDesc + '\'' +
                '}';
    }

    @Override
    public ExprList copy() {
        try {
            return (ExprList) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException("Unsupported clone");
        }
    }
}
