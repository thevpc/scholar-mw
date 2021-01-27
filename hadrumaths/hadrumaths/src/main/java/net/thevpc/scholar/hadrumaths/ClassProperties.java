package net.thevpc.scholar.hadrumaths;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ClassProperties {
    ClassProperty[] value();
}
