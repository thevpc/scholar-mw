//package net.thevpc.scholar.hadrumaths;
//
//import net.thevpc.scholar.hadrumaths.util.ArrayUtils;
//
//import java.util.Arrays;
//import java.util.List;
//
//public class LoopHelper {
//    public static void main(String[] args) {
//        Object[][] values = new Object[][]{
//                ArrayUtils.box(new int[]{1, 2, 3}),
//                ArrayUtils.box(new int[]{1, 2, 4}),
//        };
//        loopOver(values,v->System.out.println(Arrays.asList(v)));
//        System.out.println("---------------------------");
//        loopOver(new Loop[]{
//                new ArrayLoop(ArrayUtils.box(new int[]{1, 2, 3})),
//                new ArrayLoop(ArrayUtils.box(new int[]{1, 2, 3})),
//        },v->System.out.println(Arrays.asList(v)));
//    }
//
//
//}
