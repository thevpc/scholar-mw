package net.vpc.scholar.hadrumaths.transform;

public enum RewriteResultType {
    UNMODIFIED,
    BEST_EFFORT,
    NEW_VAL;

    public RewriteResultType max(RewriteResultType other) {
        if (other.ordinal() > ordinal()) {
            return other;
        }
        return this;
    }

    public RewriteResultType min(RewriteResultType other) {
        if (other.ordinal() < ordinal()) {
            return other;
        }
        return this;
    }
}
