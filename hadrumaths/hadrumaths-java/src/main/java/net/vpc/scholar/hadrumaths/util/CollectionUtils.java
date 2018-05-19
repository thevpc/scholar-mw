package net.vpc.scholar.hadrumaths.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class CollectionUtils {
    private CollectionUtils() {
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
