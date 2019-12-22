package net.vpc.scholar.hadruplot;

import java.lang.reflect.Array;
import java.util.List;

public class DefaultPlotValueArrayType extends PlotValueArrayType {
    public DefaultPlotValueArrayType(PlotValueType rootComponentType, int dimension) {
        super(rootComponentType, dimension);
    }

    @Override
    public Object get(Object obj, int index) {
        if (obj == null) {
            throw new NullPointerException();
        }
        if (obj.getClass().isArray()) {
            return Array.get(obj, index);
        }
        if (obj instanceof List) {
            return ((List) obj).get(index);
        }
        throw new ClassCastException(obj.getClass().getName() + " is not castable to array or list");
    }

    @Override
    public int length(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj);
        }
        if (obj instanceof List) {
            return ((List) obj).size();
        }
        throw new ClassCastException(obj.getClass().getName() + " is not castable to array or list");
    }

    @Override
    public Object[] toArray(Object obj) {
        int[] dims = new int[getDimension()];
        Class<?> ff = Array.newInstance(Object.class, dims).getClass();
        return (Object[]) toArray(obj, ff);
    }


    public Object toArray(Object obj, Class type) {
        Object o = toArrayDefault(obj, type);
        if (o == null) {
            throw new ClassCastException(obj.getClass().getName() + " is not castable to array or list");
        }
        return o;
    }


    @Override
    public PlotValueType toDimension(int dim) {
        if (dim == this.getDimension()) {
            return this;
        }
        if (dim == 0) {
            return getRootComponentType();
        }
        return new DefaultPlotValueArrayType(getRootComponentType(), dim);
    }


}
