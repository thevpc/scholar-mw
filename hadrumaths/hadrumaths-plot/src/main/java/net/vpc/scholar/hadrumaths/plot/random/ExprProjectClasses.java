package net.vpc.scholar.hadrumaths.plot.random;

import net.vpc.common.classpath.ClassPathUtils;
import net.vpc.common.util.Chronometer;

public class ExprProjectClasses {
    private static Class[] EXPR_CLASSES = null;

    public static Class[] getProjectClasses() {
        if (EXPR_CLASSES == null) {
            System.err.println("Loading project classes...");
            Chronometer c = Chronometer.start();
            EXPR_CLASSES = ClassPathUtils.resolveContextClassesList(false).stream()
                    .filter(x->x.getName().startsWith("net.vpc.scholar."))
                    .toArray(Class[]::new);
            System.err.println("Loaded " + EXPR_CLASSES.length + " classes in " + c.stop() + ".");
        }
        return EXPR_CLASSES;
    }


}
