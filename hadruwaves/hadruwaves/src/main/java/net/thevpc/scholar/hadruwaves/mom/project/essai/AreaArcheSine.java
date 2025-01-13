///*
// * Created by IntelliJ IDEA.
// * User: taha
// * Date: 5 juil. 03
// * Time: 11:02:55
// * To change template for new class use
// * Code Style | Class Templates options (Tools | IDE Options).
// */
//package net.thevpc.scholar.tmwlib.mom.project.essai;
//
//import net.thevpc.scholar.math.util.Configuration;
//import net.thevpc.scholar.math.functions.dfxy.DFunctionVector2D;
//import net.thevpc.scholar.math.functions.dfxy.PiecewiseSine2XFunctionXY;
//import net.thevpc.scholar.math.functions.FunctionFactory;
//import net.thevpc.scholar.math.Axis;
//import net.thevpc.scholar.math.meshalgo.rect.GridPrecision;
//import net.thevpc.scholar.math.meshalgo.rect.MeshAlgoRect;
//import net.thevpc.scholar.tmwlib.mom.project.common.AreaManager;
//import net.thevpc.scholar.tmwlib.mom.project.common.MetalArea;
//import net.thevpc.scholar.tmwlib.mom.gptest.GpCell;
//import net.thevpc.scholar.tmwlib.mom.gptest.gpmesh.gppattern.ArcheSinusPattern;
//
//public class AreaArcheSine extends MetalArea {
//    public static String TYPE = "ArcheSine";
//
//    public static void install(){
//        AreaManager.register(AreaManager.METAL_CATEGORY, TYPE, AreaArcheSine.class, "Attache Sine", "Fonction Arche Sinus");
//    }
//
//    public DFunctionVector2D[] fct;
//    private boolean cross;
//
//    private double crestValue;
//    private int nbrPeriodes;
//
//    private String crestValueExpression;
//    private String nbrPeriodesExpression;
//
//    @Override
//    public GpCell toGpCell() {
//        return new GpCell(
//                domain, 
//                getStructure().getDomain(),
//                null,
//                new ArcheSinusPattern(true,true,0.5),
//                getSymmetry(),
//                new MeshAlgoRect(new GridPrecision(nbrPeriodes, nbrPeriodes))
//                ,getInvariance()
//                );
//    }
//
//    public AreaArcheSine() {
//        super(TYPE);
//        this.cross = false;
//        this.crestValueExpression = "1";
//        this.nbrPeriodesExpression = "1";
//    }
//
//    @Override
//    public void load(Configuration conf, String key) {
//        super.load(conf, key);
//        this.cross = conf.getBoolean(key + ".cross", false);
//        this.crestValueExpression = String.valueOf(conf.getObject(key + ".value", "1"));
//        this.nbrPeriodesExpression = String.valueOf(conf.getObject(key + ".nbrPeriodes", "1"));
//    }
//    public AreaArcheSine(String name, String crestValue, String nbrPeriodes, boolean cross, String x, String y, String width, String height) {
//        super(TYPE, name, x, y, width, height);
//        this.cross = cross;
//        this.crestValueExpression = crestValue;
//        this.nbrPeriodesExpression = nbrPeriodes;
//    }
//
//    public double getCrestValue() {
////        return ((PiecewiseSine2XFunction) fct[0].fx).crestValue;
//        return crestValue;
//    }
//
//    public int getPeriodCount() {
//        return nbrPeriodes;
////        return ((PiecewiseSine2XFunction) fct[0].fx).getPeriodCount();
//    }
//
//    @Override
//    public int getFunctionMax() {
//        return cross ? 2 : 1;
//    }
//
//    @Override
//    public void store(Configuration c, String key) {
//        super.store(c, key);
//        c.setString(key + ".value", crestValueExpression);
//        c.setString(key + ".nbrPeriods", nbrPeriodesExpression);
//        c.setBoolean(key + ".cross", cross);
//    }
//
//    @Override
//    public boolean equals(Object other) {
//        if (super.equals(other)) {
//            AreaArcheSine s = (AreaArcheSine) other;
//            return getCrestValue() == s.getCrestValue() && getPeriodCount() == s.getPeriodCount() && cross == s.cross;
//        } else {
//            return false;
//        }
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 7;
//        hash = 41 * hash + (this.cross ? 1 : 0);
//        hash = 41 * hash + (int) (Double.doubleToLongBits(this.crestValue) ^ (Double.doubleToLongBits(this.crestValue) >>> 32));
//        hash = 41 * hash + this.nbrPeriodes;
//        return hash;
//    }
//
//    @Override
//    public void recompile() {
//        super.recompile();
//        nbrPeriodes = getContext().evaluateInt(nbrPeriodesExpression);
//        crestValue = getContext().evaluateDouble(crestValueExpression);
//        this.fct =
//                new DFunctionVector2D[]{
//                    new DFunctionVector2D(new PiecewiseSine2XFunctionXY(this.domain, Axis.X,crestValue,
//                            nbrPeriodes,
//                            false),
//                            FunctionFactory.DZEROXY),
//                    new DFunctionVector2D(new PiecewiseSine2XFunctionXY(this.domain, Axis.X, crestValue,
//                            nbrPeriodes,
//                            true),
//                            FunctionFactory.DZEROXY)
//                };
//    }
//
//    public String getCrestValueExpression() {
//        return crestValueExpression;
//    }
//
//    public String getNbrPeriodesExpression() {
//        return nbrPeriodesExpression;
//    }
//}
