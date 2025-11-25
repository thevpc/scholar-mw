package net.thevpc.scholar.hadruwaves.mom;


import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NUpletElementBuilder;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.HSerializable;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;
import net.thevpc.scholar.hadruwaves.Material;

import java.util.Objects;
import net.thevpc.scholar.hadruwaves.Boundary;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 20:45:41
 */
public final class BoxSpace implements HSerializable {
    private final Boundary limit;
    private final Material material;
    private final double width;

    public BoxSpace(Boundary limit, Material material, double width) {
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

    public static BoxSpace openCircuit(Material material, double width) {
        return new BoxSpace(Boundary.OPEN, material, width);
    }

    public static BoxSpace matchedLoad() {
        return matchedLoad(Material.VACUUM);
    }

    public static BoxSpace matchedLoad(Material material) {
        return new BoxSpace(Boundary.INFINITE, material, Double.POSITIVE_INFINITY);
    }

    public static BoxSpace shortCircuit(Material material, double width) {
        return new BoxSpace(Boundary.ELECTRIC, material, width);
    }

    public static BoxSpace nothing() {
        return new BoxSpace(Boundary.NOTHING, Material.VACUUM, 0);
    }

    public Material getMaterial() {
        return material;
    }

    public BoxSpace setLimit(Boundary limit) {
        return new BoxSpace(limit, material, width);
    }

    public BoxSpace setWidth(double width) {
        return new BoxSpace(limit, material, width);
    }

    public BoxSpace setMaterial(Material material) {
        return new BoxSpace(limit, material, width);
    }

    @Override
    public NElement toElement() {
        NUpletElementBuilder h = NElement.ofUpletBuilder().name(limit.name());
        if (!limit.equals(Boundary.NOTHING) && !limit.equals(Boundary.INFINITE)) {
            h.add(NElement.ofPair("width", NElementHelper.elem(width)));
        }

        if (!limit.equals(Boundary.NOTHING)) {
            h.add(NElement.ofPair("material", NElementHelper.elem(material)));
        }
        return h.build();
    }

    @Override
    public String toString() {
        return dump();
    }

    public Boundary getLimit() {
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
