package net.vpc.scholar.hadruplot.model.value;


import java.util.*;

public class PlotValueTypeFactory {
    private Map<String, PlotValueType> registry = new LinkedHashMap<>();
    private Map<String, List<PlotValueTypeConverter>> conversions = new LinkedHashMap<>();

    public interface PlotValueTypeConverter {
        String getSourceType();

        String getTargetType();
    }

    public abstract static class AbstractPlotValueTypeConverter implements PlotValueTypeConverter {
        private String source;
        private String target;

        public AbstractPlotValueTypeConverter(String source, String target) {
            this.source = source;
            this.target = target;
        }

        public String getSourceType() {
            return source;
        }

        public String getTargetType() {
            return target;
        }
    }

    public PlotValueTypeFactory() {
        registerType(PlotValueDoubleType.INSTANCE);
        registerType(PlotValueFileType.INSTANCE);
        registerType(PlotValueNullType.INSTANCE);
        registerType(PlotValueObjectType.INSTANCE);
        registerType(PlotDataPlotLinesType.INSTANCE);
        registerType(PlotValueComponentType.INSTANCE);
        registerType(PlotValueModelType.INSTANCE);
    }

    public void registerConverter(PlotValueTypeConverter converter) {
        String k = converter.getSourceType();
        List<PlotValueTypeConverter> list = conversions.get(k);
        if (list == null) {
            list = new ArrayList<>();
            conversions.put(k, list);
        }
        list.add(converter);
    }

    public void registerType(PlotValueType type) {
        registry.put(type.getName(), type);
    }

    public PlotValueType findType(String name) {
        PlotValueType found = registry.get(name);
        if (found != null) {
            return found;
        }
        if (name.endsWith("[]")) {
            PlotValueType dataType = findType(name.substring(0,name.length() - 2));
            if (dataType == null) {
                return null;
            }
            int dim = 0;
            if (dataType instanceof PlotValueArrayType) {
                dim = ((PlotValueArrayType) dataType).getDimension();
                found = new DefaultPlotValueArrayType(((PlotValueArrayType) dataType).getRootComponentType(), dim + 1);
            } else {
                found = new DefaultPlotValueArrayType(dataType, 1);
            }
            registry.put(name, found);
            return found;
        }
        return null;
    }

    public PlotValueType getType(String name) {
        PlotValueType t = findType(name);
        if (t == null) {
            throw new IllegalArgumentException("type not found " + name);
        }
        return t;
    }


    public PlotValueType resolveUmbrellaType(PlotValueType type1o, PlotValueType type2o) {
        PlotValueType n = resolveUmbrellaTypeBase(type1o, type2o);
        if (n != null) {
            return n;
        }
        n = resolveUmbrellaTypeNonArr(type1o, type2o, new HashMap<String, PlotValueType>());
        if (n != null) {
            return n;
        }
        return getType("object");
    }

