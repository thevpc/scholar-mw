package net.vpc.scholar.hadrumaths.expeval;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.DoubleParam;

import java.util.*;

public class ExpressionEvaluatorContext {
    private Map<String, ExprVar> variables = new HashMap<String, ExprVar>();
    private Set<String> supportedOperators = new HashSet<>();
    private Map<String, ExpressionNode> functions = new HashMap<String, ExpressionNode>();
    private ExpressionEvaluatorResolver resolver = new UtilClassExpressionEvaluatorResolver(false,Maths.class, PlatformHelper.class);

    public void addDefaults() {

//        declareVar("E", Double.class, Maths.E);
//        declareVar("e", Double.class, Maths.E);
//        declareVar("PI", Double.class, Maths.PI);
//        declareVar("pi", Double.class, Maths.PI);
//        declareVar("infinity", Double.class, Double.POSITIVE_INFINITY);
//        declareVar("NaN", Complex.class, Complex.NaN);
//        declareOp("+");
//        declareOp("-");
//        declareOp("*");
//        declareOp("/");
//        declare(new OpPlus());
//        declare(new OpDivide());
//        declare(new OpMinus());
//        declare(new OpMultiply());
//        declare(new OpMinusUnary());
//        declare(new FunctionCos());
//        declare(new FunctionSin());
//        declare(new FunctionReal());
//        declare(new FunctionImag());
//        declare(new FunctionToInt());
    }

    public void declareOp(String name) {
        supportedOperators.add(name);
    }

    public ExprVar declareVar(String name, Class type, Object value) {
        ExprVar v = declareVar(name, type);
        v.setValue(value);
        return v;
    }

    public ExprVar declareVar(String name, Class type) {
        ExprVar value = new ExprVar(name, type);
        variables.put(name, value);
        return value;
    }

    public void declareFunction(ExpressionFunction function) {
        functions.put(function.getName(), function);
    }

    public void undeclareVar(String name) {
        variables.remove(name);
    }

    public void undeclareFunctino(String name) {
        functions.remove(name);
    }

    public boolean isOperator(String name) {
        for (char c : name.toCharArray()) {
            if (getBinaryOpPrecedence(c) < 0) {
                return false;
            }
        }
        return true;
    }

    //public static final String OPERATORS = "-+$=<>#~%~/*|&!^";

    public int getBinaryOpPrecedence(char c) {
        switch (c) {
            case '=':
            case '<':
            case '>':
            case '#':
            case '!':
                return ExpressionNode.PRECEDENCE_1;
            case '&':
            case '|':
            case '~':
                return ExpressionNode.PRECEDENCE_2;
            case '+':
            case '-':
                return ExpressionNode.PRECEDENCE_3;
            case '*':
            case '/':
                return ExpressionNode.PRECEDENCE_4;
            case '^':
            case '@':
            case '$':
            case ':':
                return ExpressionNode.PRECEDENCE_5;
        }
        return -1;
    }

    public long getBinaryOpPrecedence(String name) {
        long p = 0;
        for (char c : name.toCharArray()) {
            p = p * ExpressionNode.PRECEDENCE_MAX + getBinaryOpPrecedence(c);
        }
        return p;
    }

    public ExpressionNode getFunctionNode(String op, Class[] args) {
        ExpressionNode n = functions.get(op);
        if (n != null) {
            return n;
        }
        if (resolver != null) {
            ExpressionNode resolved = resolver.resolveFunction(op, args);
            if (resolved != null) {
                return resolved;
            }
        }
        if (supportedOperators.contains(op)) {
            throw new NoSuchElementException("Operator " + op + " not found for " + Arrays.toString(args));
        }
        throw new NoSuchElementException("Function " + op + " not found");
    }

    public boolean isDeclaredOp(String op) {
        return supportedOperators.contains(op);
    }

    public ExpressionNode getFunctionNode(String op) {
        ExpressionNode ff = functions.get(op);
        if (ff == null) {
            throw new NoSuchFunctionException(op);
        }
        return ff;
    }

    public Object getVariableValue(String var) {
        return getVariable(var).getValue();
    }

    public ExpressionNode getVariableNode(String var) {
        ExprVar ff = variables.get(var);
        if (ff != null) {
            if(ff.isUndefinedValue()){
                if(ff.getType().equals(Double.class)){
                    return new VarExpressionNode(var, DoubleParam.class);
                }
                return new VarExpressionNode(var, Expr.class);
            }else {
                return new VarExpressionNode(var, ff.getType());
            }
        }
        if(resolver!=null){
            ExpressionNode expressionNode = resolver.resolveVar(var, false);
            if(expressionNode!=null) {
                return expressionNode;
            }
        }
        throw new NoSuchVariableException(var);
    }

    public ExprVar getVariable(String var) {
        ExprVar ff = variables.get(var);
        if (ff == null) {
            throw new NoSuchVariableException(var);
        }
        return ff;
    }

    public int computeOp(String op) {
        switch (op.charAt(0)) {
            case '+':
            case '-': {
                return (op.equals("-U")) ? 40 : 10;
            }
            case '*':
            case '/': {
                return 20;
            }
            case '^': {
                return 30;
            }
            case '(': {
                return 0;
            }
            case ',': {
                return 7;
            }
        }
        return 0;
    }

    public static class ExprVar {
        private String name;
        private Class type;
        private Object value;
        private boolean defined=false;

        public ExprVar(String name, Class type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public Class getType() {
            return type;
        }

        public Object getValue() {
            return value;
        }

        public ExprVar setValue(Object value) {
            this.value = value;
            defined = true;
            return this;
        }

        public boolean isDefinedValue() {
            return defined;
        }

        public boolean isUndefinedValue() {
            return !defined;
        }
        public ExprVar setUndefinedValue() {
            this.value = null;
            defined = false;
            return this;
        }
    }

    public ExpressionEvaluatorResolver getResolver() {
        return resolver;
    }

    public ExpressionEvaluatorContext setResolver(ExpressionEvaluatorResolver resolver) {
        this.resolver = resolver;
        return this;
    }
}
