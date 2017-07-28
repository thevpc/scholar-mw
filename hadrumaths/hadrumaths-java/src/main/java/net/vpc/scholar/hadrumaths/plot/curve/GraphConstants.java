package net.vpc.scholar.hadrumaths.plot.curve;

public class GraphConstants {

    //In parameter type in curves
    public static final int IN_DOUBLES = 0;
    public static final int IN_STRINGS = 1;
    public static final int IN_BOTH = 1;
    //bar style
    public static final int SOLID = 0;
    public static final int SOLID_SHADOW = 1;
    public static final int BOX_3D = 2;
    //Groupement style
    public static final int GROUPED_X_AXIS = 0;
    public static final int GROUPED_Y_AXIS = 1;
    public static final int GROUPED_Z_AXIS = 2;
    //Graph type
    public static final int CURVE_GRAPH = 0;
    public static final int PIE_GRAPH = 1;
    public static final int B3D_X_AXIS_GRAPH = 2;
    public static final int B3D_Y_AXIS_GRAPH = 3;
    public static final int B3D_Z_AXIS_GRAPH = 4;
    public static final int SOLID_X_AXIS_GRAPH = 5;
    public static final int SOLID_Y_AXIS_GRAPH = 6;
    public static final int SOLID_SHADOW_X_AXIS_GRAPH = 7;
    public static final int SOLID_SHADOW_Y_AXIS_GRAPH = 8;

//    public static HashMap getAllColumns(ExportedDataModel model) {
//        HashMap map = new HashMap();
//        for (int i = 0; i < model.getColumnCount(); i++) {
//            map.put(new Integer(i), model.getColumnName(i));
//        }
//        return map;
//    }
//
//    public static HashMap getNumberColumns(ExportedDataModel model) {
//        HashMap map = new HashMap();
//        for (int i = 0; i < model.getColumnCount(); i++) {
//            Class c = model.getColumnClass(i);
//            if (NumberDataType.NumberView.class.isAssignableFrom(c)
//                    || Number.class.isAssignableFrom(c)
//                    || c.equals(int.class)
//                    || c.equals(long.class)
//                    || c.equals(double.class)
//                    || c.equals(float.class)
//                    || c.equals(byte.class)
//                    || c.equals(short.class)
//            ) {
//                map.put(new Integer(i), model.getColumnName(i));
//            }
//        }
//        return map;
//    }

}
