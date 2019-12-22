package net.vpc.scholar.hadrumaths.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
public @interface DoubleValidator {
    boolean NaN() default true;

    boolean negativeInf() default true;

    boolean positiveInf() default true;
}
