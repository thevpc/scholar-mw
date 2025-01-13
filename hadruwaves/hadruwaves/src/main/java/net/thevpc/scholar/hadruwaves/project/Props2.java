package net.thevpc.scholar.hadruwaves.project;

import net.thevpc.common.props.PropertyType;
import net.thevpc.common.props.Props;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.units.UnitType;
import net.thevpc.scholar.hadruwaves.props.WritablePExpression;
import net.thevpc.scholar.hadruwaves.props.WritablePExpressionImpl;

public class Props2 {

    public static PropsBuilder2 of(String name) {
        return new PropsBuilder2(name);
    }

    public static class PropsBuilder2 extends Props.PropsBuilder {

        public PropsBuilder2(String name) {
            super(name);
        }

        public WritablePExpression<Double> exprFreqOf(Double defaultValue) {
            WritablePExpressionImpl<Double> p = new WritablePExpressionImpl<>(name, PropertyType.of(Double.class), defaultValue, UnitType.Frequency);
            prepare(p);
            return p;
        }

        public WritablePExpression<Double> exprLenOf(Double defaultValue) {
            WritablePExpressionImpl<Double> p = new WritablePExpressionImpl<>(name, PropertyType.of(Double.class), defaultValue, UnitType.Length);
            prepare(p);
            return p;
        }

        public WritablePExpression<Double> exprDoubleOf(Double defaultValue) {
            WritablePExpressionImpl<Double> p = new WritablePExpressionImpl<>(name, PropertyType.of(Double.class), defaultValue, UnitType.Double);
            prepare(p);
            return p;
        }

        public WritablePExpression<Integer> exprIntOf(Integer defaultValue) {
            WritablePExpressionImpl<Integer> p = new WritablePExpressionImpl<>(name, PropertyType.of(Integer.class), defaultValue, UnitType.Integer);
            prepare(p);
            return p;
        }

        public WritablePExpression<Complex> exprComplexOf(Complex defaultValue) {
            WritablePExpressionImpl<Complex> p = new WritablePExpressionImpl<>(name, PropertyType.of(Complex.class), defaultValue, UnitType.Complex);
            prepare(p);
            return p;
        }

        public WritablePExpression<Expr> exprOf(Expr defaultValue) {
            WritablePExpressionImpl<Expr> p = new WritablePExpressionImpl<>(name, PropertyType.of(Expr.class), defaultValue, UnitType.Expression);
            prepare(p);
            return p;
        }

        public WritablePExpression<Boolean> exprBooleanOf(boolean defaultValue) {
            WritablePExpressionImpl<Boolean> p = new WritablePExpressionImpl<>(name, PropertyType.of(Boolean.class), defaultValue, UnitType.Boolean);
            prepare(p);
            return p;
        }

        public <T extends Enum> WritablePExpression<T> exprEnumOf(Class<T> enumType, T defaultValue) {
            WritablePExpressionImpl<T> p = new WritablePExpressionImpl<T>(name, PropertyType.of(enumType), defaultValue, UnitType.forEnum(enumType));
            prepare(p);
            return p;
        }
    }
}
