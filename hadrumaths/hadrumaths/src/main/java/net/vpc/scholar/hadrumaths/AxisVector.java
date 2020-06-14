package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

public class AxisVector<T> {
    private Coordinates coordinates;
    private Vector<T> model;

    public AxisVector(Coordinates coordinates, TypeName<T> type, T c1, T c2, T c3) {
        this.coordinates = coordinates;
        this.model = new ArrayVector<T>(type, false, 3);
        this.model.append(c1);
        this.model.append(c2);
        this.model.append(c3);
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public T get(int index) {
        return model.get(index);
    }

    public T get(Axis a) {
        a.castCoordinates(coordinates);
        return model.get(a.index());
    }
}
