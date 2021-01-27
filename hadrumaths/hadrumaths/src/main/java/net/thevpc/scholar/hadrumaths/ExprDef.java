package net.thevpc.scholar.hadrumaths;

public enum ExprDef {
    DOUBLE_SCALAR(ExprNumberType.DOUBLE_TYPE, ExprDim.SCALAR),
    DOUBLE_VECTOR(ExprNumberType.DOUBLE_TYPE, ExprDim.VECTOR),
    DOUBLE_MATRIX(ExprNumberType.DOUBLE_TYPE, ExprDim.MATRIX),

    COMPLEX_SCALAR(ExprNumberType.COMPLEX_TYPE, ExprDim.SCALAR),
    COMPLEX_VECTOR(ExprNumberType.COMPLEX_TYPE, ExprDim.VECTOR),
    COMPLEX_MATRIX(ExprNumberType.COMPLEX_TYPE, ExprDim.MATRIX);

    private final ExprNumberType type;
    private final ExprDim dimension;

    ExprDef(ExprNumberType type, ExprDim dimension) {
        this.type = type;
        this.dimension = dimension;
    }

    public ExprNumberType nbr() {
        return type;
    }

    public ExprDim dim() {
        return dimension;
    }
}
