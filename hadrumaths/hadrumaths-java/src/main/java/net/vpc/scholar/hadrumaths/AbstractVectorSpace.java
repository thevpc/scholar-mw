package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.util.Converter;

import java.util.List;

public abstract class AbstractVectorSpace<T> implements VectorSpace<T> {
    @Override
    public <R> Converter<R, T> getConverterFrom(Class<R> t) {
        return Maths.Config.getConverter(t, getItemType().getTypeClass());
    }

    @Override
    public <R> Converter<T, R> getConverterTo(Class<R> t) {
        return Maths.Config.getConverter(getItemType().getTypeClass(), t);
    }

    @Override
    public <R> Converter<R, T> getConverterFrom(TypeReference<R> t) {
        return Maths.Config.getConverter(t, getItemType());
    }

    @Override
    public <R> Converter<T, R> getConverterTo(TypeReference<R> t) {
        return Maths.Config.getConverter(getItemType(), t);
    }

//    @Override
//    public <R> Converter<R, T> getConverterFrom(TypeReference<R> t) {
//        return Maths.Config.getConverter(t,getItemType());
//    }
//
//    @Override
//    public <R> Converter<T, R> getConverterTo(TypeReference<R> t) {
//        return Maths.Config.getConverter(getItemType(),t);
//    }

    @Override
    public <R> R convertTo(T value, Class<R> t) {
        return getConverterTo(t).convert(value);
    }

    @Override
    public <R> T convertFrom(R value, Class<R> t) {
        return getConverterFrom(t).convert(value);
    }

    @Override
    public T addAll(List<T> b) {
        RepeatableOp<T> o = addRepeatableOp();
        for (T t : b) {
            o.append(t);
        }
        return o.eval();
    }

    @Override
    public T mulAll(List<T> b) {
        RepeatableOp<T> o = mulRepeatableOp();
        for (T t : b) {
            o.append(t);
        }
        return o.eval();
    }
}
