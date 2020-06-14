package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.Param;
import net.vpc.scholar.hadrumaths.symbolic.double2complex.ComplexParam;
import net.vpc.scholar.hadrumaths.symbolic.double2double.DoubleParam;
import net.vpc.scholar.hadrumaths.symbolic.double2matrix.MatrixParam;
import net.vpc.scholar.hadrumaths.symbolic.double2vector.VectorParam;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultParamValues implements ParamValues {
    private Map<String, ParamVal> vals = new HashMap<>();
    private Map<Param, Expr> valCache;

    @Override
    public ParamValues set(String name, double d) {
        _set(new DoubleParam(name), Maths.expr(d));
        return this;
    }

    @Override
    public ParamValues set(Param p, double d) {
        if(p instanceof DoubleParam) {
            _set(p, Maths.expr(d));
        }else{
            _set(new DoubleParam(p.getName()), Maths.expr(d));
        }
        return this;
    }

    @Override
    public ParamValues set(Param p, Complex d) {
        if(p instanceof DoubleParam) {
            _set(p, Maths.expr(d));
        }else{
            _set(new DoubleParam(p.getName()), Maths.expr(d));
        }
        return this;
    }

    @Override
    public ParamValues set(Param name, Expr d) {
        _set(name,d);
        return this;
    }

    private void _set(Param p, Expr v) {
        if (v == null) {
            vals.remove(p.getName());
        } else {
            vals.put(p.getName(), new ParamVal(p.getName(), p, v));
        }
        valCache = null;
    }

    @Override
    public ParamValues set(String name, Complex d) {
        _set(new ComplexParam(name), Maths.expr(d));
        return this;
    }

    @Override
    public ParamValues remove(String name) {
        vals.remove(name);
        valCache=null;
        return this;
    }

    @Override
    public ParamValues set(String name, Expr d) {
        switch (d.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                _set(new DoubleParam(name), Maths.expr(d));
                break;
            }
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                _set(new ComplexParam(name), Maths.expr(d));
                break;
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR: {
                _set(new VectorParam(name), Maths.expr(d));
                break;
            }
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX: {
                _set(new MatrixParam(name), Maths.expr(d));
                break;
            }
            default: {
                throw new IllegalArgumentException("Not supported");
            }
        }
        return this;
    }

    @Override
    public Set<Param> getParams() {
        return getValCache().keySet();
    }

    private Map<Param, Expr> getValCache() {
        if (valCache == null) {
            Map<Param, Expr> valCache0 = new HashMap<>();
            for (Map.Entry<String, ParamVal> e : vals.entrySet()) {
                valCache0.put(e.getValue().param, e.getValue().value);
            }
            valCache = valCache0;
        }
        return valCache;
    }

    public Map<Param, Expr> getParamMap() {
        return getValCache();
    }

    @Override
    public boolean contains(String name) {
        return vals.containsKey(name);
    }

    @Override
    public Expr getValue(String name) {
        ParamVal t = vals.get(name);
        return t == null ? null : t.value;
    }

    private static class ParamVal {
        String name;
        Param param;
        Expr value;

        public ParamVal(String name, Param param, Expr value) {
            this.name = name;
            this.param = param;
            this.value = value;
        }
    }
}
