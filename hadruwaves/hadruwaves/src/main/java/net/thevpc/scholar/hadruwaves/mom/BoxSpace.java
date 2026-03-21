package net.thevpc.scholar.hadruwaves.mom;


import net.thevpc.nuts.elem.*;
import net.thevpc.nuts.log.NLog;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.util.NNameFormat;
import net.thevpc.nuts.util.NOptional;
import net.thevpc.nuts.util.NStringUtils;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.HSerializable;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;
import net.thevpc.scholar.hadruwaves.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import net.thevpc.scholar.hadruwaves.Boundary;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 20:45:41
 */
public final class BoxSpace implements HSerializable {
    private final Boundary limit;
    private final Material material;
    private final double width;

    public static NOptional<BoxSpace> parse(NElement value, Function<NElement, NElement> resolver) {
        boolean err = false;
        if (value == null || value.isNull()) {
            return NOptional.<BoxSpace>ofNamedEmpty("BoxSpace").withDefault(BoxSpace::nothing);
        }
        Function<NElement, NElement> resolver2 = e -> {
            if (resolver != null) {
                NElement f = resolver.apply(e);
                if (f != null) return f;
            }
            return e;
        };
        if (value.isAnyStringOrName()) {
            switch (NNameFormat.LOWER_KEBAB_CASE.format(value.asStringValue().orElse(""))) {
                case "matched-load":
                case "matched": {
                    return NOptional.of(BoxSpace.matchedLoad());
                }
                case "nothing": {
                    return NOptional.of(BoxSpace.nothing());
                }
                case "short":
                case "short-circuit": {
                    return NOptional.of(BoxSpace.shortCircuit(Material.PEC, 0.001));
                }
                case "open":
                case "open-circuit": {
                    return NOptional.of(BoxSpace.openCircuit(Material.VACUUM, 1));
                }
            }
        }
        if (value.isListContainer()) {
            Boundary limit = null;
            Material material = null;
            double width = 0;
            List<NElement> children = value.asListContainer().get().children();
            String name = null;
            if (value.isNamed()) {
                name = value.asNamed().get().name().orNull();
            }
            List<NPairElement> toDelegateToMaterial = new ArrayList<>();
            for (NElement child : children) {
                if (child.isNamedPair()) {
                    NPairElement p = child.asPair().get();
                    NElement value2 = resolver2.apply(p.value());
                    switch (NNameFormat.LOWER_KEBAB_CASE.format(p.key().asStringValue().orElse(""))) {
                        case "limit":
                        case "boundary": {
                            NOptional<Boundary> v = Boundary.parse(value2);
                            if (!v.isPresent()) {
                                err = true;
                            }
                            limit = v.orDefault();
                            break;
                        }
                        case "material": {
                            NOptional<Material> u = Material.parse(value2, resolver2);
                            if (!u.isPresent()) {
                                err = true;
                            }
                            material = u.orDefault();
                            break;
                        }
                        case "ε":
                        case "εr":
                        case "permittivity":
                        case "μ":
                        case "μr":
                        case "permeability":
                        case "σ":
                        case "conductivity":
                        case "electricConductivity": {
                            toDelegateToMaterial.add(p);
                            break;
                        }
                        case "width": {
                            NOptional<NNumberElement> n = value2.asNumber();
                            if (n.isPresent()) {
                                width = n.get().numberValue().doubleValue();
                                if (width < 0) {
                                    NLog.ofScoped(BoxSpace.class).log(NMsg.ofC("'width' should be a non negative number. found %s", p).asError());
                                    err = true;
                                    width = 0;
                                }
                            } else {
                                err = true;
                                width = 0;
                                NLog.ofScoped(BoxSpace.class).log(NMsg.ofC("'width' should be a number. found %s", p).asError());
                            }
                            break;
                        }
                        default: {
                            err = true;
                            NLog.ofScoped(BoxSpace.class).log(NMsg.ofC("invalid property %s", p).asError());
                        }
                    }
                }
            }
            if(material == null && !toDelegateToMaterial.isEmpty()) {
                NObjectElement ee = NElement.ofObjectBuilder().addAll(toDelegateToMaterial.toArray(new NElement[0])).build();
                NOptional<Material> u = Material.parse(ee, resolver2);
                if (!u.isPresent()) {
                    err = true;
                }
                material = u.orDefault();
            }
            switch (NNameFormat.LOWER_KEBAB_CASE.format(NStringUtils.trim(name))) {
                case "matched-load":
                case "matched": {
                    if (material == null) {
                        material = Material.VACUUM;
                    }
                    BoxSpace n = BoxSpace.matchedLoad(material);
                    if (err) {
                        NOptional.ofError(NMsg.ofC("'box space' should be an object. found %s", value).asError()).withDefault(n);
                    } else {
                        return NOptional.of(n);
                    }
                }
                case "open-circuit":
                case "open": {
                    if (material == null) {
                        material = Material.VACUUM;
                    }
                    BoxSpace n = BoxSpace.openCircuit(material, width);
                    if (err) {
                        NOptional.ofError(NMsg.ofC("'box space' should be an object. found %s", value).asError()).withDefault(n);
                    } else {
                        return NOptional.of(n);
                    }
                }
                case "short-circuit":
                case "short": {
                    if (material == null) {
                        material = Material.VACUUM;
                    }
                    BoxSpace n = BoxSpace.shortCircuit(material, width);
                    if (err) {
                        NOptional.ofError(NMsg.ofC("'box space' should be an object. found %s", value).asError()).withDefault(n);
                    } else {
                        return NOptional.of(n);
                    }
                }
                case "nothing": {
                    BoxSpace n = BoxSpace.nothing();
                    if (err) {
                        NOptional.ofError(NMsg.ofC("'box space' should be an object. found %s", value).asError()).withDefault(n);
                    } else {
                        return NOptional.of(n);
                    }
                }
            }
            if (limit == null) {
                limit = Boundary.NOTHING;
            }
            if (material == null) {
                material = Material.VACUUM;
            }
            BoxSpace n = new BoxSpace(
                    limit, material, width
            );
            if (err) {
                return NOptional.<BoxSpace>ofError(NMsg.ofC("'box space' should be an object. found %s", value).asError()).withDefault(n);
            } else {
                return NOptional.of(n);
            }
        } else {
            return NOptional.<BoxSpace>ofError(NMsg.ofC("'box space' should be an object. found %s", value).asError()).withDefault(BoxSpace::nothing);
        }
    }

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
            return Complex.of(material.getPermittivity() * Maths.EPS0);
        } else {
            return Complex.of(material.getPermittivity() * Maths.EPS0, material.getElectricConductivity() / (2 * Math.PI * freq));
        }
    }

    public double getEpsr() {
        return material.getPermittivity();
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