    private PlotValueType resolveUmbrellaTypeBase(PlotValueType type1o, PlotValueType type2o) {
        String type1 = type1o.getName();
        String type2 = type2o.getName();
        if (type1.equals(type2)) {
            return type1o;
        }
        if (type1.equals("object") || type2.equals("object")) {
            return getType("object");
        }
        if (type1.equals("null")) {
            return type2o;
        }
        if (type2.equals("null")) {
            return type1o;
        }
        if (type1o instanceof PlotValueArrayType && type2o instanceof PlotValueArrayType) {
            PlotValueArrayType t1 = (PlotValueArrayType) type1o;
            PlotValueArrayType t2 = (PlotValueArrayType) type2o;
            int d1 = t1.getDimension();
            int d2 = t2.getDimension();
            if (d1 == d2) {
                PlotValueType i = resolveUmbrellaType(t1.toDimension(d1 - 1), t2.toDimension(d1 - 1));
                if (i instanceof PlotValueArrayType) {
                    return ((PlotValueArrayType) i).toDimension(d1);
                } else {
                    return new DefaultPlotValueArrayType(
                            i, d1
                    );
                }
            } else {
                return getType("object");
            }
        } else if (type1o instanceof PlotValueArrayType || type2o instanceof PlotValueArrayType) {
            return getType("object");
        }
        return null;
    }

//    private PlotValueType resolveUmbrellaType(PlotValueType type1o, PlotValueType type2o) {
//        String type1 = type1o.getName();
//        String type2 = type2o.getName();
//        if (type1.equals(type2)) {
//            return type1o;
//        }
//        if (type1.equals("object") || type2.equals("object")) {
//            return getType("object");
//        }
//        if (type1.equals("null")) {
//            return getType("object");
//        }
//        if (type2.equals("null")) {
//            return type1o;
//        }
//        if (type1o instanceof PlotValueArrayType && type2o instanceof PlotValueArrayType) {
//            PlotValueArrayType t1 = (PlotValueArrayType) type1o;
//            PlotValueArrayType t2 = (PlotValueArrayType) type2o;
//            int d1 = t1.getDimension();
//            int d2 = t2.getDimension();
//            if (d1 == d2) {
//                PlotValueType i = resolveUmbrellaType(t1.toDimension(d1 - 1), t2.toDimension(d1 - 1));
//                if (i instanceof PlotValueArrayType) {
//                    return ((PlotValueArrayType) i).toDimension(d1);
//                } else {
//                    return new DefaultPlotValueArrayType(
//                            i, d1
//                    );
//                }
//            } else {
//                return getType("object");
//            }
//        } else if (type1o instanceof PlotValueArrayType || type2o instanceof PlotValueArrayType) {
//            return getType("object");
//        }
//
//        if (PlotUtils.isSameCouple(type1, type2, "expr[][]", "complex[][]")) {
//            return getType("expr[][]");
//        }
//        if (PlotUtils.isSameCouple(type1, type2, "complex[][]", "number[][]")) {
//            return getType("complex[][]");
//        }
//        if (PlotUtils.isSameCouple(type1, type2, "expr[]", "complex[]")) {
//            return getType("expr[]");
//        }
//        if (PlotUtils.isSameCouple(type1, type2, "complex[]", "number[]")) {
//            return getType("complex[]");
//        }
//
//        if (PlotUtils.isSameCouple(type1, type2, "point[]", "null[]")) {
//            return getType("point[]");
//        }
//        if (PlotUtils.isSameCouple(type1, type2, "expr[]", "null[]")) {
//            return getType("expr[]");
//        }
//        if (PlotUtils.isSameCouple(type1, type2, "complex[]", "null[]")) {
//            return getType("complex[]");
//        }
//        if (PlotUtils.isSameCouple(type1, type2, "number[]", "null[]")) {
//            return getType("number[]");
//        }
//        if (PlotUtils.isSameCouple(type1, type2, "expr[][]", "null[][]")) {
//            return getType("expr[][]");
//        }
//        if (PlotUtils.isSameCouple(type1, type2, "complex[][]", "null[][]")) {
//            return getType("complex[][]");
//        }
//        if (PlotUtils.isSameCouple(type1, type2, "number[][]", "null[][]")) {
//            return getType("number[][]");
//        }
//        return getType("object");
//    }

    private PlotValueType resolveUmbrellaTypeNonArr(PlotValueType type1o, PlotValueType type2o, HashMap<String, PlotValueType> visited) {
        String k = type1o.getName() + "::" + type2o.getName();
        if (visited.containsKey(k)) {
            PlotValueType p = visited.get(k);
            if (p != null) {
                return p;
            }
            throw new IllegalArgumentException("Already checked");
        }
        visited.put(k, null);
        List<PlotValueTypeConverter> c1 = conversions.get(type1o.getName());
        List<PlotValueTypeConverter> c2 = conversions.get(type2o.getName());
        List<PlotValueType> t1 = new ArrayList<>();
        List<PlotValueType> t2 = new ArrayList<>();
        if (c1 != null) {
            for (PlotValueTypeConverter plotValueTypeConverter : c1) {
                PlotValueType xt = getType(plotValueTypeConverter.getTargetType());
                PlotValueType n = resolveUmbrellaTypeBase(xt, type2o);
                if (n != null) {
                    visited.put(k, n);
                    return n;
                }
                t1.add(xt);
            }
        }
        if (c2 != null) {
            for (PlotValueTypeConverter plotValueTypeConverter : c2) {
                PlotValueType xt = getType(plotValueTypeConverter.getTargetType());
                PlotValueType n = resolveUmbrellaTypeBase(type1o, xt);
                if (n != null) {
                    visited.put(k, n);
                    return n;
                }
                t2.add(xt);
            }
        }
        for (PlotValueType tt1 : t1) {
            for (PlotValueType tt2 : t2) {
                PlotValueType n = resolveUmbrellaTypeNonArr(tt1, tt2, visited);
                if (n != null) {
                    visited.put(k, n);
                    return n;
                }
            }
        }
        return null;
    }

}
