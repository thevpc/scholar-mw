package net.vpc.scholar.hadruplot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public abstract class PlotValueArrayType extends AbstractPlotValueType {
    private PlotValueType rootComponentType;
    private PlotValueType componentType;
    private int dimension;

    public PlotValueArrayType(PlotValueType rootComponentType, int dimension) {
        super(generateName(rootComponentType, dimension));
        if (dimension <= 0 || rootComponentType instanceof PlotValueArrayType) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        this.rootComponentType = rootComponentType;
        this.dimension = dimension;
    }

    private static String generateName(PlotValueType rootComponentType, int dimension) {
        StringBuilder sb = new StringBuilder(rootComponentType.getName().length()+dimension*2);
        sb.append(rootComponentType.getName());
        for (int i = 0; i < dimension; i++) {
            sb.append("[]");
        }
        return sb.toString();
    }

    public int getDimension() {
        return dimension;
    }

    public PlotValueType getRootComponentType() {
        return rootComponentType;
    }

    public PlotValueType getComponentType() {
        if (componentType == null) {
            PlotValueType rootComponentType = getRootComponentType();
            int dimension = getDimension();
            if (dimension == 1) {
                componentType = rootComponentType;
            } else {
                componentType = toDimension(dimension - 1);
            }
        }
        return componentType;
    }

    public abstract Object get(Object obj, int index);

    public abstract int length(Object obj);

    public abstract Object[] toArray(Object obj);

    public abstract Object toArray(Object obj, Class type);

    public abstract PlotValueType toDimension(int dim);

    @Override
    public Object getValue(Object o) {
        return toArray(o);
    }

    protected Object toArrayDefault(Object obj, Class type) {
        if(!type.isArray()){
            throw new IllegalArgumentException("Not an array");
        }
        PlotValueType pft = getComponentType();
        if (obj == null) {
            throw new NullPointerException();
        }
        if (obj.getClass().isArray()) {
            int len = Array.getLength(obj);
            Object oo = Array.newInstance(type, len);
            for (int i = 0; i < len; i++) {
                Array.set(oo, i, pft.getValue(Array.get(obj, i)));
            }
            return oo;
        }
        if (obj instanceof Iterable) {
            List<Object> all = new ArrayList<>();
            Object t=Array.newInstance(type.getComponentType(),0);
            for (Object o : ((Iterable) obj)) {
                all.add(pft.toValue(o, type.getComponentType()));
            }
            return all.toArray((Object[]) t);
        }
        return null;
    }

    @Override
    public Object toValue(Object value, Class cls) {
        if(value==null){
            return null;
        }
        if(cls.isArray()){
            return toArray(value,cls);
        }
        return super.toValue(value, cls);
    }
}
