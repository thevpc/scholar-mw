package net.thevpc.scholar.hadrumaths.interop.ojalgo;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.Matrix;
import org.ojalgo.matrix.BasicMatrix;
import org.ojalgo.matrix.MutableComplexMatrix;
import org.ojalgo.scalar.ComplexNumber;

public class OjalgoConverter {
    public static Complex toVpcComplex(ComplexNumber complexDouble) {
        return Complex.of(complexDouble.getReal(), complexDouble.getImaginary());
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

    public static MutableComplexMatrix fromVpcCMatrix(Matrix<Complex> matrix) {
        OjalgoComplexMatrix m = (OjalgoComplexMatrix) OjalgoComplexMatrixFactory.INSTANCE.newMatrix(matrix);
        return m.getBase();
    }
}
