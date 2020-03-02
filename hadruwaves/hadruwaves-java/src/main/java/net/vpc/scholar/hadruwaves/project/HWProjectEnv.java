package net.vpc.scholar.hadruwaves.project;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.Param;
import net.vpc.scholar.hadruwaves.project.configuration.HWSConfigurationRun;
import net.vpc.scholar.hadruwaves.project.parameter.HWSParameterValue;

import java.util.HashMap;
import java.util.Map;

import static net.vpc.scholar.hadrumaths.Maths.parseExpression;

public class HWProjectEnv {
    private HWProject project;
    private HWSConfigurationRun configuration;
    private Map<String, Object> cache = new HashMap<>();
    private Map<String, HWSParameterValue> parametersMap;

    public HWProjectEnv(HWProject project, HWSConfigurationRun configuration) {
        this.project = project;
        this.configuration = configuration;
        this.parametersMap = project==null?new HashMap<>() : project.getParametersMap();
    }

    public HWProject getProject() {
        return project;
    }

    public HWSConfigurationRun getConfiguration() {
        return configuration;
    }

    public Double evalExprDouble(String expression, Double def) {
        Object o = evalExpr(expression);
        if(o==null){
            return def;
        }
        if(o instanceof Expr){
            return ((Expr) o).toDouble();
        }
        throw new ClassCastException(o.toString()+" : expected double");
    }

    public Complex evalExprComplex(String expression, Complex def) {
        Object o = evalExpr(expression);
        if(o==null){
            return def;
        }
        if(o instanceof Expr){
            return ((Expr) o).toComplex();
        }
        throw new ClassCastException(o.toString()+" : expected Complex");
    }

    public Expr evalExprComplex(String expression, Expr def) {
        Object o = evalExpr(expression);
        if(o==null){
            return def;
        }
        if(o instanceof Expr){
            return ((Expr) o);
        }
        throw new ClassCastException(o.toString()+" : expected Expr");
    }

    public Boolean evalBoolean(String expression,Boolean defaultValue) {
        if(expression==null || expression.trim().isEmpty()){
            return defaultValue;
        }
        switch (expression.trim().toLowerCase()){
            case "true":
            case "yes":{
                return true;
            }
            case "false":
            case "no":{
                return false;
            }
        }
        Double aDouble = evalExprDouble(expression, null);
        if(aDouble==null){
            return defaultValue;
        }
        return aDouble.doubleValue()!=0;
    }

    public String evalString(String expression) {
        return expression;
    }

    public Object evalExpr(String expression) {
        if (expression == null) {
            return null;
        }
        Expr e = parseExpression(expression);
        ParamValues pv = new DefaultParamValues();
        for (Param param : e.getParams()) {
            Object c = cache.get(param.getName());
            if (c != null) {
                if (c.getClass().equals(Object.class)) {
                    throw new IllegalArgumentException("Unable to resolve " + param.getName() + ". Recursion detected.");
                }
            } else {
                c = evalParam(param.getName());
            }
            if (c instanceof Double) {
                pv.set(param, (Double) c);
            } else if (c instanceof Complex) {
                pv.set(param, (Complex) c);
            } else if (c instanceof Expr) {
                pv.set(param, (Expr) c);
            } else {
                throw new IllegalArgumentException("Unexpected type " + c.getClass().getName() + " for " + param.getName());
            }
        }
        return e.setParams(pv).simplify();
    }

    public Object evalParam(String parameterName) {
        cache.put(parameterName, new Object());
        HWSParameterValue m = parametersMap.get(parameterName);
        if (m == null) {
            throw new IllegalArgumentException("Parameter not found " + parameterName);
        }
        String expression = configuration==null?null:configuration.parameters().get(parameterName);
        Object o = evalExpr(expression);
        cache.put(parameterName, o);
        return o;
    }

}
