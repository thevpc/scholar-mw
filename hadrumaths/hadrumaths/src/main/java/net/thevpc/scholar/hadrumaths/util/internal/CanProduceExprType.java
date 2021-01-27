package net.thevpc.scholar.hadrumaths.util.internal;

import net.thevpc.scholar.hadrumaths.symbolic.ExprType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CanProduceExprType {
    ExprType[] value();
}
