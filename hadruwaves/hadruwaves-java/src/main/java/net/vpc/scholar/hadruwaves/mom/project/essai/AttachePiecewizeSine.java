package net.vpc.scholar.hadruwaves.mom.project.essai;

//package net.vpc.scholar.tmwlib.mom.planar.essai;
//
//import org.vpc.params.Configuration;
//import net.vpc.scholar.math.functions.dfxy.DFunctionVector2D;
//import net.vpc.scholar.math.functions.dfxy.PiecewiseSineXFunctionXY;
//import net.vpc.scholar.math.functions.FunctionFactory;
//import net.vpc.scholar.math.Axis;
//import net.vpc.scholar.tmwlib.mom.planar.common.AreaManager;
//import net.vpc.scholar.tmwlib.mom.planar.common.MetalArea;
//
///**
// * User: taha
// * Date: 3 juil. 2003
// * Time: 17:32:08
// */
//public class AttachePiecewizeSine extends MetalArea {
//    public static String TYPE = "PiecewizeSine";
//
//    public static void install(){
//        AreaManager.register(AreaManager.METAL_CATEGORY, TYPE, AttachePiecewizeSine.class, "Attache Piecewise Sine", "Fonction Arche Sinus par parties");
//    }
//
//    DFunctionVector2D[] fct;
//    boolean crossAttache;
//    double factor;
//    int periodsCount;
//    String factorExpression;
//    String periodsCountExpression;
//
//    public String dump() {
//        StringBuffer sb = new StringBuffer(super.dump());
//        sb.append("  periodsCount=").append(periodsCount).append("\n");
//        sb.append("  factor=").append(factor).append("\n");
//        sb.append("  crossAttache=").append(crossAttache).append("\n");
//        return sb.toString();
//    }
//
//    public AttachePiecewizeSine() {
//        super(TYPE);
//        this.crossAttache = false;
//        this.factorExpression = "1";
//        this.periodsCountExpression = "1";
//    }
//    public AttachePiecewizeSine(Configuration conf, String key) {
//        super(conf, key);
//        this.crossAttache = conf.getBoolean(key + ".cross", false);
//        this.factorExpression = String.valueOf(conf.getObject(key + ".factor", "1"));
//        this.periodsCountExpression = String.valueOf(conf.getObject(key + ".periods", "1"));
//    }
//
//    public AttachePiecewizeSine(String name, String factor, String periodsCount, boolean crossAttache, String x, String y, String width, String height) {
//        super(TYPE, name, x, y, width, height);
//        this.crossAttache = crossAttache;
//        this.factorExpression = factor;
//        this.periodsCountExpression = periodsCount;
//    }
//
//    public double getFactor() {
//        return ((PiecewiseSineXFunctionXY) fct[0].fx).getFactor();
//    }
//
//    public int getPeriodCount() {
//        return ((PiecewiseSineXFunctionXY) fct[0].fx).getPeriodCount();
//    }
//
//    public DFunctionVector2D[] getAllFunctions() {
//        return fct;
//    }
//
//    public DFunctionVector2D getFunction(int i) {
//        return fct[i];
//    }
//
//    public int getFunctionMax() {
//        return crossAttache ? 2 : 1;
//    }
//
//    public void store(Configuration c, String key) {
//        super.store(c, key);
//        c.setString(key + ".factor", factorExpression);
//        c.setString(key + ".periods", periodsCountExpression);
//        c.setBoolean(key + ".cross", crossAttache);
//    }
//
//    public boolean equals(Object other) {
//        if (super.equals(other)) {
//            if(fct==null){
//                recompile();
//            }
//            AttachePiecewizeSine s = (AttachePiecewizeSine) other;
//            return getFactor() == s.getFactor() && getPeriodCount() == s.getPeriodCount() && crossAttache == s.crossAttache;
//        } else {
//            return false;
//        }
//    }
//
//    public void recompile() {
//        super.recompile();
//        periodsCount = getStructureContext().evaluateInt(periodsCountExpression);
//        factor = getStructureContext().evaluateDouble(factorExpression);
//        fct = new DFunctionVector2D[]{
//                //todo amplitude is always 1????
//            new DFunctionVector2D(new PiecewiseSineXFunctionXY(domain, Axis.X, /**todo**/ 1,factor, periodsCount, false),
//                    FunctionFactory.DZEROXY),
//            new DFunctionVector2D(new PiecewiseSineXFunctionXY(domain, Axis.X, /**todo**/ 1,factor, periodsCount, true),
//                    FunctionFactory.DZEROXY)
//        };
//    }
//
//    public String getFactorExpression() {
//        return factorExpression;
//    }
//
//    public String getPeriodsCountExpression() {
//        return periodsCountExpression;
//    }
//}
