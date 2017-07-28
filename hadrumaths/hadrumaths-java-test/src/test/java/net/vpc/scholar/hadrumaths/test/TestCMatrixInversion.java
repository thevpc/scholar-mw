//package test;
//
//import net.vpc.scholar.math.CMatrix;
//import net.vpc.scholar.math.Complex;
//
//import java.util.TreeSet;
//
///**
// * Created by IntelliJ IDEA.
// * User: vpc
// * Date: 28 janv. 2009
// * Time: 09:19:36
// * To change this template use File | Settings | File Templates.
// */
//public class TestCMatrixInversion {
//    private static class Item implements Comparable<Item> {
//        long time;
//        CMatrix.InverseStrategy strategy;
//
//        private Item(CMatrix.InverseStrategy strategy, long time) {
//            this.strategy = strategy;
//            this.time = time;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//
//            Item item = (Item) o;
//
//            if (time != item.time) return false;
//            if (strategy != item.strategy) return false;
//
//            return true;
//        }
//
//        @Override
//        public int hashCode() {
//            int result = (int) (time ^ (time >>> 32));
//            result = 31 * result + (strategy != null ? strategy.hashCode() : 0);
//            return result;
//        }
//
//        public int compareTo(Item o) {
//            long l = time-o.time;
//            if (l > 0) {
//                return 1;
//            } else if (l < 0) {
//                return -1;
//            } else {
//                return o.strategy.ordinal() - strategy.ordinal();
//            }
//        }
//
//        @Override
//        public String toString() {
//            return "Item{" +
//                    "strategy=" + strategy +
//                    ", time=" + time +
//                    '}';
//        }
//    }
//
//    public static void main(String[] args) {
//        for (int i = 8; i < 14; i++) {
//        //for (int i = 2; i < 8; i++) {
//            int n = (int) Math.pow(2, i);
//            System.out.println("-------------------------------------------------------------");
//            System.out.println(" size : " + n);
//            System.out.println("-------------------------------------------------------------");
//            CMatrix m = generateMatrix(n);
////        System.out.println(m);
//            TreeSet<Item> values = new TreeSet<Item>();
//            CMatrix.InverseStrategy[] all={
////                CMatrix.InverseStrategy.ADJOINT,
//                CMatrix.InverseStrategy.SOLVE,
//                CMatrix.InverseStrategy.GAUSS,
//                CMatrix.InverseStrategy.BLOCK_SOLVE,
//                CMatrix.InverseStrategy.BLOCK_GAUSS,
////                CMatrix.InverseStrategy.BLOCK_ADJOINT,
//            };
//            for (CMatrix.InverseStrategy inverseStrategy : all) {
//                try {
//                    values.add(evaluateMatrixInversion(m, inverseStrategy));
//                } catch (Exception e) {
//                    System.err.println("["+inverseStrategy+"] "+e.toString());
//                }
//            }
//            for (Item value : values) {
//                System.out.println(value);
//            }
//        }
//    }
//
//    public static Item evaluateMatrixInversion(CMatrix m, CMatrix.InverseStrategy strategy) {
//        long start = System.currentTimeMillis();
//        CMatrix cMatrix = m.inv(strategy);
//        long end = System.currentTimeMillis();
//        Item item = new Item(strategy, end - start);
//        System.out.println("\t[" + m.getRowCount() + "] " + item);
////        System.out.println(cMatrix);
////        System.out.println("----------------------------------------");
//        return item;
//    }
//
//    public static CMatrix generateMatrix(int n) {
//        CMatrix m = new CMatrix(n, n, CMatrix.CellIterator.FULL, new CMatrix.CellFactory() {
//            public Complex item(int row, int column) {
//                if (row == column) {
//                    return new Complex((row+1)*(column+1));
//                } else if(column==0){
//                    return new Complex((row+1)*2);
//                }
//                return Complex.ZERO;
//                //return new Complex(Math.random()*100-50);
//            }
//        });
////        return new CMatrix("[" +
////                "-1  2  3  4  5  6  7  8 ;" +
////                " 1 -2  3  4  5  6  7  8 ;" +
////                " 1  2 -3  4  5  6  7  8 ;" +
////                " 1  2  3 -4  5  6  7  8 ;" +
////                " 1  2  3  4 -5  6  7  8 ;" +
////                " 1  2  3  4  5 -6  7  8 ;" +
////                " 1  2  3  4  5  6 -7  8 ;" +
////                " 1  2  3  4  5  6  7 -8  " +
////                "]");
//        return m;
//    }
//}
