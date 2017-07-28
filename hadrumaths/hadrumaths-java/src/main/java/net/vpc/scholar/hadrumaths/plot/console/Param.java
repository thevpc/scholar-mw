package net.vpc.scholar.hadrumaths.plot.console;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 24 janv. 2007 12:20:46
 */
@Target({PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Param{
    String value();
}
