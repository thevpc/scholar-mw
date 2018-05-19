package net.vpc.scholar.hadrumaths.util;

import java.util.AbstractList;
import java.util.List;

/**
 * Created by vpc on 8/15/14.
 */
public class ImmutableConvertedList<A, B> extends AbstractList<B> {
    private List<A> base;
    private Converter<A, B> converter;

    public ImmutableConvertedList(List<A> base, Converter<A, B> converter) {
        this.base = base;
        this.converter = converter;
    }

    @Override
    public int size() {
        return base.size();
    }


    @Override
    public B get(int index) {
        return converter.convert(base.get(index));
    }

    @Override
    public B set(int index, B element) {
        return super.set(index, element);
    }
}
