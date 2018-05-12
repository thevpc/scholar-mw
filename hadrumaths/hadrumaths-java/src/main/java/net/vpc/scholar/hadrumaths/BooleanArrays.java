package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.Range;
import net.vpc.scholar.hadrumaths.util.BitSet2;

/**
 * Created by vpc on 4/7/17.
 */
public class BooleanArrays {


    public static BooleanArray1 newArray(int i) {
        return new Arr1(i);
    }

    public static BooleanArray2 newArray(int i, int j) {
        return new Arr2(i, j);
    }

    public static BooleanArray3 newArray(int i, int j, int k) {
        return new Arr3(i, j, k);
    }

    public static class Arr1 extends AbstractBooleanArray1 {

        BitSet2 value;
        int size;

        public Arr1(int size, BitSet2 nbits) {
            value = nbits;
            this.size = size;
        }

        public Arr1(int nbits) {
            size = nbits;
            value = new BitSet2(nbits);
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean get(int i) {
            return value.get(i);
        }

        @Override
        public void set(int i, boolean value) {
            this.value.set(i, value);
        }

        @Override
        public BitSet2 toBitSet() {
            return value;
        }

        public void copyFrom(BooleanArray1 src, int srcOffset, int destOffset, int len) {
            value.clear(destOffset, destOffset + len);
            value.or(src.toBitSet().get(srcOffset, srcOffset + len));
        }

    }

    public abstract static class AbstractBooleanArray1 implements BooleanArray1 {

        @Override
        public String toString() {
            return BooleanArrays.toString(this);
        }

        @Override
        public void set(int i) {
            set(i, true);
        }

        @Override
        public void clear(int i) {
            set(i, false);
        }

        public void copyFrom(BooleanArray1 other, Range r0) {
            for (int j = r0.xmin; j <= r0.xmax; j++) {
                set(j, other.get(j));
            }
        }
    }

    public abstract static class AbstractBooleanArray2 implements BooleanArray2 {

        @Override
        public String toString() {
            return BooleanArrays.toString(this);
        }

        @Override
        public void set(int i, int j) {
            set(i, j, true);
        }

        @Override
        public void clear(int i, int j) {
            set(i, j, false);
        }

        public void copyFrom(BooleanArray2 other, Range r0) {
            for (int i = r0.ymin; i <= r0.ymax; i++) {
                for (int j = r0.xmin; j <= r0.xmax; j++) {
                    set(i, j, other.get(i, j));
                }
            }
        }
    }

    public abstract static class AbstractBooleanArray3 implements BooleanArray3 {

        @Override
        public String toString() {
            return BooleanArrays.toString(this);
        }

        @Override
        public void clear(int i, int j, int k) {
            set(i, j, k, false);
        }

        @Override
        public void set(int i, int j, int k) {
            set(i, j, k, true);
        }

        public void copyFrom(BooleanArray3 other, Range r0) {
            if(other==null){
                return;
            }
            for (int i = r0.zmin; i <= r0.zmax; i++) {
                for (int j = r0.ymin; j <= r0.ymax; j++) {
                    for (int k = r0.xmin; k <= r0.xmax; k++) {
//                        set(j, i, k, other.get(j, i, k));
                        set(i, j, k, other.get(i, j, k));
                    }
                }
            }
        }
    }

    public static class Arr2 extends AbstractBooleanArray2 {

        private BitSet2 val;
        private int size1;
        private int size2;
//    public static void main(String[] args) {
//        boolean[][] a = new boolean[4][5];
//        BooleanArray2 b = new BooleanArray2(4, 5);
////        b.index()
//        for (int i = 0; i < a.length; i++) {
//            for (int j = 0; j < a[i].length; j++) {
//                    boolean v = Maths.randomBoolean();
//                    a[i][j] = v;
//                    b.set(i,j,v);
//            }
//        }
//        for (int i = 0; i < a.length; i++) {
//            for (int j = 0; j < a[i].length; j++) {
//                    if(a[i][j]!=b.get(i,j)){
//                        System.out.println("why "+i+" "+j+" : "+a[i][j]+"!="+b.get(i,j));
//                    }
//            }
//        }
//    }

        public Arr2(int size1, int size2) {
            this.size1 = size1;
            this.size2 = size2;
            this.val = new BitSet2(size1 * size2);
        }

        @Override
        public int size1() {
            return size1;
        }

        @Override
        public int size2() {
            return size2;
        }

        public boolean get(int i, int j) {
            return val.get(i * size2 + j);
        }

        public void set(int i, int j, boolean value) {
            val.set(i * size2 + j, value);
        }

        @Override
        public BooleanArray1 get(int i) {
            return new Arr1(size2,
                    val.get(i * size2, (i + 1) * size2)
            );
        }

        @Override
        public BooleanArray2 copy() {
            Arr2 a = new Arr2(size1, size2);
            a.val = (BitSet2) val.clone();
            return a;
        }
    }

