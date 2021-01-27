package net.thevpc.scholar.hadrumaths;

import net.thevpc.scholar.hadrumaths.symbolic.ExprType;

public class SimplifyOptions implements Cloneable {
    public static SimplifyOptions DEFAULT = new ReadOnlySimplifyOptions();
    private boolean preserveRootName;
    private ExprType targetExprType;

    public SimplifyOptions() {

    }

    public SimplifyOptions(SimplifyOptions other) {
        this.preserveRootName = other.isPreserveRootName();
        this.targetExprType = other.getTargetExprType();
    }

    public boolean isPreserveRootName() {
        return preserveRootName;
    }

    public SimplifyOptions setPreserveRootName(boolean preserveRootName) {
        this.preserveRootName = preserveRootName;
        return this;
    }

    public ExprType getTargetExprType() {
        return targetExprType;
    }

    public SimplifyOptions setTargetExprType(ExprType targetExprType) {
        this.targetExprType = targetExprType;
        return this;
    }

    public SimplifyOptions copy() {
        return new SimplifyOptions(this);
    }

    public SimplifyOptions lock() {
        return new ReadOnlySimplifyOptions(this);
    }

    public final static class ReadOnlySimplifyOptions extends SimplifyOptions {
        public ReadOnlySimplifyOptions(SimplifyOptions other) {
            super.setPreserveRootName(other.isPreserveRootName());
        }

        public ReadOnlySimplifyOptions() {
        }

        @Override
        public SimplifyOptions setPreserveRootName(boolean preserveRootName) {
            throw new IllegalArgumentException("Read Only SimplifyOptions");
        }

        @Override
        public SimplifyOptions setTargetExprType(ExprType targetExprType) {
            throw new IllegalArgumentException("Read Only SimplifyOptions");
        }
    }
}
