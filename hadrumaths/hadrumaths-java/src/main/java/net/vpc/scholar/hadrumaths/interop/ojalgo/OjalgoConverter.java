package net.vpc.scholar.hadrumaths.interop.ojalgo;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.TMatrix;
import org.ojalgo.matrix.BasicMatrix;
import org.ojalgo.matrix.ComplexMatrix;
import org.ojalgo.matrix.MutableComplexMatrix;
import org.ojalgo.scalar.ComplexNumber;

public class OjalgoConverter {
    public static Complex toVpcComplex(ComplexNumber complexDouble) {
        return Complex.valueOf(complexDouble.getReal(), complexDouble.getImaginary());
    }

    public static ComplexNumber fromVpcComplex(Complex complex) {
        return ComplexNumber.makeRectangular(complex.getReal(), complex.getImag());
    }

    public static Matrix toVpcCMatrix(BasicMatrix complexDouble) {
        if(complexDouble instanceof MutableComplexMatrix) {
            return new OjalgoMatrix((MutableComplexMatrix) complexDouble);
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public static MutableComplexMatrix fromVpcCMatrix(TMatrix<Complex> matrix) {
        OjalgoMatrix m = (OjalgoMatrix) OjalgoMatrixFactory.INSTANCE.newMatrix(matrix);
        return m.getBase();
    }
}
