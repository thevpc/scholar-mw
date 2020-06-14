package net.vpc.scholar.hadrumaths.transform;

/**
 * implementations MUST be stateless!!
 */
public abstract class AbstractExpressionRewriterRule implements ExpressionRewriterRule {
    @Override
    public final int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        return obj != null && getClass().getName().equals(obj.getClass().getName());
    }

    @Override
    public final String toString() {
        return getClass().getSimpleName();
    }
}
