package net.thevpc.scholar.hadrumaths;

import net.thevpc.scholar.hadrumaths.symbolic.Range;
import net.thevpc.common.util.BitSet2;

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

        @Override
        public int size() {
            return size;
        }

        @Override
        public void setRange(int from, int toExcluded) {
            value.set(from, toExcluded, true);
        }

        public void copyFrom(BooleanArray1 src, int srcOffset, int destOffset, int len) {
            value.clear(destOffset, destOffset + len);
            value.or(src.toBitSet().get(srcOffset, srcOffset + len));
        }

    }

    public abstract static class AbstractBooleanArray1 implements BooleanArray1 {

        @Override
        public int hashCode() {
            int result = 0;
            int s1 = size();
            for (int i = 0; i < s1; i++) {
                result = 31 * result + Boolean.hashCode(get(i));
            }
            result = 31 * result + size();
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || !BooleanArray1.class.isAssignableFrom(getClass())) return false;

            BooleanArray1 arr2 = (BooleanArray1) o;

            int s1 = size();
            if (s1 != arr2.size()) return false;
            for (int i = 0; i < s1; i++) {
                if (get(i) != arr2.get(i)) {
                    return false;
                }
            }
            return true;
        }        @Override
        public void set(int i) {
            set(i, true);
        }

        @Override
        public String toString() {
            return BooleanArrays.toString(this);
        }        @Override
        public void clear(int i) {
            set(i, false);
        }

        public void copyFrom(BooleanArray1 other, Range r0) {
            for (int j = r0.xmin; j <= r0.xmax; j++) {
                set(j, other.get(j));
            }
        }

        public void addFrom(BooleanArray1 other, Range r0) {
            for (int j = r0.xmin; j <= r0.xmax; j++) {
                if (other.get(j)) {
                    set(j);
                }
            }
        }

        public void setAll() {
            for (int j = 0; j < size(); j++) {
                set(j);
            }
        }

        @Override
        public void setRange(int from, int toExcluded) {
            for (int j = from; j < toExcluded; j++) {
                set(j);
            }
        }





        @Override
        public boolean[] toArray() {
            boolean[] b = new boolean[size()];
            for (int i = 0; i < b.length; i++) {
                b[i] = get(i);
            }
            return b;
        }

    }

    public abstract static class AbstractBooleanArray2 implements BooleanArray2 {

        @Override
        public int hashCode() {
            int result = 0;
            int s1 = size1();
            int s2 = size2();
            for (int i = 0; i < s1; i++) {
                for (int j = 0; j < s2; j++) {
                    result = 31 * result + Boolean.hashCode(get(i, j));
                }
            }
            result = 31 * result + size1();
            result = 31 * result + size2();
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || !BooleanArray2.class.isAssignableFrom(getClass())) return false;

            BooleanArray2 arr2 = (BooleanArray2) o;

            int s1 = size1();
            int s2 = size2();
            if (s1 != arr2.size1()) return false;
            if (s2 != arr2.size2()) return false;
            for (int i = 0; i < s1; i++) {
                for (int j = 0; j < s2; j++) {
                    if (get(i, j) != arr2.get(i, j)) {
                        return false;
                    }
                }
            }
            return true;
        }        @Override
        public void set(int i, int j) {
            set(i, j, true);
        }

        @Override
        public String toString() {
            return BooleanArrays.toString(this);
        }        @Override
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

        public void addFrom(BooleanArray2 other, Range r0) {
            for (int i = r0.ymin; i <= r0.ymax; i++) {
                for (int j = r0.xmin; j <= r0.xmax; j++) {
                    if (other.get(i, j)) {
                        set(i, j);
                    }
                }
            }
        }

        public void setAll() {
            for (int i = 0; i < size1(); i++) {
                for (int j = 0; j < size2(); j++) {
                    set(i, j);
                }
            }
        }





        @Override
        public void set(int i, BooleanArray1 arr) {
            for (int j = 0; j < arr.size(); j++) {
                set(i, j, arr.get(j));
            }
        }

        @Override
        public boolean[][] toArray() {
            boolean[][] b = new boolean[size1()][size2()];
            for (int i = 0; i < b.length; i++) {
                for (int j = 0; j < b[i].length; j++) {
                    b[i][j] = get(i, j);
                }
            }
            return b;
        }
    }

    public abstract static class AbstractBooleanArray3 implements BooleanArray3 {

        @Override
        public void clear(int i, int j, int k) {
            set(i, j, k, false);
        }

        @Override
        public void set(int i, int j, int k) {
            set(i, j, k, true);
        }

        @Override
        public void set(int i, BooleanArray2 value) {
            for (int j = 0; j < value.size1(); j++) {
                for (int k = 0; k < value.size2(); k++) {
                    set(i, j, k, value.get(j, k));
                }
            }
        }

        @Override
        public void set(int i, int j, BooleanArray1 value) {
            for (int k = 0; k < value.size(); k++) {
                set(i, j, k, value.get(k));
            }
        }

        public void copyFrom(BooleanArray3 other, Range r0) {
            if (other == null) {
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

        public void addFrom(BooleanArray3 other, Range r0) {
            if (other == null) {
                return;
            }
            for (int i = r0.zmin; i <= r0.zmax; i++) {
                for (int j = r0.ymin; j <= r0.ymax; j++) {
                    for (int k = r0.xmin; k <= r0.xmax; k++) {
//                        set(j, i, k, other.get(j, i, k));
                        if (other.get(i, j, k)) {
                            set(i, j, k);
                        }
                    }
                }
            }
        }

        @Override
        public boolean[][][] toArray() {
            boolean[][][] b = new boolean[size1()][size2()][size3()];
            for (int i = 0; i < b.length; i++) {
                for (int j = 0; j < b[i].length; j++) {
                    for (int k = 0; k < b[i][j].length; k++) {
                        b[i][j][k] = get(i, j, k);
                    }
                }
            }
            return b;
        }

        public void setAll() {
            for (int i = 0; i < size1(); i++) {
                for (int j = 0; j < size2(); j++) {
                    for (int k = 0; k < size3(); k++) {
                        set(i, j, k);
                    }
                }
            }
        }

        @Override
        public int hashCode() {
            int result = 0;
            int s1 = size1();
            int s2 = size2();
            int s3 = size3();
            for (int i = 0; i < s1; i++) {
                for (int j = 0; j < s2; j++) {
                    for (int k = 0; k < s3; k++) {
                        result = 31 * result + Boolean.hashCode(get(i, j, k));
                    }
                }
            }
            result = 31 * result + size1();
            result = 31 * result + size2();
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || !BooleanArray3.class.isAssignableFrom(getClass())) return false;

            BooleanArray3 arr2 = (BooleanArray3) o;

            int s1 = size1();
            int s2 = size2();
            int s3 = size3();
            if (s1 != arr2.size1()) return false;
            if (s2 != arr2.size2()) return false;
            if (s3 != arr2.size3()) return false;
            for (int i = 0; i < s1; i++) {
                for (int j = 0; j < s2; j++) {
                    for (int k = 0; k < s3; k++) {
                        if (get(i, j, k) != arr2.get(i, j, k)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        @Override
        public String toString() {
            return BooleanArrays.toString(this);
        }

    }

    public static class Arr2 extends AbstractBooleanArray2 {

        private BitSet2 val;
        private final int size1;
        private final int size2;
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
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || !BooleanArray2.class.isAssignableFrom(getClass())) return false;

            if (o instanceof Arr2) {
                Arr2 arr2 = (Arr2) o;

                if (size1 != arr2.size1) return false;
                if (size2 != arr2.size2) return false;
                return val != null ? val.equals(arr2.val) : arr2.val == null;
            } else {
                BooleanArray2 arr2 = (BooleanArray2) o;

                if (size1 != arr2.size1()) return false;
                if (size2 != arr2.size2()) return false;
                for (int i = 0; i < size1; i++) {
                    for (int j = 0; j < size1; j++) {
                        if (get(i, j) != arr2.get(i, j)) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }

        public boolean get(int i, int j) {
            return val.get(i * size2 + j);
        }

        @Override
        public BooleanArray1 get(int i) {
            return new Arr1(size2,
                    val.get(i * size2, (i + 1) * size2)
            );
        }

        public void setRange(int i, int jFrom, int jTo, boolean value) {
            int ii = i * size2;
            val.set(ii + jFrom, ii + jTo, value);
        }

        public void set(int i, int j, boolean value) {
            val.set(i * size2 + j, value);
        }

        @Override
        public BooleanArray2 copy() {
            Arr2 a = new Arr2(size1, size2);
            a.val = val.clone();
            return a;
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
        public int hashCode() {
            int result = val != null ? val.hashCode() : 0;
            result = 31 * result + size1;
            result = 31 * result + size2;
            return result;
        }
    }

    public static class Arr3 extends AbstractBooleanArray3 {

        private BitSet2 val;
        private final int size1;
        private final int size2;
        private final int size3;

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

        public void setAll(int i, int j, int kFrom, int kTo, boolean value) {
            int bitIndex = index(i, j, kFrom);
            val.set(bitIndex, bitIndex + kTo - kFrom, value);
        }

        public void set(int i, int j, int k, boolean value) {
            int bitIndex = index(i, j, k);
            val.set(bitIndex, value);
        }

        public boolean get(int i, int j, int k) {
            return val.get(index(i, j, k));
        }

        @Override
        public BooleanArray1 get(int i, int j) {
            return new AbstractBooleanArray1() {
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

                @Override
                public int size() {
                    return Arr3.this.size3;
                }
            };
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
                public BooleanArray2 copy() {
                    System.out.println("please check me");
                    Arr2 a = new Arr2(size2, size3);
                    a.val = val.get(
                            i * size2 * size3,
                            (i + 1) * size2 * size3
                    );
                    return a;
                }

                @Override
                public int size1() {
                    return Arr3.this.size2();
                }

                @Override
                public int size2() {
                    return Arr3.this.size3();
                }

            };
        }

        @Override
        public BooleanArray3 copy() {
            Arr3 a = new Arr3(size1, size2, size3);
            a.val = val.clone();
            return a;
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

        private int index(int i, int j, int k) {
            return i * size2 * size3 + j * size3 + k;
        }

//        @Override
//        public void set(int i, BooleanArray2 value) {
//            if (value instanceof Arr2) {
//                int x = i * size2 * size3;
//                val.set(x, ((Arr2) value).val, 0, size2 * size3);
//            } else {
//                for (int j = 0; j < size2; j++) {
//                    val.set(i * size2 * size3 + j * size3, value.get(j).toBitSet(), 0, size3);
//                }
//            }
//        }

    }

}
