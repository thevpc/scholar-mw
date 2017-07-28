//package net.vpc.scholar.tmwlib.mom.project.essai;
//
//
//import net.vpc.scholar.math.functions.dfxy.Rooftop2DFunctionXY.RooftopType;
//import net.vpc.scholar.math.functions.dfxy.Rooftop2DFunctionXY;
//import net.vpc.scholar.math.meshalgo.rect.GridPrecision;
//import net.vpc.scholar.math.meshalgo.rect.MeshAlgoRect;
//import net.vpc.scholar.tmwlib.mom.project.common.AreaManager;
//import net.vpc.scholar.tmwlib.mom.project.common.MetalArea;
//import net.vpc.scholar.tmwlib.mom.gptest.GpCell;
//import net.vpc.scholar.tmwlib.mom.gptest.gpmesh.gppattern.GpPattern;
//import net.vpc.scholar.tmwlib.mom.gptest.gpmesh.gppattern.Rooftop2DPattern;
//import net.vpc.scholar.tmwlib.mom.gptest.gpmesh.gppattern.Rooftop2DSimplePattern;
//import net.vpc.scholar.math.util.Configuration;
//
///**
// * User: taha
// * Date: 3 juil. 2003
// * Time: 17:32:08
// */
//public class AreaRooftop2D extends MetalArea {
//    public static String TYPE = "RoofTop2";
//
//    public static void install(){
//        AreaManager.register(AreaManager.METAL_CATEGORY, TYPE, AreaRooftop2D.class, "Rooftop 2D", "Fonction de type Rooftop 2D\nEn cours de d\u00E9veloppement\ncontient les remarques de Dr T.AGUILI")
//                .setDefaultAreaType();
//    }
//
//    private boolean cross;
//    private int gridX;
//    private int gridY;
//    private Rooftop2DFunctionXY.RooftopType rooftopType=Rooftop2DFunctionXY.RooftopType.FULL;
//
//    private String gridXExpression;
//    private String gridYExpression;
//
//    @Override
//    public GpCell toGpCell() {
//        int gridXX=(int)(Math.log(gridX)/Math.log(2));
//        int gridYY=(int)(Math.log(gridY)/Math.log(2));
//        GpPattern gpp=null;
//        if(gridXX==0 && gridYY==0 && cross==false && !Rooftop2DFunctionXY.RooftopType.FULL.equals(rooftopType)){
//            gpp=new Rooftop2DSimplePattern(rooftopType);
//        }else if(cross){
//            gpp=new Rooftop2DPattern(true, true);
//        }else{
//            gpp=new Rooftop2DPattern(false, false);
//        }
//        return new GpCell(
//                domain, 
//                getStructure().getDomain(),
//                null,
//                gpp
////                    GpPatternFactory.ROOFTOP_ATTACHXY:GpPatternFactory.ROOFTOP
//                    ,
//                getSymmetry(),
//                new MeshAlgoRect(new GridPrecision(gridXX,gridXX, gridYY,gridYY))
//                ,getInvariance()
//                );
//    }
//
//
//    
//    public AreaRooftop2D() {
//        super(TYPE);
//        cross = false;
//        gridXExpression = "1";
//        gridYExpression = "1";
//    }
//
//    @Override
//    public void load(Configuration conf, String key) {
//        super.load(conf, key);
//        cross = conf.getBoolean(key + ".cross", false);
//        gridXExpression = String.valueOf(conf.getObject(key + ".gridX", "1"));
//        gridYExpression = String.valueOf(conf.getObject(key + ".gridY", "1"));
//        rooftopType = Rooftop2DFunctionXY.RooftopType.valueOf(conf.getString(key + ".rooftopType", "FULL"));
//    }
//    
//
//    @Override
//    public void recompile() {
//        super.recompile();
//        gridX = getContext().evaluateInt(gridXExpression);
//        gridY = getContext().evaluateInt(gridYExpression);
//        if (gridX < 1) {
//            gridX = 1;
//        }
//
//        if (gridY < 1) {
//            gridY = 1;
//        }
//    }
//
//    public AreaRooftop2D(String name, double gridX, double gridY, boolean crossAttache, double x, double y, double width, double height) {
//        this(name, String.valueOf(gridX), String.valueOf(gridY), crossAttache, String.valueOf(x), String.valueOf(y), String.valueOf(width), String.valueOf(height));
//    }
//
//    public AreaRooftop2D(String name, String gridX, String gridY, boolean crossAttache, String x, String y, String width, String height) {
//        super(TYPE, name, x, y, width, height);
//
//        this.gridXExpression = gridX;
//        this.gridYExpression = gridY;
//        this.cross = crossAttache;
//    }
//
//    public int getGridX() {
//        return gridX;
//    }
//
//    public int getGridY() {
//        return gridY;
//    }
//
//    @Override
//    public int getFunctionMax() {
//        return 0;
//    }
//
//    @Override
//    public void store(Configuration c, String key) {
//        super.store(c, key);
//        c.setString(key + ".gridX", gridXExpression);
//        c.setString(key + ".gridY", gridYExpression);
//        c.setBoolean(key + ".cross", cross);
//    }
//
//    public boolean isCross() {
//        return cross;
//    }
//
//    @Override
//    public boolean equals(Object other) {
//        if (super.equals(other)) {
//            AreaRooftop2D s = (AreaRooftop2D) other;
//            return gridX == s.gridX && gridY == s.gridY && cross == s.cross && rooftopType.equals(s.rooftopType);
//        } else {
//            return false;
//        }
//    }
//
//    public String getGridXExpression() {
//        return gridXExpression;
//    }
//
//    public String getGridYExpression() {
//        return gridYExpression;
//    }
//
//    public RooftopType getRooftopType() {
//        return rooftopType;
//    }
//
//    public void setRooftopType(RooftopType type) {
//        this.rooftopType = type;
//    }
//    
//}
