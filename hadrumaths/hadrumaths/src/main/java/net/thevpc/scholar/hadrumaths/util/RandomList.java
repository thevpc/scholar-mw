package net.thevpc.scholar.hadrumaths.util;

import net.thevpc.common.util.ObjectFactory;
import net.thevpc.scholar.hadrumaths.Maths;

import java.util.ArrayList;
import java.util.List;

public class RandomList<T> {
    double[] indexes = null;
    double x = 0;
    private final List<RandomItem<T>> all = new ArrayList<>();

    public RandomList<T> addIf(boolean cond, double weigth, ObjectFactory<T> a) {
        if (cond) {
            add(weigth, a);
        }
        return this;
    }

    public RandomList<T> add(double weigth, ObjectFactory<T> a) {
        return add(new RandomItem(weigth) {
            @Override
            public Object create() {
                return a.create();
            }
        });
    }

    public RandomList<T> add(RandomItem<T> a) {
        all.add(a);
        indexes = null;
        return this;
    }

    public RandomList<T> addIf(boolean cond, ObjectFactory<T> a) {
        if (cond) {
            add(a);
        }
        return this;
    }

    public RandomList<T> add(ObjectFactory<T> a) {
        return add(1, a);
    }

    public T random() {
        double v = Maths.randomDouble(x);
        double[] indexes = getIndexes();
        for (int i = 0; i < indexes.length; i++) {
            if (v < indexes[i]) {
                return all.get(i).create();
            }
        }
        return all.get(all.size() - 1).create();
    }

    public double[] getIndexes() {
        if (indexes == null) {
            x = 0;
            indexes = new double[all.size()];
            for (int i = 0; i < all.size(); i++) {
                indexes[i] = x;
                x += all.get(i).weigth;
            }
        }
        return indexes;
    }
}
