///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.vpc.scholar.math.interop.jblas;
//
//import net.vpc.scholar.math.CMatrix;
//import net.vpc.scholar.math.Complex;
//import net.vpc.scholar.math.interop.InteropHelper;
//import org.jblas.ComplexDouble;
//import org.jblas.ComplexDoubleMatrix;
//
///**
// *
// * @author vpc
// */
//public class JBlasHelper implements InteropHelper{
//    public static final JBlasHelper INSTANCE=new JBlasHelper();
//    public  ComplexDouble exportObj(Complex c) {
//        return new ComplexDouble(c.getReal(), c.getImag());
//    }
//
//    public  Complex importObj(ComplexDouble c) {
//        return new Complex(c.real(), c.imag());
//    }
//
//    public  ComplexDoubleMatrix exportObj(CMatrix m) {
//        ComplexDoubleMatrix e = new ComplexDoubleMatrix(m.getRowCount(), m.getColumnDimension());
//        for (int r = 0; r < m.getRowCount(); r++) {
//            for (int c = 0; c < m.getColumnCount(); c++) {
//                e.put(r, c, exportObj(m.get(r, c)));
//            }
//        }
//        return e;
//    }
//
//    public  CMatrix importObj(ComplexDoubleMatrix m) {
//        CMatrix e = new CMatrix(m.rows, m.columns);
//        for (int r = 0; r < m.rows; r++) {
//            for (int c = 0; c < m.columns; c++) {
//                e.set(importObj(m.get(r, c)), r, c);
//            }
//        }
//        return e;
//    }
//    public CMatrix inv(CMatrix c){
//        throw new IllegalArgumentException("Not Supported");
//    }
//}
