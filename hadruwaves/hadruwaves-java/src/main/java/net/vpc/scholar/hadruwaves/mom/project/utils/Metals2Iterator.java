package net.vpc.scholar.hadruwaves.mom.project.utils;

//package net.vpc.scholar.tmwlib.mom.planar.utils;
//
//import net.vpc.scholar.tmwlib.mom.planar.Jxy;
//
///**
// * User: vpc
// * Date: 11 févr. 2005
// * Time: 01:46:30
// */
//abstract class Metals2Iterator {
//    private Jxy jxy;
//
//    public Metals2Iterator(Jxy jxy) {
//        this.jxy = jxy;
//    }
//
//    public void iterate() {
//        int row = 0;
//        int col = 0;
//        for (int metal1 = 0; metal1 < jxy.metal.length; metal1++) {
//            int max_p1 = jxy.metal[metal1].getFunctionMax();
//            for (int metalIndex1 = 0; metalIndex1 < max_p1; metalIndex1++) {
//                col = 0;
//                for (int metal2 = 0; metal2 < jxy.metal.length; metal2++) {
//                    int max_p2 = jxy.metal[metal2].getFunctionMax();
//                    for (int metalIndex2 = 0; metalIndex2 < max_p2; metalIndex2++) {
//                        run(row, col, metal1, metalIndex1, metal2, metalIndex2);
//                        col++;
//                    }
//                }
//                row++;
//            }
//        }
//    }
//
//    protected abstract void run(int row, int col, int metal1, int metalIndex1, int metal2, int metalIndex2);
//}
