//package net.thevpc.scholar.tmwlib.mom.project.essai;
//
//import java.util.ArrayList;
//
//import net.thevpc.scholar.math.util.Configuration;
//import net.thevpc.scholar.math.functions.DomainXY;
//import net.thevpc.scholar.math.functions.FunctionFactory;
//import net.thevpc.scholar.math.functions.dfxy.DFunctionVector2D;
//import net.thevpc.scholar.math.functions.dfxy.DLinearFunctionXY;
//import net.thevpc.scholar.math.meshalgo.rect.GridPrecision;
//import net.thevpc.scholar.math.meshalgo.rect.MeshAlgoRect;
//import net.thevpc.scholar.tmwlib.mom.project.common.AreaManager;
//import net.thevpc.scholar.tmwlib.mom.project.common.MetalArea;
//import net.thevpc.scholar.tmwlib.mom.gptest.GpCell;
//import net.thevpc.scholar.tmwlib.mom.gptest.gpmesh.gppattern.GpPatternFactory;
//
///**
// * User: taha
// * Date: 3 juil. 2003
// * Time: 17:32:08
// */
//public class AreaEchelon2D extends MetalArea {
//    public static String TYPE = "Echelon2";
//
//    public static void install(){
//        AreaManager.register(AreaManager.METAL_CATEGORY, TYPE, AreaEchelon2D.class, "Echelon2 2D", "Fonction de type Echelon 2D");
//    }
//
//    private DFunctionVector2D[] fct;
//    private int gridX;
//    private int gridY;
//    private double value;
//
//    private String gridXExpression;
//    private String gridYExpression;
//    private String valueExpression;
//
//    @Override
//    public GpCell toGpCell() {
//        return new GpCell(
//                domain, 
//                getStructure().getDomain(),
//                null,
//                GpPatternFactory.ECHELON,
//                getSymmetry(),
//                new MeshAlgoRect(new GridPrecision(gridX, gridY))
//                ,getInvariance()
//                );
//    }
//
//    public AreaEchelon2D() {
//        super(TYPE);
//        gridXExpression = "1";
//        gridYExpression = "1";
//        valueExpression = "0";
//    }
//
//
//    @Override
//    public void load(Configuration conf, String key) {
//        super.load(conf, key);
//        gridXExpression = String.valueOf(conf.getObject(key + ".gridX", "1"));
//        gridYExpression = String.valueOf(conf.getObject(key + ".gridY", "1"));
//        valueExpression = String.valueOf(conf.getObject(key + ".value", "0"));
//    }
//    
//
//    public void recompile() {
//        super.recompile();
//        gridX = getContext().evaluateInt(gridXExpression);
//        gridY = getContext().evaluateInt(gridYExpression);
//        value = getContext().evaluateDouble(valueExpression);
//
//        if (gridX <= 0) {
//            gridX = 1;
//        }
//
//        if (gridY <= 0) {
//            gridY = 1;
//        }
//        if (value == 0) {
//            value = defaultValue();
//        }
//
//        ArrayList<DFunctionVector2D> arrayList = new ArrayList<DFunctionVector2D>();
//        for (int j = 0; j < gridY; j++) {
//            for (int i = 0; i < gridX; i++) {
//                arrayList.add(new DFunctionVector2D(new DLinearFunctionXY(
//                        0, 0,value, new DomainXY(x + (width / gridX) * i,
//                        y + (height / gridY) * j, x + (width / gridX) * (i + 1),
//                        y + (height / gridY) * (j + 1))),
//                        FunctionFactory.DZEROXY));
//            }
//        }
//
//        fct = (DFunctionVector2D[]) arrayList.toArray(new DFunctionVector2D[arrayList.size()]);
//    }
//
//    public AreaEchelon2D(String name, String crestValue, String gridX, String gridY, String x, String y, String width, String height) {
//        super(TYPE, name, x, y, width, height);
//
//        this.valueExpression = crestValue;
//
//        this.gridXExpression = gridX;
//        this.gridYExpression = gridY;
//    }
//
//    public double getValue() {
//        return value;
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
//    private double defaultValue() {
//        double crest = (x + width) / 2;
//        double crVal = (Math.sqrt(3.0 / (height) / (width / 2)) / (width / 2)) // a
//                * crest // crest
//                + ((Math.sqrt(3.0 / height / (width / 2)) * ((width / 2) - crest) / (width / 2))) // b
//                ;
//        return crVal / gridX / gridY;
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
//        return fct.length;
//    }
//
//    public void store(Configuration c, String key) {
//        super.store(c, key);
//        c.setString(key + ".value", valueExpression);
//        c.setString(key + ".gridX", gridXExpression);
//        c.setString(key + ".gridY", gridYExpression);
//    }
//
//    public boolean equals(Object other) {
//        if (super.equals(other)) {
//            AreaEchelon2D s = (AreaEchelon2D) other;
//            return value == s.value && gridX == s.gridX && gridY == s.gridY;
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
//    public String getValueExpression() {
//        return valueExpression;
//    }
//}
