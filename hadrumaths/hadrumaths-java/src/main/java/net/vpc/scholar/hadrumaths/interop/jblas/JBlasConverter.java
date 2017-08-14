package net.vpc.scholar.hadrumaths.interop.jblas;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.TMatrix;
import org.jblas.ComplexDouble;
import org.jblas.ComplexDoubleMatrix;

public class JBlasConverter {
    public static Complex toVpcComplex(ComplexDouble complexDouble) {
        return Complex.valueOf(complexDouble.real(), complexDouble.imag());
    }

    public static ComplexDouble fromVpcComplex(Complex complex) {
        return new ComplexDouble(complex.getReal(), complex.getImag());
    }

    public static Matrix toVpcCMatrix(ComplexDoubleMatrix complexDouble) {
        return new JBlasMatrix(complexDouble);
    }

    public static ComplexDoubleMatrix fromVpcCMatrix(TMatrix<Complex> matrix) {
        JBlasMatrix m = (JBlasMatrix) JBlasMatrixFactory.INSTANCE.newMatrix(matrix);
        return m.getBase();
    }
}
