package net.vpc.scholar.hadrumaths.test.removeme;

//package test;
//
//import net.vpc.scholar.math.Chronometer;
//import net.vpc.scholar.math.CMatrix;
//import net.vpc.scholar.math.DMatrix;
//
///**
// * @author Taha Ben Salah (taha.bensalah@gmail.com)
// * @creationtime 31 juil. 2007 21:43:50
// */
//public class TestCMatrix {
//    public static void main(String[] args) {
//
//    }
//    public static void test1(){
//            CMatrix m = new CMatrix("[1 2 3 5; 3 5 2 8; 3 8 2 1; 3 8 2 2]");
//            for (int i = 0; i < 3; i++) {
//                m.invAdjoint();
//                m.invBlock(CMatrix.InverseStrategy.ADJOINT,64);
//            }
//            int max = 10000;
//            Chronometer c = new Chronometer();
//            c.start();
//            System.out.println("cMatrix = " + m.inv());
//            for (int i = 0; i < max; i++) {
//                m.invAdjoint();
//            }
//            c.stop();
//            System.out.println("c = " + c.getTime());
//
//            c.start();
//            System.out.println("cMatrix = " + m.invBlock(CMatrix.InverseStrategy.ADJOINT,64));
//            for (int i = 0; i < max; i++) {
//                m.invBlock(CMatrix.InverseStrategy.SOLVE,64);
//            }
//            c.stop();
//            System.out.println("c = " + c.getTime());
//
//            c.start();
//            System.out.println("cMatrix = " + m.inv());
//            for (int i = 0; i < max; i++) {
//                m.inv();
//            }
//            c.stop();
//            System.out.println("c = " + c.getTime());
//    }
//
//    public static void test2(String[] args) {
////        CMatrix CA=CMatrix.randomRealMatrix(2,2,-10,10);
////        CMatrix CB=CMatrix.randomRealMatrix(2,1,-10,10);
//        CMatrix CA=new CMatrix(new double[][]{{-10,-1},{-10,4}});
//        CMatrix CB=new CMatrix(new double[][]{{-5},{4}});
//
//        System.out.println("CA = "+CA);
//        System.out.println("CB = "+CB);
//        CMatrix CX=CA.solve(CB);
//        System.out.println("CX = "+CX);
//        CMatrix CDiff=CA.multiply(CX).substract(CB);
//        System.out.println("---------------------");
//        double[][] a=new double[CA.getRowCount()][CA.getColumnCount()];
//        for(int i=0;i<a.length;i++)for(int j=0;j<a[i].length;j++)a[i][j]=CA.get(i,j).getReal();
//        double[][] b=new double[CB.getRowCount()][CB.getColumnCount()];
//        for(int i=0;i<b.length;i++)for(int j=0;j<b[i].length;j++)b[i][j]=CB.get(i,j).getReal();
//        DMatrix A=new DMatrix(a);
//        DMatrix B=new DMatrix(b);
//        System.out.println("A = "+A);
//        System.out.println("B = "+B);
//        DMatrix X=A.solve(B);
//        System.out.println("X = "+X);
//        System.out.println("CDiff = "+CDiff);
////        DMatrix Diff=A.times(X).minus(B);
////        System.out.println("Diff = "+Diff);
//        CMatrix CCDiff=new CMatrix(CX).substract(new CMatrix(X.getArray()));
//        System.out.println("---------------------------------------------------------");
//        System.out.println("---------------------------------------------------------");
//        System.out.println("---------------------------------------------------------");
//        System.out.println("Diff = "+CCDiff);
//
//    }
//
//    public static void test3(String[] args) {
//        CMatrix m=new CMatrix("[1 2+i 3; 2 5 6]");
//        System.out.println("m = " + m);
//    }
//
//}
