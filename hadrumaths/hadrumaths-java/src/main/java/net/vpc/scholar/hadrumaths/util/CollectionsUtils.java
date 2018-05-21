package net.vpc.scholar.hadrumaths.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by vpc on 8/15/14.
 */
public class CollectionsUtils {

    public static <A, B> List<B> convert(List<A> list, Converter<A, B> converter) {
        return new ImmutableConvertedList<A, B>(list, converter);
    }

    public static <T> List<T> filter(Collection<T> collection, CollectionFilter<T> filter) {
        ArrayList<T> ret = new ArrayList<T>();
        int i = 0;
        for (T t : collection) {
            if (filter.accept(t, i, collection)) {
                ret.add(t);
            }
            i++;
        }
        return ret;
    }


    public static <T> List<T> toList(Iterator<T> it) {
        List<T> all = new ArrayList<>();
        while (it.hasNext()) {
            all.add(it.next());
        }
        return all;
    }

    public static <T> List<T> toList(Iterable<T> it) {
        return toList(it.iterator());
    }
}
