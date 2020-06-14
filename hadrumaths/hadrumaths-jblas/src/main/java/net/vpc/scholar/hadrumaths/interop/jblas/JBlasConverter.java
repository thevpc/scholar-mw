package net.vpc.scholar.hadrumaths.interop.jblas;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.Matrix;
import org.jblas.ComplexDouble;
import org.jblas.ComplexDoubleMatrix;

public class JBlasConverter {
    public static Complex toVpcComplex(ComplexDouble complexDouble) {
        return Complex.of(complexDouble.real(), complexDouble.imag());
    }

    public static ComplexDouble fromVpcComplex(Complex complex) {
        return new ComplexDouble(complex.getReal(), complex.getImag());
    }

    public static ComplexMatrix toVpcCMatrix(ComplexDoubleMatrix complexDouble) {
        return new JBlasComplexMatrix(complexDouble);
    }

    public static ComplexDoubleMatrix fromVpcCMatrix(Matrix<Complex> matrix) {
        JBlasComplexMatrix m = (JBlasComplexMatrix) JBlasComplexMatrixFactory.INSTANCE.newMatrix(matrix);
        return m.getBase();
    }
}
