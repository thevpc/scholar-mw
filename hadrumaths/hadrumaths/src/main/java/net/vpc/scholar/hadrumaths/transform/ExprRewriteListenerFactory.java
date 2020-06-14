package net.vpc.scholar.hadrumaths.transform;

import java.io.PrintStream;

public final class ExprRewriteListenerFactory {
    private ExprRewriteListenerFactory() {
    }

    public static ExprRewriteListener logger() {
        return ExprRewriteLogger.INSTANCE;
    }

    public static ExprRewriteListener logger(PrintStream out) {
        return out == null ? ExprRewriteLogger.INSTANCE : new ExprRewriteLogger(out);
    }

    public static ExprRewriteCounter counter() {
        return new ExprRewriteCounter();
    }
}
