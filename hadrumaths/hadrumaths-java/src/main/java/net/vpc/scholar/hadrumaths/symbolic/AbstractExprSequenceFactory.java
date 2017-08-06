package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.TList;

/**
 * Created by vpc on 2/14/15.
 */
public abstract class AbstractExprSequenceFactory<T extends Expr> implements ExprSequenceFactory<T> {
    @Override
    public Class<T> getComponentType() {
        return (Class<T>) Expr.class;
    }

    public TList<T> newSequence(T pattern, DoubleParam[] vars, int[] max) {
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
                values = Maths.cross(Maths.dsteps(0, max[0] - 1, 1), Maths.dsteps(0, max[1] - 1, 1));
                break;
            }
            default: {
                throw new IllegalArgumentException("Too many vars " + vars.length + ">2, unsupported yet");
            }
        }
        return (TList<T>) new ParamExprList(false,pattern, vars, values, valuesDesc.toString());
    }

    public TList<Expr> newSequence(Expr pattern, DoubleParam[] vars, double[][] values) {
        return new ParamExprList(false,pattern, vars, values, Maths.dump(values));
    }

}
