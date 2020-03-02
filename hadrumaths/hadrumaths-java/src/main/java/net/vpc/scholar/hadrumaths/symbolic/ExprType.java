package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.ExprDef;
import net.vpc.scholar.hadrumaths.ExprDim;
import net.vpc.scholar.hadrumaths.ExprNumberType;

public enum ExprType {
    DOUBLE_NBR(true, ExprDef.DOUBLE_SCALAR, ExprDef.DOUBLE_SCALAR),
    COMPLEX_NBR(true, ExprDef.COMPLEX_SCALAR, ExprDef.COMPLEX_SCALAR),
    CVECTOR_NBR(true, ExprDef.COMPLEX_VECTOR, ExprDef.COMPLEX_VECTOR),
    CMATRIX_NBR(true, ExprDef.COMPLEX_MATRIX, ExprDef.COMPLEX_MATRIX),
    DOUBLE_EXPR(true, ExprDef.DOUBLE_SCALAR, ExprDef.DOUBLE_SCALAR),
    COMPLEX_EXPR(true, ExprDef.COMPLEX_SCALAR, ExprDef.COMPLEX_SCALAR),
    CVECTOR_EXPR(true, ExprDef.COMPLEX_VECTOR, ExprDef.COMPLEX_VECTOR),
    CMATRIX_EXPR(true, ExprDef.COMPLEX_MATRIX, ExprDef.COMPLEX_MATRIX),
    /**
     * double to scalar double
     */
    DOUBLE_DOUBLE(false, ExprDef.DOUBLE_SCALAR, ExprDef.DOUBLE_SCALAR),

    /**
     * double to scalar complex
     */
    DOUBLE_COMPLEX(false, ExprDef.DOUBLE_SCALAR, ExprDef.COMPLEX_SCALAR),

    /**
     * double to vector complex
     */
    DOUBLE_CVECTOR(false, ExprDef.DOUBLE_SCALAR, ExprDef.COMPLEX_VECTOR),

    /**
     * double to matrix complex
     */
    DOUBLE_CMATRIX(false, ExprDef.DOUBLE_SCALAR, ExprDef.COMPLEX_MATRIX)//,

//    COMPLEX_COMPLEX(false, ExprDef.COMPLEX_SCALAR, ExprDef.COMPLEX_SCALAR),
//
//    COMPLEX_CVECTOR(false, ExprDef.COMPLEX_SCALAR, ExprDef.COMPLEX_VECTOR),
//
//    COMPLEX_CMATRIX(false, ExprDef.COMPLEX_SCALAR, ExprDef.COMPLEX_MATRIX)
    ;
    private final boolean constant;
    private final ExprDef in;
    private final ExprDef out;

    ExprType(boolean constant,
             ExprDef in, ExprDef out) {
        this.constant = constant;
        this.in = in;
        this.out = out;
    }

    public boolean isConstant() {
        return constant;
    }

    public boolean isDoubleInput() {
        return in().nbr() == ExprNumberType.DOUBLE_TYPE;
    }

    public ExprDef in() {
        return in;
    }

    public boolean isComplexInput() {
        return in().nbr() == ExprNumberType.COMPLEX_TYPE;
    }

    public boolean isDoubleOutput() {
        return out().nbr() == ExprNumberType.DOUBLE_TYPE;
    }

    public ExprDef out() {
        return out;
    }

    public boolean isComplexOutput() {
        return out().nbr() == ExprNumberType.COMPLEX_TYPE;
    }

    public boolean isVectorOutput() {
        return out().dim() == ExprDim.VECTOR;
    }

    public boolean isMatrixOutput() {
        return out().dim() == ExprDim.MATRIX;
    }

    public boolean isScalarOutput() {
        return out().dim() == ExprDim.SCALAR;
    }

    public boolean isScalarInput() {
        return in().dim() == ExprDim.SCALAR;
    }
}
