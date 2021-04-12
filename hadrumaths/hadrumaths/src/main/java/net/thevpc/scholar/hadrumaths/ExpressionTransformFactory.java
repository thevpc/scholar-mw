/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths;

import net.thevpc.common.collections.ClassMap;
import net.thevpc.scholar.hadrumaths.transform.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author vpc
 */
public class ExpressionTransformFactory extends AbstractFactory {

    private static final ClassMap<Map<Class, ExpressionTransformer>> expressionToExpressionTransformers = new ClassMap<Map<Class, ExpressionTransformer>>(Expr.class, (Class) Map.class);

    public static void setExpressionTransformer(Class<? extends Expr> expressionClass, Class<? extends ExpressionTransform> transformerClass, ExpressionTransformer expressionTransformer) {
        if (expressionTransformer == null) {
            getExpressionTransformerMap(expressionClass).remove(transformerClass);
        } else {
            getExpressionTransformerMap(expressionClass).put(transformerClass, expressionTransformer);
        }
    }

    private static Map<Class, ExpressionTransformer> getExpressionTransformerMap(Class<? extends Expr> expressionClass) {
        Map<Class, ExpressionTransformer> t = expressionToExpressionTransformers.getExact(expressionClass);
        if (t == null) {
            t = new HashMap<Class, ExpressionTransformer>();
            expressionToExpressionTransformers.put(expressionClass, t);
        }
        return t;
    }

    public static <T extends Expr> T transform(Expr e, ExpressionTransform transform) {
        return (T) getExpressionTransformer(e.getClass(), transform.getClass()).transform(e, transform);
    }

    public static ExpressionTransformer getExpressionTransformer(Class<? extends Expr> expressionClass, Class<? extends ExpressionTransform> transformerClass) {
        for (Map<Class, ExpressionTransformer> t2 : expressionToExpressionTransformers.getAll(expressionClass)) {
            ExpressionTransformer found = t2.get(transformerClass);
            if (found != null) {
                return found;
            }
        }
        throw new NoSuchElementException("Missing " + transformerClass + " for " + expressionClass);
    }

    public static DoubleMultiplierTransform doubleMul(double d) {
        return new DoubleMultiplierTransform(d);
    }

    public static DomainXYMultiplierTransform domainMul(Domain d) {
        return new DomainXYMultiplierTransform(d);
    }

    public static OppositeTransform opposite(Axis axis) {
        return new OppositeTransform(axis);
    }

    public static SymmetryTransform symmetric(Axis axis, Domain domainXY) {
        return new SymmetryTransform(axis, domainXY);
    }

    public static TranslateTransform translate(double valueX, double valueY) {
        return new TranslateTransform(valueX, valueY);
    }
}
