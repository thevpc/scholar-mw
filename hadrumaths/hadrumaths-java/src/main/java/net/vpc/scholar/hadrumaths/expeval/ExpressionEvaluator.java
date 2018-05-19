/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.expeval;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.expeval.functions.*;
import net.vpc.scholar.hadrumaths.expeval.operators.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * <i>Mathematic expression evaluator.</i> Supports the following functions:
 * +, -, *, /, ^, %, cos, sin, tan, acos, asin, atan, sqrt, sqr, log, min, max, ceil, floor, absdbl, neg, rndr.<br>
 * <pre>
 * Sample:
 * MathEvaluator m = new MathEvaluator();
 * m.declare("x", 15.1d);
 * System.out.println( m.evaluate("-5-6/(-2) + sqr(15+x)") );
 * </pre>
 *
 * @author Taha BEN SALAH
 * @version 1.0
 * @date April 2008
 */
public class ExpressionEvaluator {

    /***
     * Main. To run the program in command line.
     * Usage: java MathEvaluator.main [your math expression]
     */
    public static void main(String[] args) {
        try {
            ExpressionEvaluator m = new ExpressionEvaluator();
            m.addDefaults();
            m.declare("x", 4);
            m.declare("y", 4);
            //System.out.println(m.evaluate("-x-y+4i+(-x)"));
            System.out.println(m.evaluate("-4i+1/4i"));
            //System.out.println(m.evaluate("(1+2()*3"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> variables = new HashMap<String, Object>();
    private Map<String, ExpressionNode> functions = new HashMap<String, ExpressionNode>();

    /***
     * creates an empty MathEvaluator. You need to use setExpression(String s) to assign a math expression string to it.
     */
    public ExpressionEvaluator() {
    }

    public void addDefaults() {
        declare("E", Maths.E);
        declare("e", Maths.E);
        declare("PI", Maths.PI);
        declare("pi", Maths.PI);
        declare("infinity", Double.POSITIVE_INFINITY);
        declare("NaN", Complex.NaN);
        declare(new OpPlus());
        declare(new OpDivide());
        declare(new OpMinus());
        declare(new OpMultiply());
        declare(new OpMinusUnary());
        declare(new FunctionCos());
        declare(new FunctionSin());
        declare(new FunctionReal());
        declare(new FunctionImag());
        declare(new FunctionToInt());
    }

    public void declare(String name, Number value) {
        variables.put(name, (value instanceof Complex) ? (Complex) value : Complex.valueOf(value.doubleValue()));
    }

    public void declare(String name, Complex value) {
        variables.put(name, value);
    }

    public void declare(String name, String value) {
        variables.put(name, value);
    }

    public void declare(ExpressionNode function) {
        functions.put(function.getName(), function);
    }

    public void undeclare(String name, Number value) {
        variables.remove(name);
    }

    public Object evaluate(String expression) {
        try {

            ExpressionStreamTokenizer tok = new ExpressionStreamTokenizer(new StringReader(expression));

            Stack<String> opStack = new Stack<String>();
            Stack<Object> valStack = new Stack<Object>();
            int old = -1;
            while (tok.nextToken() != ExpressionStreamTokenizer.TT_EOF) {
                switch (tok.ttype) {
                    case ExpressionStreamTokenizer.TT_NUMBER: {
                        if (old == ExpressionStreamTokenizer.TT_WORD) {
                            String s = (String) valStack.pop();
                            valStack.push(getVariableValue(s));
                        }
                        valStack.push(Complex.valueOf(tok.nval));
                        break;
                    }
                    case ExpressionStreamTokenizer.TT_COMPLEX: {
                        if (old == ExpressionStreamTokenizer.TT_WORD) {
                            String s = (String) valStack.pop();
                            valStack.push(getVariableValue(s));
                        }
                        valStack.push(tok.cval);
                        break;
                    }
                    case ExpressionStreamTokenizer.TT_STRING: {
                        if (old == ExpressionStreamTokenizer.TT_WORD) {
                            String s = (String) valStack.pop();
                            valStack.push(getVariableValue(s));
                        }
                        valStack.push(tok.sval);
                        break;
                    }
                    case ExpressionStreamTokenizer.TT_WORD: {
                        if (old == ExpressionStreamTokenizer.TT_WORD) {
                            String s = (String) valStack.pop();
                            valStack.push(getVariableValue(s));
                        }
                        valStack.push(tok.sval);
                        break;
                    }
                    case '(': {
                        String op = "(";
                        if (old == ExpressionStreamTokenizer.TT_WORD) {
                            Object oo = valStack.peek();
                            if (oo instanceof String) {
                                valStack.pop();
                                op = oo.toString();
                            }
                        }
                        opStack.push(op);
                        break;
                    }
                    case ')': {
                        if (old == ExpressionStreamTokenizer.TT_WORD) {
                            String s = (String) valStack.pop();
                            valStack.push(getVariableValue(s));
                        }
                        while (!opStack.empty()) {
                            String op = opStack.pop();
                            int x = computeOp(op);
                            if (!op.equals("(")) {
                                Object f = getFunction(op).evaluate(valStack, variables);
                                valStack.push(f);
                            }
                            if (x == 0) {
                                break;
                            }
                        }
                        break;
                    }
                    case '+':
                    case '-': {
                        String meOp = String.valueOf((char) tok.ttype);
                        if (old == ExpressionStreamTokenizer.TT_WORD) {
                            String s = (String) valStack.pop();
                            valStack.push(getVariableValue(s));
                        } else if (old != ')' && old != ExpressionStreamTokenizer.TT_WORD && old != ExpressionStreamTokenizer.TT_NUMBER && old != ExpressionStreamTokenizer.TT_COMPLEX) {
                            meOp = meOp + "U";
                        }
                        int me = computeOp(meOp);
                        while (!opStack.empty()) {
                            String op = opStack.peek();
                            int x = computeOp(op);
                            if (x >= me) {
                                opStack.pop();
                                try {
                                    Object f = getFunction(op).evaluate(valStack, variables);
                                    valStack.push(f);
                                } catch (Exception e) {
                                    throw new ExpressionEvaluatorException("Error while evaluating " + op, e);
                                }
                            } else {
                                break;
                            }
                        }
                        opStack.push(meOp);
                        break;
                    }
                    case '*':
                    case '/':
                    case '^':
                    case ',': {
                        if (old == ExpressionStreamTokenizer.TT_WORD) {
                            String s = (String) valStack.pop();
                            valStack.push(getVariableValue(s));
                        }
                        String meOp = String.valueOf((char) tok.ttype);
                        int me = computeOp(meOp);
                        while (!opStack.empty()) {
                            String op = opStack.peek();
                            int x = computeOp(op);
                            if (x >= me) {
                                opStack.pop();
                                try {
                                    Object f = getFunction(op).evaluate(valStack, variables);
                                    valStack.push(f);
                                } catch (Exception e) {
                                    throw new ExpressionEvaluatorException("Error while evaluating " + op, e);
                                }
                            } else {
                                break;
                            }
                        }
                        opStack.push(meOp);
                        break;
                    }
                    default: {
                        if (old == ExpressionStreamTokenizer.TT_WORD) {
                            String s = (String) valStack.pop();
                            valStack.push(getVariableValue(s));
                        }
//                        System.out.println("ExpressionEvaluator.evaluate : default");
                    }
                }
                old = tok.ttype;
            }

            if (old == ExpressionStreamTokenizer.TT_WORD) {
                String s = (String) valStack.pop();
                valStack.push(getVariableValue(s));
            }
            while (!opStack.empty()) {
                String op = opStack.pop();
                try {
                    Object f = getFunction(op).evaluate(valStack, variables);
                    valStack.push(f);
                } catch (Exception e) {
                    throw new ExpressionEvaluatorException("Error while evaluating " + op, e);
                }
            }
            if (valStack.size() != 1) {
                throw new RuntimeException("incorrect syntax : stack not empty");
            }
            return valStack.pop();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public ExpressionNode getFunction(String op) {
        ExpressionNode ff = functions.get(op);
        if (ff == null) {
            throw new NoSuchFunctionException(op);
        }
        return ff;
    }

    public Object getVariableValue(String var) {
        Object ff = variables.get(var);
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
}
