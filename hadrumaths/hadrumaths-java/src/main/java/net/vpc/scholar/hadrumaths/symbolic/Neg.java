/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.FunctionFactory;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransform;
import net.vpc.scholar.hadrumaths.ExpressionTransformFactory;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransformer;

/**
 *
 * @author vpc
 */
public class Neg extends AbstractUnaryExpOperator implements Cloneable {

    static {
        ExpressionTransformFactory.setExpressionTransformer(Neg.class, ExpressionTransform.class, new ExpressionTransformer() {

            public Expr transform(Expr expression, ExpressionTransform transform) {
                Neg e = (Neg) expression;
                return new Neg(ExpressionTransformFactory.transform(e.getExpression(), transform));
            }
        });
    }


    public Neg(Expr expression) {
        super(expression);
    }

    @Override
    protected Neg newInstance(Expr e) {
        return new Neg(e);
    }


    @Override
    protected Expressions.UnaryExprHelper<? extends AbstractUnaryExpOperator> getExprHelper() {
        return exprHelper;
    }

    private static Expressions.UnaryExprHelper<Neg> exprHelper = new NegUnaryExprHelper();

    private static class NegUnaryExprHelper implements Expressions.UnaryExprHelper<Neg> , Serializable{

        @Override
        public Expr getBaseExpr(Neg expr) {
            return expr.getExpression();
        }

        @Override
        public double computeDouble(double x) {
            return - x;
        }

        @Override
        public Complex computeComplex(Complex x) {
            return x.neg();
        }

        @Override
        public Matrix computeMatrix(Matrix x) {
            return x.neg();
        }
    }
}
