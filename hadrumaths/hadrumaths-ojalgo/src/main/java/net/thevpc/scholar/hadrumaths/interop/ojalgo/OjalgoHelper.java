///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.thevpc.scholar.hadrumaths.interop.ojalgo;
//
//import net.thevpc.scholar.hadrumaths.Maths;
//import net.thevpc.scholar.hadrumaths.Matrix;
//import org.ojalgo.matrix.BasicMatrix;
//import org.ojalgo.matrix.ComplexMatrix;
//import org.ojalgo.matrix.store.PhysicalStore;
//import org.ojalgo.scalar.ComplexNumber;
//import net.thevpc.scholar.hadrumaths.Complex;
//import net.thevpc.scholar.hadrumaths.interop.InteropHelper;
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
//        Complex[][] primitiveElement3DS = matrix.getArray();
//        int tmpRowDim = primitiveElement3DS.length;
//        int tmpColDim = primitiveElement3DS[0].length;
//
//        MatrixBuilder<ComplexNumber> b = ComplexMatrix.FACTORY.getBuilder(tmpRowDim, tmpColDim);
//        Complex cc;
//        for (int i = 0; i < tmpRowDim; i++) {
//            for (int j = 0; j < tmpColDim; j++) {
//                cc = primitiveElement3DS[i][j];
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
//        return Maths.matrix(ret);
//    }
//
//}
