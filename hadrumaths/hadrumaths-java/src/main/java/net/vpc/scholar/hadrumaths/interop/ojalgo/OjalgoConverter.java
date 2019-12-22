package net.vpc.scholar.hadrumaths.interop.ojalgo;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.TMatrix;
import org.ojalgo.matrix.BasicMatrix;
import org.ojalgo.matrix.MutableComplexMatrix;
import org.ojalgo.scalar.ComplexNumber;

public class OjalgoConverter {
    public static Complex toVpcComplex(ComplexNumber complexDouble) {
        return Complex.valueOf(complexDouble.getReal(), complexDouble.getImaginary());
    }

    public static ComplexNumber fromVpcComplex(Complex complex) {
        return ComplexNumber.makeRectangular(complex.getReal(), complex.getImag());
    }

    public static ComplexMatrix toVpcCMatrix(BasicMatrix complexDouble) {
        if (complexDouble instanceof MutableComplexMatrix) {
            return new OjalgoComplexMatrix((MutableComplexMatrix) complexDouble);
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public static MutableComplexMatrix fromVpcCMatrix(TMatrix<Complex> matrix) {
        OjalgoComplexMatrix m = (OjalgoComplexMatrix) OjalgoComplexMatrixFactory.INSTANCE.newMatrix(matrix);
        return m.getBase();
    }
}
