//package net.vpc.scholar.tmwlib.mom.project.essai;
//
//import net.vpc.scholar.math.util.Configuration;
//import net.vpc.scholar.math.Math2;
//import net.vpc.scholar.math.functions.dfxy.DCosCosFunctionXY;
//import net.vpc.scholar.math.functions.dfxy.DFunctionVector2D;
//import net.vpc.scholar.math.meshalgo.rect.MeshAlgoRect;
//import net.vpc.scholar.tmwlib.mom.project.common.AreaManager;
//import net.vpc.scholar.tmwlib.mom.gptest.GpCell;
//import net.vpc.scholar.tmwlib.mom.gptest.gpmesh.gppattern.BoxModesPattern;
//
///**
// * User: taha
// * Date: 2 juil. 2003
// * Time: 12:03:05
// */
//public class AreaSine extends AbstractAreaPQ {
//    public static String TYPE = "Sine";
//
//    public static void install(){
//        AreaManager.register(AreaManager.METAL_CATEGORY, TYPE, AreaSine.class,
//                "Fonction \u00E9tendue de type sinus",
//                "<P>fx(<B><Font color=blue>x</Font></B>,<B><Font color=blue>y</Font></B>)=N.cos((2p+1)(<B><Font color=blue>x</Font></B>-x0).pi/2width)cos(q.pi/width.(<B><Font color=blue>y</Font></B>+height/2))</P>" +
//                "<P>fy(<B><Font color=blue>x</Font></B>,<B><Font color=blue>y</Font></B>)=N.sin((2p+1)(<B><Font color=blue>x</Font></B>-x0).pi/2width)sin(q.pi/width.(<B><Font color=blue>y</Font></B>+height/2))</P>");
//    }
//
//    private double n1;
//
//    public AreaSine() {
//        super(TYPE);
//    }
//    
//    public AreaSine(String name, String x, String y, String width, String height, String max_p, String max_q) {
//        super(TYPE, name, x, y, width, height, max_p, max_q);
//    }
//
//    @Override
//    public GpCell toGpCell() {
//        BoxModesPattern b=new BoxModesPattern(getFunctionMax());
//        b.setAxisIndependent(getInvariance());
//        return new GpCell(domain, 
//                getStructure().getDomain(),
//                null,
//                b,
//                getSymmetry(),
//                MeshAlgoRect.RECT_ALGO_LOW_RESOLUTION
//                ,getInvariance()
//                );
//    }
//
//    public DFunctionVector2D getFunction(int i) {
//        int p = (i / max_q);
//        int q = (i % max_q);
//        double coeff = (2 * p + 1) / 2.0;
//        return new DFunctionVector2D(new DCosCosFunctionXY(n1,
//
//                coeff * Math.PI / width,
//                -(x * coeff * Math.PI / width), //cos
//
//                q * Math.PI / height,
//                q * Math2.HALF_PI, //cos
//
//                domain),
//                new DCosCosFunctionXY(n1,
//
//                        coeff * Math.PI / width,
//                        -(x * coeff * Math.PI / width) - Math2.HALF_PI, //sin
//
//                        q * Math.PI / height,
//                        q * Math2.HALF_PI - Math2.HALF_PI, //sin
//
//                        domain));
//    }
//
//    public void recompile() {
//        super.recompile();
//        n1 = Math.sqrt(2 / width / height);
//    }
//}
