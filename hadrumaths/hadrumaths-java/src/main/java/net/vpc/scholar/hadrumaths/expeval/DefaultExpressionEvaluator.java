/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.expeval;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

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
public class DefaultExpressionEvaluator extends AbstractExpressionEvaluator {



    /***
     * creates an empty MathEvaluator. You need to use setExpression(String s) to assign a math expression string to it.
     */
    public DefaultExpressionEvaluator() {
    }


    public Object readL0(ExpressionStreamTokenizer tok) throws IOException {
        ExpressionStreamTokenizer.Token token = tok.nextToken();
        if (getContext().isOperator(token.image)) {
            //unary
            char op = token.image.charAt(0);
            switch (op) {
                case '!':
                case '~':
                case '$':
                case '-':
                case '&': {
                    Object o = readL1(tok, Long.MAX_VALUE,false);
                    Object[] args = {o};
                    Class[] argTypes = {resolveType(o)};
                    return new ExprInvocation(getContext().getFunctionNode(token.image, argTypes),args);
                }
                default: {
                    tok.pushBack(token);
                    throw new MissingTokenException();
                }
            }
        }
        switch (token.ttype) {
            case ExpressionStreamTokenizer.TT_WORD: {
                ExpressionStreamTokenizer.Token peek = tok.peek();
                if (peek.ttype == '(') {
                    //this is a function
                    Object[] r = readUplet(tok);
                    Class[] types = new Class[r.length];
                    for (int i = 0; i < types.length; i++) {
                        types[i] = resolveType(r[i]);
                    }
                    ExpressionNode function = getContext().getFunctionNode(token.image, types);
                    return new ExprInvocation(function,r);
                } else {
                    String varName = token.image;
                    //getContext().getVariableValue(token.image)
                    return new ExprInvocation(getContext().getVariableNode(varName), new Object[0]);
                }
            }
            case '(': {
                Object o = readL1(tok, -1, true);
                readOp(tok, ')', ")");
                return o;
            }
            case ExpressionStreamTokenizer.TT_STRING: {
                return token.sval;
            }
            case ExpressionStreamTokenizer.TT_NUMBER: {
                return token.nval;
            }
            case ExpressionStreamTokenizer.TT_COMPLEX: {
                return token.cval;
            }
            default: {
                tok.pushBack(token);
                throw new MissingTokenException();
            }
        }
    }

    public Object readL1(ExpressionStreamTokenizer tok, long opPrecedence, boolean openPar) throws IOException {
        Object o1 = readL0(tok);
        while (true) {
            ExpressionStreamTokenizer.Token token = tok.nextToken();
            if (token.isEOF()) {
                if (openPar) {
                    throw new MissingTokenException("Missing )");
                }
                return o1;
            }
            if (token.ttype == ')') {
                if(!openPar){
                    System.out.print("");//throw new IllegalArgumentException("Why");
                }
                tok.pushBack(token);
                return o1;
            }
            if (token.ttype == ',') {
                tok.pushBack(token);
                return o1;
            }
            if (getContext().isOperator(token.image)) {
                long binaryOpPrecedence = getContext().getBinaryOpPrecedence(token.image);
                if (binaryOpPrecedence > opPrecedence) {
                    // a binary ?
                    Object o2 = readL1(tok, binaryOpPrecedence, openPar);
                    Object[] args = {o1, o2};
                    Class[] argTypes = {resolveType(o1), resolveType(o2)};
                    o1 = new ExprInvocation(
                            getContext().getFunctionNode(token.image, argTypes),
                            args
                    );
                } else {
                    tok.pushBack(token);
                    return o1;
                }
            } else {
                throw new MissingTokenException("Unexpected token " + token);
            }
        }
    }

    private static Class resolveType(Object o) {
        if (o == null) {
            throw new IllegalArgumentException("unsupported null type");
        }
        if (o instanceof ExprInvocation) {
            Class returnType = ((ExprInvocation) o).getReturnType();
            return returnType;
        }
        Class<?> aClass = o.getClass();
        return aClass;
    }

    public Object readAny(ExpressionStreamTokenizer tok) throws IOException {
        return readL1(tok, -1, false);
    }

    private ExpressionStreamTokenizer.Token readOp(ExpressionStreamTokenizer tok, int ttype, String sval) throws IOException {
        ExpressionStreamTokenizer.Token token = tok.nextToken();
        if (token.ttype == ttype && token.image.equals(sval)) {
            return token;
        }
        throw new IllegalArgumentException("Expected " + sval);
    }

    private Object[] readUplet(ExpressionStreamTokenizer tok) throws IOException {
        List<Object> all = new ArrayList<>();
        readOp(tok, '(', "(");
        ExpressionStreamTokenizer.Token peek = tok.peek();
        if (peek.ttype == ')') {
            readOp(tok, ')', ")");
        } else {
            all.add(readL1(tok, -1, true));
            while (true) {
                peek = tok.peek();
                if (peek.ttype == ')') {
                    readOp(tok, ')', ")");
                    break;
                } else {
                    readOp(tok, ',', ",");
                    all.add(readL1(tok, -1, true));
                }
            }
        }
        return all.toArray(new Object[all.size()]);

    }

    public Object evaluate(String expression) {
        try {

            ExpressionStreamTokenizer tok = new ExpressionStreamTokenizer(new StringReader(expression));
            Object o = readAny(tok);
            if (o instanceof ExprInvocation) {
                return ((ExprInvocation) o).eval(getContext());
            }
            return o;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


}
