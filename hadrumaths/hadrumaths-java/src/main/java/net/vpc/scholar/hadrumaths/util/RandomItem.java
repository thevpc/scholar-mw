package net.vpc.scholar.hadrumaths.util;

public abstract class RandomItem<T> {
    public double weigth = 1;

    public RandomItem(double weigth) {
        this.weigth = weigth <= 0 ? 1 : weigth;
    }

    public abstract T create();
}