    public static class Arr3 extends AbstractBooleanArray3 {

        private BitSet2 val;
        private int size1;
        private int size2;
        private int size3;

        //    public static void main(String[] args) {
//        boolean[][][] a = new boolean[4][5][6];
//        BooleanArray3 b = new BooleanArray3(4, 5, 6);
////        b.index()
//        for (int i = 0; i < a.length; i++) {
//            for (int j = 0; j < a[i].length; j++) {
//                for (int k = 0; k < a[i][j].length; k++) {
//                    boolean v = Maths.randomBoolean();
//                    a[i][j][k] = v;
//                    b.set(i,j,k,v);
////                    System.out.println(i+" "+j+" "+k+" : "+b.get(i,j,k)+":"+v);
////                    System.out.println(i+" "+j+" "+k+" : "+b.index(i,j,k));
//                }
//            }
//        }
//        for (int i = 0; i < a.length; i++) {
//            for (int j = 0; j < a[i].length; j++) {
//                for (int k = 0; k < a[i][j].length; k++) {
//                    if(a[i][j][k]!=b.get(i,j,k)){
//                        System.out.println("why "+i+" "+j+" "+k+" : "+a[i][j][k]+"!="+b.get(i,j,k));
//                    }
//                }
//            }
//        }
//    }
        public Arr3(int size1, int size2, int size3) {
            this.size1 = size1;
            this.size2 = size2;
            this.size3 = size3;
            this.val = new BitSet2(size1 * size2 * size3);
        }

        @Override
        public int size1() {
            return size1;
        }

        @Override
        public int size2() {
            return size2;
        }

        @Override
        public int size3() {
            return size3;
        }

        public boolean get(int i, int j, int k) {
            return val.get(index(i, j, k));
        }

        public void set(int i, int j, int k, boolean value) {
            int bitIndex = index(i, j, k);
            val.set(bitIndex, value);
        }

        private int index(int i, int j, int k) {
            return i * size2 * size3 + j * size3 + k;
        }

        @Override
        public BooleanArray2 get(int i) {
            return new AbstractBooleanArray2() {
                @Override
                public boolean get(int j, int k) {
                    return Arr3.this.get(i, j, k);
                }

                @Override
                public BooleanArray1 get(int j) {
                    return Arr3.this.get(i, j);
                }

                @Override
                public void set(int j, int k, boolean value) {
                    Arr3.this.set(i, j, k, value);
                }

                @Override
                public int size1() {
                    return Arr3.this.size2();
                }

                @Override
                public int size2() {
                    return Arr3.this.size3();
                }

                @Override
                public BooleanArray2 copy() {
                    System.out.println("please check me");
                    Arr2 a = new Arr2(size2, size3);
                    a.val = val.get(
                            i * size2 * size3,
                            (i + 1) * size2 * size3
                    );
                    return a;
                }

            };
        }

        @Override
        public BooleanArray1 get(int i, int j) {
            return new AbstractBooleanArray1() {
                @Override
                public int size() {
                    return Arr3.this.size3;
                }

                @Override
                public boolean get(int k) {
                    return Arr3.this.get(i, j, k);
                }

                @Override
                public void set(int k, boolean value) {
                    Arr3.this.set(i, j, k, value);
                }

                @Override
                public BitSet2 toBitSet() {
                    int x = i * size2 * size3 + j * size3;
                    return val.get(x, x + size3);
                }
            };
        }

        @Override
        public BooleanArray3 copy() {
            Arr3 a = new Arr3(size1, size2, size3);
            a.val = (BitSet2) val.clone();
            return a;
        }

        @Override
        public void set(int i, BooleanArray2 value) {
            if (value instanceof Arr2) {
                int x = i * size2 * size3;
                val.set(x, ((Arr2) value).val, 0, size2 * size3);
            } else {
                for (int j = 0; j < size2; j++) {
                    val.set(i * size2 * size3 + j * size3, value.get(j).toBitSet(), 0, size3);
                }
            }
        }
    }

    public static String toString(BooleanArray1 a) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < a.size(); i++) {
            sb.append(a.get(i) ? "1" : "0");
        }
        sb.append("}");
        return sb.toString();
    }

    public static String toString(BooleanArray2 a) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < a.size1(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append("{");
            for (int j = 0; j < a.size2(); j++) {
                sb.append(a.get(i, j) ? "1" : "0");
            }
            sb.append("}");
        }
        sb.append("}");
        return sb.toString();
    }

    public static String toString(BooleanArray3 a) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < a.size1(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append("{");
            for (int j = 0; j < a.size2(); j++) {
                if (j > 0) {
                    sb.append(",");
                }
                sb.append("{");
                for (int k = 0; k < a.size3(); k++) {
                    sb.append(a.get(i, j, k) ? "1" : "0");
                }
                sb.append("}");
            }
            sb.append("}");
        }
        sb.append("}");
        return sb.toString();
    }

}
