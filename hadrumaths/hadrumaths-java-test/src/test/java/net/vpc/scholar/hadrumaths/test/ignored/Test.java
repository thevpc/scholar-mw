//package test;
//
//import net.vpc.scholar.math.CMatrix;
//
//import java.io.File;
//import java.io.IOException;
//
//
///**
// * User: taha
// * Date: 25 aout 2003
// * Time: 20:03:12
// */
//public class Test {
//    public static void main(String[] args) {
//        int n = 4;
//        for (int i = 1; i < n; i++) {
//            for (int j = 1; j < n; j++) {
//                System.out.println(String.valueOf(i) + String.valueOf(j));
//            }
//        }
//        System.out.println("----------");
//        for (int i = 1; i < n; i++) {
//            System.out.println("i = " + i);
//            go(i);
//        }
//
//
//    }
//
//    private static void go(int n) {
//        int min = 1;
//        if (n == min) {
//            System.out.println(String.valueOf(min) + String.valueOf(min));
//        } else {
//            for (int i = min; i < n; i++) {
//                System.out.println(String.valueOf(i) + String.valueOf(n));
//                System.out.println(String.valueOf(n) + String.valueOf(i));
//            }
//            System.out.println(String.valueOf(n) + String.valueOf(n));
//        }
//    }
//
//    public static void main0(String[] args) {
//        try {
//
//            //JEP p=new JEP();
//
//            CMatrix m = new CMatrix(new File("c:/ez.txt"));
//            CMatrix m2 = new CMatrix(m.getRowCount() * 2 - 1, m.getColumnDimension());
//            for (int r = 0; r < m.getRowCount(); r++) {
//                for (int c = 0; c < m.getColumnCount(); c++) {
//                    m2.set(m.get(r, c), r, c);
//                    m2.set(m.get(r, c), m2.getRowCount() - r - 1, c);
//                }
//            }
//            m2.store(new File("c:/ez2.txt"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
