/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.project;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.expeval.ExpressionEvaluator;
import net.vpc.scholar.hadrumaths.expeval.ExpressionFunction;
import net.vpc.scholar.hadrumaths.expeval.ExpressionStreamTokenizer;
import net.vpc.scholar.hadrumaths.expeval.NoSuchVariableException;
import net.vpc.scholar.hadrumaths.expeval.operators.OpListComma;
import net.vpc.scholar.hadruwaves.mom.CircuitType;
import net.vpc.scholar.hadruwaves.mom.ProjectType;
import net.vpc.scholar.hadruwaves.Wall;
import net.vpc.scholar.hadruwaves.mom.project.common.VarUnit;
import net.vpc.scholar.hadruwaves.mom.project.common.VariableExpression;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.util.MomUtils;

/**
 *
 * @author vpc
 */
public class VarEvaluator {

    private Map<String, Object> workingVars = new HashMap<String, Object>();

    private static Map<String, VariableExpression> expressions0 = new HashMap<String, VariableExpression>();
    private static Map<String, VariableExpression> expressions = new HashMap<String, VariableExpression>();
    private static final Object EVALUATING = new Object();
    private double dimensionUnit = 1;
    private double frequencyUnit = 1;
    private File folder;

    static {
        expressions0.put("LAMBDA", new VariableExpression("LAMBDA", "C/FREQUENCE", VarUnit.NUMBER, ""));
        expressions0.put("OMEGA", new VariableExpression("OMEGA", "2*PI*FREQUENCE", VarUnit.NUMBER, ""));
        expressions0.put("K0", new VariableExpression("K0", "2*PI*FREQUENCE/C", VarUnit.NUMBER, ""));
    }

    public void clearCache() {
        workingVars.clear();
    }

    public void addExpressions(Collection<VariableExpression> exprs) {
        clearCache();
        for (VariableExpression variableExpression : exprs) {
            expressions.put(variableExpression.getName(), variableExpression);
        }
    }

    public void clearExpressions() {
        clearCache();
        expressions.clear();
    }

    public void update(MomProject momProject) {
        clearCache();
        clearExpressions();
        if (momProject != null) {
            for (VariableExpression variableExpression : momProject.getVariableExpressions()) {
                expressions.put(variableExpression.getName(), variableExpression);
            }
            dimensionUnit = momProject.getDimensionUnit();
            frequencyUnit = momProject.getFrequencyUnit();
            File configFile = momProject.getConfigFile();
            folder = configFile == null ? new File(".") : configFile.getParentFile();
        } else {
            dimensionUnit = 1;
            frequencyUnit = 1;
            folder = new File(".");
        }
    }

    public synchronized Object getVar(String name) {
        Object o = workingVars.get(name);
        if (o == EVALUATING) {
            throw new IllegalStateException("Counld nor retrieve '" + name + "' since it is not yet evaluated");
        }
        if (o instanceof Complex) {
            return (Complex) o;
        }
        RuntimeException exc = null;
        try {
            workingVars.put(name, EVALUATING);
            VariableExpression variableExpression = expressions.get(name);
            if (variableExpression == null) {
                variableExpression = expressions0.get("name");
            }
            if (variableExpression == null) {
                throw new NoSuchElementException("Unrecognized symbol \"" + name + "\"");
            }
            String expr = variableExpression.getExpression();
            VarUnit unit = variableExpression.getUnit();

            Object c = evaluate(expr, unit);
            workingVars.put(name, c);
            return c;
        } catch (RuntimeException e) {
            exc = e;
            throw e;
        } finally {
            o = workingVars.get(name);
            if (o == EVALUATING) {
                System.err.println("Problem evaluating " + name + " discarded..." + (exc == null ? "" : exc.toString()));
                workingVars.remove(name);
            }
        }
    }

    public Complex evaluateComplex(String expr) {
        return evaluateComplex(expr, VarUnit.NUMBER);
    }

    public double evaluateDouble(String expr) {
        Complex c = evaluateComplex(expr, VarUnit.NUMBER);
        if (c.getImag() == 0) {
            return c.getReal();
        }
        throw new ClassCastException("Expected a real value but got " + c);
    }

    public int evaluateInt(String expr) {
        Complex c = evaluateComplex(expr, VarUnit.INT);
        if (c.getImag() == 0) {
            double d = c.getReal();
            double r = Math.round(c.getReal());
            if (Math.abs(d - r) >= 0.1) {
                throw new ClassCastException("Expected an integer value but got " + c);
            }
            return (int) r;
        }
        throw new ClassCastException("Expected a real value but got " + c);
    }

    public double getVarDouble(String name) {
        return getVarComplex(name).doubleValue();
    }

    public Complex getVarComplex(String name) {
        return (Complex)getVar(name);
    }

