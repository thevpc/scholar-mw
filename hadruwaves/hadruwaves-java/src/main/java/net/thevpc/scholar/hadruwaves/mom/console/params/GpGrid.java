package net.thevpc.scholar.hadruwaves.mom.console.params;

//package net.thevpc.scholar.tmwlib.mom.console.paramsets;
//
//import net.thevpc.scholar.tmwlib.mom.str.AbstractStructure2D;
//import net.thevpc.scholar.math.plot.plotconsole.paramsets.IntArrayParamSet;
//import net.thevpc.scholar.tmwlib.mom.gptest.GpTestFunctions;
//import net.thevpc.scholar.tmwlib.mom.gptest.gpmesh.GpAdaptatif;
//
//public class GpGrid extends IntArrayParamSet implements Cloneable {
//    static String NAME = null;
////    private int startMax;
////    private int endMin;
//    private Grid[] grids;
//
//    public GpGrid(Grid... grids) {
//        super(NAME, 0);
//        this.grids = grids;
//        int[] values = new int[grids.length];
//        for (int i = 0; i < values.length; i++) {
//            values[i] = i;
//        }
//        dsteps(values);
//    }
//
////    public GpGrid(int value) {
////        this(value, value);
////    }
////
////    public GpGrid(int min, int max) {
////        this(new int[]{min}, new int[]{max});
////    }
////
////    public GpGrid(int[][] minmax) {
////        this(minmax, 0, -1);
////    }
////    public GpGrid(int[][] minmax, int count) {
////        this(minmax, 0, count);
////    }
////
////    public GpGrid(int[][] minmax, int[] indexes) {
////        super(NAME, 0);
////        this.min = new int[minmax.length];
////        this.max = new int[minmax.length];
////        for (int i = 0; i < indexes.length; i++) {
////            min[i] = minmax[i][0];
////            max[i] = minmax[i][1];
////        }
////        dsteps(indexes);
////    }
////
////    public GpGrid(int[][] minmax, int from, int to) {
////        super(NAME, 0);
////        if (from < 0 || from > minmax.length) {
////            from = 0;
////        }
////        if (to < 0 || to > minmax.length) {
////            to = minmax.length;
////        }
////        this.min = new int[minmax.length];
////        this.max = new int[minmax.length];
////        for (int i = 0; i < minmax.length; i++) {
////            min[i] = minmax[i][0];
////            max[i] = minmax[i][1];
////
////        }
////        int[] values = new int[to - from];
////        for (int i = 0; i < values.length; i++) {
////            values[i] = from+i;
////        }
////        dsteps(values);
////    }
////
////    public GpGrid(int[] min, int[] max) {
////        super(NAME, 0);
////        this.min = min;
////        this.max = max;
////        int[] values = new int[Maths.max(min.length, max.length)];
////        for (int i = 0; i < values.length; i++) {
////            values[i] = i;
////        }
////        dsteps(values);
////    }
////    public GpGrid(int start, int mid, int end, int count) {
////        super(NAME, 0);
////        this.startMax = start;
////        this.endMin = end;
////        ArrayList<int[]> c = new ArrayList<int[]>();
////        for (int i = start; i <= end; i++) {
////            c.add(new int[]{start, i});
////        }
////        int mid0 = ((mid >= (start + 1) && mid <= end) ? mid : end);
////        for (int i = start + 1; i <= mid0; i++) {
////            c.add(new int[]{i, end});
////        }
////        max = new int[c.size()];
////        min = new int[c.size()];
////        int[] values = new int[(count > 0 && count < c.size()) ? count : c.size()];
////        for (int i = 0; i < c.size(); i++) {
////            int[] ints = c.get(i);
////            min[i] = ints[0];
////            max[i] = ints[1];
////            if (i < values.length) {
////                values[i] = i;
////            }
////        }
////        dsteps(values);
////    }
//
//
//    public void setParameter(AbstractStructure2D structure) {
//        GpTestFunctions functions = structure.getGpTestFunctions();
//        if (functions instanceof GpAdaptatif) {
//            GpAdaptatif gpa = (GpAdaptatif) functions;
//            gpa.setGpMinGridPrecisionX(grids[getValue()].getMin());
//            gpa.setGpMaxGridPrecisionX(grids[getValue()].getMin());
//            gpa.setGpMinGridPrecisionY(-1);
//            gpa.setGpMaxGridPrecisionY(-1);
//            structure.setGpTestFunctions(gpa, structure.getSchemaType());
//        }
//    }
//
//    public String toString() {
//        return getName() + "=" + getValue() + "[1/2^" + grids[getValue()].getMin() + "->1/2^" + grids[getValue()].getMax() + "]";
//    }
//
//
////    public int getStartMax() {
////        return startMax;
////    }
////
////    public int getEndMin() {
////        return endMin;
////    }
//
//
//}
