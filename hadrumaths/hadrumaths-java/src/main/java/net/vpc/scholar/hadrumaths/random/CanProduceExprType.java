package net.vpc.scholar.hadrumaths.random;

import net.vpc.scholar.hadrumaths.symbolic.ExprType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CanProduceExprType {
    ExprType[] value();
}
