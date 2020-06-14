package net.vpc.scholar.hadrumaths.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vpc on 3/15/15.
 */
public abstract class Const<T> {
    private static final Map<Class, Registry> registries = new HashMap<Class, Registry>();
    int index;
    private final String name;
    private final Class type;
    private Registry list;

    protected Const(String name, Class<T> type) {
        this.name = name;
        this.type = type;
        Registry r = registries.get(type);
        if (r == null) {
            r = new Registry(type);
            registries.put(type, r);
        }
        r.add(this);
    }

    public String name() {
        return name;
    }

    public int ordinal() {
        return index;
    }

    public T[] values() {
        return list.values.toArray((T[]) Array.newInstance(type, list.values.size()));
    }

    private static class Registry {
        private final Class type;
        private final List<Const> values = new ArrayList<Const>();

        public Registry(Class type) {
            this.type = type;
        }

        void add(Const v) {
            v.index = values.size();
            v.list = this;
            values.add(v);
        }
    }
}
