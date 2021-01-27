package net.thevpc.scholar.hadrumaths.test.removeme;

//package net.thevpc.scholar.math.derivation.formal;
//
//import net.thevpc.scholar.math.Axis;
//import net.thevpc.scholar.math.derivation.FunctionDifferentiatorManager;
//import net.thevpc.scholar.math.IDCxy;
//import net.thevpc.scholar.math.IDDxy;
//import net.thevpc.scholar.math.functions.cfxy.DCxyAbstractSum;
//import net.thevpc.scholar.math.functions.cfxy.DCxySum;
//
///**
// * @author Taha Ben Salah (taha.bensalah@gmail.com)
// * @creationtime 6 juil. 2007 12:36:49
// */
//public class CAbstractSumFunctionXYDerivator {
//    public IDCxy derive(IDCxy f, Axis varIndex, FunctionDifferentiatorManager d) {
//        DCxyAbstractSum c = (DCxyAbstractSum) f;
//        IDCxy[] s = c.getSegments();
//        IDCxy[] s2 = new IDCxy[s.length];
//        for (int i = 0; i < s.length; i++) {
//            s2[i] = d.derive(s[i], varIndex);
//        }
//        return new DCxySum(s2);
//    }
//
//    public IDDxy derive(IDDxy f, Axis varIndex, FunctionDifferentiatorManager d) {
//        throw new IllegalArgumentException();
//    }
//}
