package net.vpc.scholar.hadrumaths;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ClassProperties.class)
public @interface ClassProperty {
    String name();

    String value() default "";
}
