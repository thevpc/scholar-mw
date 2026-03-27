package net.thevpc.scholar.hadrumaths.plot.random;

import net.thevpc.common.classpath.ClassPathUtils;
import net.thevpc.nuts.time.NChronometer;

public class ExprProjectClasses {
    private static Class[] EXPR_CLASSES = null;

    public static Class[] getProjectClasses() {
        if (EXPR_CLASSES == null) {
            System.err.println("Loading project classes...");
            NChronometer c = NChronometer.startNow();
            EXPR_CLASSES = ClassPathUtils.resolveContextClassesList(false).stream()
                    .filter(x->x.getName().startsWith("net.thevpc.scholar."))
                    .toArray(Class[]::new);
            System.err.println("Loaded " + EXPR_CLASSES.length + " classes in " + c.stop() + ".");
        }
        return EXPR_CLASSES;
    }


}
