//package test;
//
//import net.thevpc.scholar.math.functions.DomainXY;
//import net.thevpc.scholar.math.functions.cfxy.CFunctionVector2D;
//import net.thevpc.scholar.math.plot.Plot;
//import net.thevpc.scholar.tmwlib.mom.testfunctions.gpmesh.gppattern.CellBoundaries;
//import net.thevpc.scholar.tmwlib.mom.testfunctions.gpmesh.gppattern.UserSinePattern;
//
//import java.util.ArrayList;
//
///**
// * @author Taha Ben Salah (taha.bensalah@gmail.com)
// * @creationtime 31 juil. 2007 21:59:03
// */
//public class TestUserSinePattern {
//    public static void main(String[] args) {
//        DomainXY lilltleDomain = new DomainXY(1, 1, 2, 2);
//        DomainXY globalDomain = new DomainXY(0, 0, 3, 3);
//        ArrayList<CFunctionVector2D> ll=new ArrayList<CFunctionVector2D>();
//        for (CellBoundaries cellBoundaries : CellBoundaries.values()) {
//            CFunctionVector2D vector2D = UserSinePattern.createFunction(
//                    cellBoundaries,
//                    cellBoundaries,
//                    1,
//                    0, 1,
//                    lilltleDomain,
//                    globalDomain
//            );
//            ll.add(vector2D);
//        }
//        Plot.plotFunctions("thing",globalDomain, (CFunctionVector2D[]) ll.toArray(new CFunctionVector2D[ll.size()]));
//    }
//}
