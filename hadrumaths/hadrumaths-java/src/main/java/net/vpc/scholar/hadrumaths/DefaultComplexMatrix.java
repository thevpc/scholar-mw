package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.util.adapters.ComplexMatrixFromComplexMatrix;

public class DefaultComplexMatrix {
    public static ComplexMatrix of(Matrix c){
        if(c instanceof ComplexMatrix){
            return (ComplexMatrix) c;
        }
        Matrix<Complex> u = c.to(Maths.$COMPLEX);
        if(u instanceof ComplexMatrix){
            return (ComplexMatrix) u;
        }
        return new ComplexMatrixFromComplexMatrix(u);
    }
}
