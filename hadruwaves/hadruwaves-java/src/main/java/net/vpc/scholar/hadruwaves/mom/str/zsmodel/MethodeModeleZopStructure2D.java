package net.vpc.scholar.hadruwaves.mom.str.zsmodel;

//package net.vpc.scholar.tmwlib.mom.str.zsmodel;
//
//import net.vpc.scholar.math.CMatrix;
//import net.vpc.scholar.math.Complex;
//import net.vpc.scholar.math.functions.cfxy.CFunctionVector2D;
//import net.vpc.scholar.tmwlib.mom.util.ScalarProductCache;
//import net.vpc.scholar.tmwlib.mom.fnbase.FnBaseFunctions;
//import net.vpc.scholar.tmwlib.mom.fnbase.ModeInfo;
//import net.vpc.scholar.tmwlib.mom.gptest.GpTestFunctions;
//import net.vpc.scholar.tmwlib.mom.str.AMatrixBuilder;
//import net.vpc.scholar.tmwlib.mom.MomStructure;
//import net.vpc.scholar.tmwlib.mom.str.momstr.MoMStructureRef;
//import net.vpc.scholar.math.util.dumpstring.DumpStringHelper;
//
//import java.io.File;
//
///**
// * @author Taha Ben Salah (taha.bensalah@gmail.com)
// * @creationtime 12 mai 2005 12:39:21
// * @deprecated
// */
//@Deprecated
//public class MethodeModeleZopStructure2D extends MomStructure {
//
//    int realK;
//
//
//    public MethodeModeleZopStructure2D(File folder) {
//        super(folder);
//    }
//
//    public MethodeModeleZopStructure2D(MomStructure other, File folder) {
//        super(other, folder);
//    }
//
//    public MethodeModeleZopStructure2D(MomStructure other, String folder) {
//        super(other, folder);
//    }
//
//    public MethodeModeleZopStructure2D(MomStructure other) {
//        super(other, other.getFolder());
//    }
//
//    public DumpStringHelper getDumper() {
//        DumpStringHelper helper = super.getDumper();
//        helper.add("realK", realK);
//        return helper;
//    }
//
//    CMatrix Zop() {
//        CMatrix ret;
//        int theK = realK;
//        if (theK == 2) {
//            MoMStructureRef md = new MoMStructureRef(getFolder());
//            md.setAllValues(this);
//            md.setWidth(getWidth() * (1 - getIrisQuotient()) / 2);
//            md.setK(1);
//            ret = md.computeZin();
//        } else if (theK > 2) {
//            MethodeModeleZopStructure2D mm = new MethodeModeleZopStructure2D(getFolder());
//            mm.setAllValues(this);
//            mm.setWidth(getWidth() * (1 - getIrisQuotient()) / 2);
//            mm.setK(theK - 1);
//            ret = mm.computeZin();
//        } else {
//            return null;
//            //throw new IllegalArgumentException("Zop n'a pas de sens pour k="+theK);
//        }
//        //getLog().debug(getClass().getName()+": [Zop]="+ret);
//        return ret;
//
//    }
//
//    Zoperator ZopLeft() {
//        CMatrix ret;
//        int theK = realK;
//        if (theK == 2) {
//            MoMStructureRef md = new MoMStructureRef(getFolder());
//            md.setAllValues(this);
//            md.setWidth(getWidth() * getMetalQuotient());
//            md.setXminFactor(Double.NaN);
//            md.setXmin(getXmin());
//            md.setK(1);
//            ret = md.computeZin();
//            return new Zoperator(ret, md.getBaseFunctions());
//        } else if (theK > 2) {
//            MethodeModeleZopStructure2D mm = new MethodeModeleZopStructure2D(getFolder());
//            mm.setAllValues(this);
//            mm.setWidth(getWidth() * getMetalQuotient());
//            mm.setXminFactor(Double.NaN);
//            mm.setXmin(getXmin());
//            mm.setK(theK - 1);
//            ret = mm.computeZin();
//            return new Zoperator(ret, mm.getBaseFunctions());
//        } else {
//            return null;
//            //throw new IllegalArgumentException("Zop n'a pas de sens pour k="+theK);
//        }
//        //getLog().debug(getClass().getName()+": [Zop]="+ret);
//    }
//
//    Zoperator ZopRight() {
//        CMatrix ret;
//        int theK = realK;
//        if (theK == 2) {
//            MoMStructureRef md = new MoMStructureRef(getFolder());
//            md.setAllValues(this);
//            md.setWidth(getWidth() * getMetalQuotient());
//            md.setXminFactor(Double.NaN);
//            md.setXmin(getXmin() + getWidth() - md.getWidth());
//            md.setK(1);
//            ret = md.computeZin();
//            return new Zoperator(ret, md.getBaseFunctions());
//        } else if (theK > 2) {
//            MethodeModeleZopStructure2D mm = new MethodeModeleZopStructure2D(getFolder());
//            mm.setAllValues(this);
//            mm.setWidth(getWidth() * getMetalQuotient());
//            mm.setXminFactor(Double.NaN);
//            mm.setXmin(getXmin() + getWidth() - mm.getWidth());
//            mm.setK(theK - 1);
//            ret = mm.computeZin();
//            return new Zoperator(ret, mm.getBaseFunctions());
//        } else {
//            return null;
//            //throw new IllegalArgumentException("Zop n'a pas de sens pour k="+theK);
//        }
//        //getLog().debug(getClass().getName()+": [Zop]="+ret);
//    }
//
//    CMatrix B_old() {
//        GpTestFunctions gpTestFunctions = getGpTestFunctions();
//        CFunctionVector2D[] g = gpTestFunctions.gp();
//        Complex[][] b = new Complex[g.length][g.length];
//        FnBaseFunctions fn = getBaseFunctions();
//        ModeInfo[] n_evan = isParameter(HINT_REGULAR_ZN_OPERATOR) ? fn.getModes() : fn.getVanishingModes();
//        ModeInfo[] n_propa = fn.getPropagatingModes();
//        ScalarProductCache sp = getTestModeScalarProducts();
//        for (int p = 0; p < g.length; p++) {
//            for (int q = 0; q < g.length; q++) {
//                Complex c = Complex.CZERO;
//                for (ModeInfo aN_evan : n_evan) {
//                    Complex zn = aN_evan.zn;
//                    Complex sp1 = sp.gf(p, aN_evan.index);
//                    Complex sp2 = sp.fg(aN_evan.index, q);
//                    c = c.add(zn.multiply(sp1).multiply(sp2));
//                }
//                b[p][q] = c;
//            }
//        }
//
//        CMatrix ZopValue = Zop();
//        Complex[][] zop = ZopValue == null ? null : ZopValue.getArray();
//        for (int p = 0; p < g.length; p++) {
//            for (int q = 0; q < g.length; q++) {
//                Complex c = Complex.CZERO;
//                if (zop != null) {//zop==null si k==1
//                    for (int m = 0; m < zop.length; m++) {
//                        for (int n = 0; n < zop[m].length; n++) {
//                            Complex sp1 = sp.gf(p, n_propa[n].index);
//                            Complex sp2 = sp.fg(n_propa[m].index, q);
//                            c = c.add((zop[n_propa[m].index][n_propa[n].index]).multiply(sp1).multiply(sp2));
//                        }
//                    }
//                }
//                b[p][q] = b[p][q].add(c);
//            }
//        }
//
//        return new CMatrix(b);
//    }
//
//    public AMatrixBuilder createMatrixAEvaluator() {
//        return new ZsAMatrixWaveguideSerialBuilder();
//    }
//
//    CMatrix getAMatrix(CMatrix ZopValue) {
//        GpTestFunctions gpTestFunctions = getGpTestFunctions();
//        CFunctionVector2D[] g = gpTestFunctions.gp();
//        Complex[][] b = new Complex[g.length][g.length];
//        FnBaseFunctions fn = getBaseFunctions();
//        ModeInfo[] n_evan = isParameter(HINT_REGULAR_ZN_OPERATOR) ? fn.getModes() : fn.getVanishingModes();
//        ModeInfo[] n_propa = fn.getPropagatingModes();
//        ScalarProductCache sp = getTestModeScalarProducts();
//        for (int p = 0; p < g.length; p++) {
//            for (int q = 0; q < g.length; q++) {
//                Complex c = Complex.CZERO;
//                for (ModeInfo aN_evan : n_evan) {
//                    Complex zn = aN_evan.zn;
//                    Complex sp1 = sp.gf(p, aN_evan.index);
//                    Complex sp2 = sp.fg(aN_evan.index, q);
//                    c = c.add(zn.multiply(sp1).multiply(sp2));
//                }
//                b[p][q] = c;
//            }
//        }
//
//        Complex[][] zop = ZopValue == null ? null : ZopValue.getArray();
//        for (int p = 0; p < g.length; p++) {
//            for (int q = 0; q < g.length; q++) {
//                Complex c = Complex.CZERO;
//                if (zop != null) {//zop==null si k==1
//                    for (int m = 0; m < zop.length; m++) {
//                        for (int n = 0; n < zop[m].length; n++) {
//                            Complex sp1 = sp.gf(p, n_propa[n].index);
//                            Complex sp2 = sp.fg(n_propa[m].index, q);
//                            c = c.add((zop[n_propa[m].index][n_propa[n].index]).multiply(sp1).multiply(sp2));
//                        }
//                    }
//                }
//                b[p][q] = b[p][q].add(c);
//            }
//        }
//
//        return new CMatrix(b);
//    }
//
//    CMatrix Bevan() {
//        GpTestFunctions gpTestFunctions = getGpTestFunctions();
//        CFunctionVector2D[] g = gpTestFunctions.gp();
//        Complex[][] b = new Complex[g.length][g.length];
//        FnBaseFunctions fn = getBaseFunctions();
//        ModeInfo[] n_evan = isParameter(HINT_REGULAR_ZN_OPERATOR) ? fn.getModes() : fn.getVanishingModes();
//        ScalarProductCache sp = getTestModeScalarProducts();
//        for (int p = 0; p < g.length; p++) {
//            for (int q = 0; q < g.length; q++) {
//                Complex c = Complex.CZERO;
//                for (ModeInfo aN_evan : n_evan) {
//                    Complex zn = aN_evan.zn;
//                    Complex sp1 = sp.gf(p, aN_evan.index);
//                    Complex sp2 = sp.fg(aN_evan.index, q);
//                    c = c.add(zn.multiply(sp1).multiply(sp2));
//                }
//                b[p][q] = c;
//            }
//        }
//        return new CMatrix(b);
//    }
//
//    public CMatrix createMatrixBEvaluator() {
//        GpTestFunctions gpTestFunctions = getGpTestFunctions();
//        CFunctionVector2D[] g = gpTestFunctions.gp();
//        ModeInfo[] n_propa = getBaseFunctions().getPropagatingModes();
//        Complex[][] a = new Complex[g.length][n_propa.length];
//        ScalarProductCache sp = getTestModeScalarProducts();
//        for (int n = 0; n < n_propa.length; n++) {
//            for (int p = 0; p < g.length; p++) {
//                a[p][n] = sp.gf(p, n_propa[n].index).negate();
//            }
//        }
//        return new CMatrix(a);
//    }
//
//    public CMatrix createZinEvaluator() {
//        //getLog().debug(getClass().getName()+".resolveZin() : START");
//
////        getLog().debug("P3MethodeModeleZop.getVanishingModes="+FnBaseFunctions.toStr(fnBaseFunctionsns.getVanishingModes())
////                +" ;\n\tP3MethodeModeleZop.getPropagatingModes="+FnBaseFunctions.toStr(fnBaseFunctionsns.getPropagatingModes())
////        );
//
////        CMatrix B = B();
//        CMatrix A_ = computeMatrixA();
//
//        CMatrix B_ = computeMatrixB();
//        //getLog().debug(getClass().getName()+": [B]="+B);
//
//        CMatrix ZinCond;
//
//        try {
//            ZinCond = B_.transpose().multiply(A_.inverseCond()).multiply(B_).inverse();
//        } catch (Exception e) {
//            getLog().error("Error P3MethodeModeleZop.Zin: " + e);
//            getLog().error("A=" + B_);
//            getLog().error("B=" + A_);
//            throw new RuntimeException(e);
//        }
////        getLog().debug(getClass().getName() + ".resolveZin() : END  =" + ZinCond);
//        return ZinCond;
//    }
//
//    public CMatrix createMatrixUnknownEvaluator() {
//        invalidateCache();
//
//        CMatrix B_ = computeMatrixB();
//        CMatrix A_ = computeMatrixA();
//
//
//        CMatrix Testcoeff;
//
//        try {
//            Testcoeff = A_.solve(B_);
//        } catch (Exception e) {
//            getLog().error("Error : " + e);
//            getLog().error("A=" + B_);
//            getLog().error("B=" + A_);
//            throw new RuntimeException(e);
//        }
//        return Testcoeff;
//    }
//
//    public void setK(int k) {
//        this.k = 1;
//        this.realK = k;
//        invalidateCache();
//    }
//
//    public CMatrix resolveNextPointFixe(CMatrix Zinit, int kInit) {
//        getLog().debug(1.2337139917695478E-4 / 4.11237997256516E-5);
//        double a = getWidth();
//        setK(1);
//        for (int ki = 0; ki < kInit; ki++) {
//            setWidth(a * Math.pow(getMetalQuotient(), (kInit - ki)));
//            CMatrix A_ = getAMatrix(Zinit);
//            CMatrix B_ = computeMatrixB();
//            CMatrix ZinCond;
//            try {
//                ZinCond = B_.transpose().multiply(A_.inverseCond()).multiply(B_).inverse();
//            } catch (Exception e) {
//                getLog().error("Error P3MethodeModeleZop.resolveNextPointFixe: " + e);
//                getLog().error("A=" + B_);
//                getLog().error("B=" + A_);
//                throw new RuntimeException(e);
//            }
//            getLog().debug("k=" + (kInit - ki) + " => a=" + getWidth());
//            getLog().debug("\tz=" + ZinCond);
//            getLog().debug("\terr=" + ZinCond.getRelativeMatrixError3(Zinit, 1E-5));
//            Zinit = ZinCond;
//        }
//        return Zinit;
//    }
//
////    public CMatrix resolveNextPointFixe(CMatrix Znext){
////        //System.out.println(getClass().getName()+".resolveZin() : START");
////
//////        System.out.println("P3MethodeModeleZop.getVanishingModes="+FnBaseFunctions.toStr(fnBaseFunctionsns.getVanishingModes())
//////                +" ;\n\tP3MethodeModeleZop.getPropagatingModes="+FnBaseFunctions.toStr(fnBaseFunctionsns.getPropagatingModes())
//////        );
////
////        CMatrix B = B();
////
////        CMatrix A = A();
////        //System.out.println(getClass().getName()+": [B]="+B);
////
////        CMatrix ZinCond = null;
////
////        try {
////            ZinCond = A.transpose().multiply(B.inverseCond()).multiply(A).inverse();
////        } catch (Exception e) {
////            System.out.println("Error P3MethodeModeleZop.Zin: " + e);
////            System.out.println("A=" + A);
////            System.out.println("B=" + B);
////            throw new RuntimeException(e);
////        }
//////        System.out.println(getClass().getName() + ".resolveZin() : END  =" + ZinCond);
////        return ZinCond;
////    }
////    public CMatrix resolvePointFixe(CMatrix Znext){
////        CFunctionXY[] g = gpTestFunctions.gpCache();
////        int[] n_propa = fnBaseFunctions.getPropagatingModes();
////        Complex[][] Parray=new Complex[g.length][n_propa.length];
////        for (int p = 0; p < g.length; p++) {
////            for (int m = 0; m < n_propa.length; m++) {
////                Parray[p][m]=gpTestFunctions.gf(p, n_propa[m]);
////            }
////        }
////        CMatrix P=new CMatrix(Parray);
////        CMatrix Pt=P.transpose();
////        CMatrix A=A();
////        CMatrix At=A.transpose();
////        CMatrix Be=Bevan();
////        CMatrix PtPinv=(Pt.multiply(P)).inverse();
////        CMatrix AtAinv=(At.multiply(A)).inverse();
////        return PtPinv.multiply(Pt).multiply(
////         (AtAinv.multiply(At.multiply(Znext).multiply(A).multiply(AtAinv))).inverse()
////                .substract(Be)
////        ).multiply(P).multiply(PtPinv);
////
////    }
//}
