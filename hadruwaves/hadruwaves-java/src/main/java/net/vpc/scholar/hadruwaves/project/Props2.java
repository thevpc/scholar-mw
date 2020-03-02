package net.vpc.scholar.hadruwaves.project;

import net.vpc.common.prpbind.PropertyType;
import net.vpc.common.prpbind.Props;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadruwaves.props.PExpression;
import net.vpc.scholar.hadruwaves.props.WritablePExpression;
import net.vpc.scholar.hadruwaves.props.WritablePExpressionImpl;

public class Props2 {
    public static PropsBuilder2 of(String name) {
        return new PropsBuilder2(name);
    }

    public static class PropsBuilder2 extends Props.PropsBuilder {
        public PropsBuilder2(String name) {
            super(name);
        }

        public WritablePExpression<Double> doubleOf(Double defaultValue) {
            WritablePExpressionImpl<Double> p = new WritablePExpressionImpl<>(name, PropertyType.of(Double.class), defaultValue);
            prepare(p);
            return p;
        }

        public WritablePExpression<Complex> complexOf(Complex defaultValue) {
            WritablePExpressionImpl<Complex> p = new WritablePExpressionImpl<>(name, PropertyType.of(Complex.class), defaultValue);
            prepare(p);
            return p;
        }

        public WritablePExpression<Expr> exprOf(Expr defaultValue) {
            WritablePExpressionImpl<Expr> p = new WritablePExpressionImpl<>(name, PropertyType.of(Expr.class), defaultValue);
            prepare(p);
            return p;
        }
    }
}