    public Complex evaluateComplex(String expr, VarUnit unit) {
        return (Complex)evaluate(expr, unit);
    }
    
    public Object evaluate(String expr, VarUnit unit) {
        return evaluate(expr, unit,0);
    }

    public Object evaluate(String expr, VarUnit unit,int depth) {
        if(depth>50){
            throw new IllegalArgumentException("Expression too complex");
        }
        
        ExpressionStreamTokenizer tok = new ExpressionStreamTokenizer(new StringReader(expr));
        StringBuilder expr2 = new StringBuilder();
        try {
            while (tok.nextToken() != ExpressionStreamTokenizer.TT_EOF) {
                switch (tok.ttype) {
                    case ExpressionStreamTokenizer.TT_EOL: {
                        expr2.append("\n");
                        break;
                    }
                    case ExpressionStreamTokenizer.TT_NUMBER: {
                        expr2.append(String.valueOf(tok.nval));
                        break;
                    }
                    case ExpressionStreamTokenizer.TT_STRING: {
                        expr2.append("\"").append(String.valueOf(tok.nval)).append("\"");
                        break;
                    }
                    case ExpressionStreamTokenizer.TT_COMPLEX: {
                        expr2.append(String.valueOf(tok.cval));
                        break;
                    }
                    case ExpressionStreamTokenizer.TT_WORD: {
                        VariableExpression v2 = expressions.get(tok.sval);
                        if (v2 == null) {
                            expr2.append(tok.sval);
                        } else {
                            switch (v2.getUnit()) {
                                case LEN: {
                                    expr2.append("((" + tok.sval + ")/DIM_UNIT)");
                                    break;
                                }
                                case LAMBDA: {
                                    expr2.append("((" + tok.sval + ")/LAMBDA)");
                                    break;
                                }
                                case FREQ: {
                                    expr2.append("((" + tok.sval + ")/FREQ_UNIT)");
                                    break;
                                }
                                default: {
                                    expr2.append(tok.sval);
                                    break;
                                }
                            }
                        }
                        break;
                    }
                    default: {
                        expr2.append((char) tok.ttype);
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Expression");
        }
        switch (unit) {
            case LEN: {
                expr = "((" + expr2.toString() + ")*DIM_UNIT)";
                break;
            }
            case FREQ: {
                expr = "((" + expr2.toString() + ")*FREQ_UNIT)";
                break;
            }
            case LAMBDA: {
                expr = "((" + expr2.toString() + ")*LAMBDA)";
                break;
            }
            case INT: {
                expr = "(toint(real(" + expr2.toString() + ")))";
                break;
            }
            case NUMBER: {
                expr = expr2.toString();
                break;
            }
            case WALL: {
                expr = expr2.toString();
                if(expressions.containsKey(expr)){
                    return evaluate(expr, unit,depth+1);
                }else{
                    Wall t = (Wall)MomUtils.getEnum(expr, Wall.class);
                    if(t!=null){
                        return t;
                    }
                    if(expr.trim().length()>0){
                        throw new IllegalArgumentException("Invalid "+expr);
                    }
                    return null;
                }
            }
            case CIRCUIT_TYPE: {
                expr = expr2.toString();
                if(expressions.containsKey(expr)){
                    return evaluate(expr, unit,depth+1);
                }else{
                    CircuitType t = (CircuitType)MomUtils.getEnum(expr, CircuitType.class);
                    if(t!=null){
                        return t;
                    }
                    if(expr.trim().length()>0){
                        throw new IllegalArgumentException("Invalid "+expr);
                    }
                    return null;
                }
            }
            case PROJECT_TYPE: {
                expr = expr2.toString();
                if(expressions.containsKey(expr)){
                    return evaluate(expr, unit,depth+1);
                }else{
                    ProjectType t = (ProjectType)MomUtils.getEnum(expr, ProjectType.class);
                    if(t!=null){
                        return t;
                    }
                    if(expr.trim().length()>0){
                        throw new IllegalArgumentException("Invalid "+expr);
                    }
                    return null;
                }
            }
        }
        ExpressionEvaluator parser = new ExpressionEvaluator();
        parser.addDefaults();

        parser.declare("DIM_UNIT", dimensionUnit);
        parser.declare("FREQ_UNIT", frequencyUnit);
        parser.declare("C", Maths.C);
        parser.declare("U0", Maths.U0);
        parser.declare("EPS0", Maths.EPS0);

        parser.declare(new ZinFunction());
        parser.declare(new CapaFunction());
        parser.declare(new SindicesFunction());

        for (Iterator i = workingVars.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (v instanceof Complex) {
                Complex c = (Complex) v;
                parser.declare(k, ((Complex) v));
            }
        }

        while (true) {
            try {
                Object n = parser.evaluate(expr);
                if (n instanceof Complex) {
                    return (Complex) n;
                } else if (n instanceof Number) {
                    return Complex.valueOf(((Number) n).doubleValue());
                }
                return Complex.NaN;
            } catch (NoSuchVariableException var) {
                Complex c2 = getVarComplex(var.getVarName());//

                parser.declare(var.getVarName(), c2.doubleValue());
            }
        }
    }

    private abstract class SubStrFunction extends ExpressionFunction {

        public SubStrFunction(String name) {
            super(name);
        }

        @Override
        public Object evaluate(OpListComma.Uplet params, Map<String, Object> variables) {
            MomStructure str = null;
            try {
                String n = (String) params.get(0);
                Map<String, String> mapping = new HashMap<String, String>();
                if (n.endsWith(")")) {
                    String nn = n.substring(n.indexOf('(') + 1, n.length() - 1);
                    n = n.substring(0, n.indexOf('('));
                    String[] m = nn.split(",");
                    for (String mm : m) {
                        int eq = mm.indexOf('=');
                        if (eq > 0) {
                            mapping.put(mm.substring(0, eq).trim(), mm.substring(eq + 1).trim());
                        } else {
                            throw new IllegalArgumentException("expected : structurename(var1=var2,var3=var4)");
                        }
                    }
                }
                File fn = folder;
                MomProject prj = new MomProject(new File(fn.getParent(), n + ".str"));
                Map<String, VariableExpression> actualVars = new HashMap<String, VariableExpression>();
                for (VariableExpression variableExpression : expressions.values()) {
                    actualVars.put(variableExpression.getName(), variableExpression);
                }
                for (VariableExpression variableExpression : prj.getVariableExpressions()) {
                    String goodName = mapping.get(variableExpression.getName());
                    if (goodName == null) {
                        goodName = variableExpression.getName();
                    }
                    VariableExpression e = actualVars.get(goodName);
                    if (e != null) {
                        prj.setExpression(e.clone());
                    }
                }

                str = new MomStructure();
                str.loadProject(prj);
            } catch (ParseException ex) {
                Logger.getLogger(VarEvaluator.class.getName()).log(Level.SEVERE, null, ex);
                throw new IllegalArgumentException(ex);
            } catch (IOException ex) {
                Logger.getLogger(VarEvaluator.class.getName()).log(Level.SEVERE, null, ex);
                throw new IllegalArgumentException(ex);
            }
            return evaluate(str, params, variables);
        }

        public abstract Object evaluate(MomStructure str, OpListComma.Uplet params, Map<String, Object> variables);
    }

    private class ZinFunction extends SubStrFunction {

        public ZinFunction() {
            super("zin");
        }

        @Override
        public Object evaluate(MomStructure str, OpListComma.Uplet params, Map<String, Object> variables) {
            int ii = 0;
            int jj = 0;
            if (params.size() >= 3) {
                ii = ((Number) params.get(1)).intValue() - 1;
                jj = ((Number) params.get(2)).intValue() - 1;
            }
            return str.self().computeMatrix().get(ii, jj);
        }
    }

    private class CapaFunction extends SubStrFunction {

        public CapaFunction() {
            super("capa");
        }

        @Override
        public Object evaluate(MomStructure str, OpListComma.Uplet params, Map<String, Object> variables) {
            int ii = 0;
            int jj = 0;
            if (params.size() >= 3) {
                ii = ((Number) params.get(1)).intValue() - 1;
                jj = ((Number) params.get(2)).intValue() - 1;
            }
            return str.capacity().computeMatrix().get(ii, jj);
        }
    }

    private class SindicesFunction extends SubStrFunction {

        public SindicesFunction() {
            super("si");
        }

        @Override
        public Object evaluate(MomStructure str, OpListComma.Uplet params, Map<String, Object> variables) {
            int ii = 0;
            int jj = 0;
            if (params.size() >= 3) {
                ii = ((Number) params.get(1)).intValue() - 1;
                jj = ((Number) params.get(2)).intValue() - 1;
            }
            return str.sparameters().computeMatrix().get(ii, jj);
        }
    }

    public double evaluateDimension(String expression) {
        return evaluateComplex(expression, VarUnit.LEN).getReal();
    }

    public double evaluateFrequency(String expression) {
        return evaluateComplex(expression, VarUnit.FREQ).getReal();
    }

    public double getDimensionUnit() {
        return dimensionUnit;
    }

    public void setDimensionUnit(double dimensionUnit) {
        this.dimensionUnit = dimensionUnit;
    }

    public double getFrequencyUnit() {
        return frequencyUnit;
    }

    public void setFrequencyUnit(double frequencyUnit) {
        this.frequencyUnit = frequencyUnit;
    }

}
