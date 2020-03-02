package net.vpc.scholar.hadruwaves.mom;

import net.vpc.common.tson.*;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.HSerializable;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadruwaves.Material;

import java.util.Objects;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 20:45:41
 */
public final class BoxSpace implements HSerializable {
    private final BoxLimit limit;
    private final Material material;
    private final double width;

    public BoxSpace(BoxLimit limit, Material material, double width) {
        if (limit == null) {
            throw new IllegalArgumentException("Null Box Limit");
        }
        if (material == null) {
            throw new IllegalArgumentException("Null Material Limit");
        }
        this.material = material;
        this.limit = limit;
        this.width = width;
    }

    public Material getMaterial() {
        return material;
    }

    public BoxSpace setLimit(BoxLimit limit) {
        return new BoxSpace(limit, material, width);
    }

    public BoxSpace setWidth(double width) {
        return new BoxSpace(limit, material, width);
    }

    public BoxSpace setMaterial(Material material) {
        return new BoxSpace(limit, material, width);
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonFunctionBuilder h = Tson.function(limit.name());
        if (!limit.equals(BoxLimit.NOTHING) && !limit.equals(BoxLimit.MATCHED_LOAD)) {
            h.add(Tson.pair("width", context.elem(width)));
        }

        if (!limit.equals(BoxLimit.NOTHING)) {
            h.add(Tson.pair("material", context.elem(material)));
        }
        return h.build();
    }

    @Override
    public String toString() {
        return dump();
    }

    public BoxLimit getLimit() {
        return limit;
    }

    public double getWidth() {
        return width;
    }

    public Complex getEpsrc(double freq) {
        if (material.getElectricConductivity() == 0) {
            return Complex.of(material.getPermettivity() * Maths.EPS0);
        } else {
            return Complex.of(material.getPermettivity() * Maths.EPS0, material.getElectricConductivity() / (2 * Math.PI * freq));
        }
    }

    public double getEpsr() {
        return material.getPermettivity();
    }

    public double getElectricConductivity() {
        return material.getElectricConductivity();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoxSpace boxSpace = (BoxSpace) o;
        return Double.compare(boxSpace.width, width) == 0 &&
                boxSpace.material.equals(material) &&
                limit == boxSpace.limit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(limit, width, material);
    }
}
