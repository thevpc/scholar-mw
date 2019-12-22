///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.vpc.scholar.hadrumaths.interop.ojalgo;
//
//import net.vpc.scholar.hadrumaths.MathsBase;
//import net.vpc.scholar.hadrumaths.Matrix;
//import org.ojalgo.matrix.BasicMatrix;
//import org.ojalgo.matrix.ComplexMatrix;
//import org.ojalgo.matrix.store.PhysicalStore;
//import org.ojalgo.scalar.ComplexNumber;
//import net.vpc.scholar.hadrumaths.Complex;
//import net.vpc.scholar.hadrumaths.interop.InteropHelper;
//import org.ojalgo.matrix.MatrixBuilder;
//
///**
// *
// * @author vpc
// */
//public class OjalgoHelper implements InteropHelper {
//
//    public static final OjalgoHelper INSTANCE = new OjalgoHelper();
//
//    public Matrix inv(Matrix matrix) {
//        return importObj(exportObj(matrix).invert());
//    }
//
//    public Matrix solve(Matrix a, Matrix b) {
//        return importObj(exportObj(a).solve(exportObj(b)));
//    }
//
//    public Matrix mul(Matrix a, Matrix b) {
//        return importObj((exportObj(a).multiplyRight(exportObj(b))));
//    }
//
//    public Matrix add(Matrix a, Matrix b) {
//        return importObj((exportObj(a).add(exportObj(b))));
//    }
//
//    public ComplexMatrix exportObj(Matrix matrix) {
//        Complex[][] elements = matrix.getArray();
//        int tmpRowDim = elements.length;
//        int tmpColDim = elements[0].length;
//
//        MatrixBuilder<ComplexNumber> b = ComplexMatrix.FACTORY.getBuilder(tmpRowDim, tmpColDim);
//        Complex cc;
//        for (int i = 0; i < tmpRowDim; i++) {
//            for (int j = 0; j < tmpColDim; j++) {
//                cc = elements[i][j];
//                b.set(i, j, ComplexNumber.makeRectangular(cc.getReal(), cc.getImag()));
//            }
//        }
//        return (ComplexMatrix) b.build();
//    }
//
//    public Matrix importObj(BasicMatrix minv) {
//        Complex[][] ret = new Complex[minv.getRowDim()][minv.getColDim()];
//        PhysicalStore<ComplexNumber> cs = minv.toComplexStore();
//
//        int tmpRowDim = ret.length;
//        int tmpColDim = ret[0].length;
//        for (int i = 0; i < tmpRowDim; i++) {
//            for (int j = 0; j < tmpColDim; j++) {
//                ComplexNumber cn = cs.get(i, j);
//                ret[i][j] = Complex.valueOf(cn.getReal(), cn.getImaginary());
//            }
//        }
//        return MathsBase.matrix(ret);
//    }
//
//}
