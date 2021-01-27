package net.thevpc.scholar.hadrumaths.util.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
public @interface IntValidator {
    int min() default Integer.MIN_VALUE;

    int max() default Integer.MAX_VALUE;
}
