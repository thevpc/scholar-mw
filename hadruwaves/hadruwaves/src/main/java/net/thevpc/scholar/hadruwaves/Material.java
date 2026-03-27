package net.thevpc.scholar.hadruwaves;

import net.thevpc.nuts.elem.*;
import net.thevpc.nuts.log.NLog;
import net.thevpc.nuts.math.NDoubleComplex;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.util.NBlankable;
import net.thevpc.nuts.util.NNameFormat;
import net.thevpc.nuts.util.NOptional;
import net.thevpc.nuts.util.NStringUtils;
import net.thevpc.scholar.hadrumaths.Complex;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.function.Function;

public class Material implements NToElement {

    public static final Material VACUUM = new Material("Vacuum", 1, 1, 0);
    public static final Material PEC = new Material("PEC", 1, 1, Double.POSITIVE_INFINITY);
    private final String name;
    private final double permittivity;
    private final double permeability;
    private final double electricConductivity;

    public static NOptional<Material> parse(NElement value, Function<NElement, NElement> resolver) {
        if (value == null || value.isNull()) {
            return NOptional.<Material>ofNamedEmpty("material").withDefaultOptional(() -> NOptional.of(Material.VACUUM));
        }
        Function<NElement, NElement> resolver2 = e -> {
            if (resolver != null) {
                NElement f = resolver.apply(e);
                if (f != null) return f;
            }
            return e;
        };
        boolean err = false;
        if (value.isAnyStringOrName()) {
            switch (NNameFormat.LOWER_KEBAB_CASE.format(value.asStringValue().orElse(""))) {
                case "pec":
                    return NOptional.of(Material.PEC);
                case "vacuum":
                    return NOptional.of(Material.VACUUM);
                default: {
                    NLog.ofScoped(Material.class).log(NMsg.ofC("unknown material %s", value).asError());
                    return NOptional.<Material>ofNamedEmpty(NMsg.ofC("unknown material %s", value).asError())
                            .withDefaultOptional(() -> NOptional.of(Material.VACUUM));
                }
            }
        }
        if (value.isListContainer()) {
            NListContainerElement o = value.asListContainer().get();
            String name = value.isNamed() ? o.asNamed().get().name().orNull() : null;
            double permittivity = 1;
            double permeability = 1;
            Complex electricConductivity = Complex.ZERO;
            for (NElement child : o.children()) {
                if (child.isNamedPair()) {
                    NPairElement p = child.asPair().get();
                    NElement value2 = resolver2.apply(p.value());
                    switch (NNameFormat.LOWER_KEBAB_CASE.format(p.key().asStringValue().orElse(""))) {
                        case "name": {
                            String s = value2.asStringValue().orNull();
                            if (s != null) {
                                name = s;
                            }
                            break;
                        }
                        case "ε":
                        case "εr":
                        case "permittivity": {
                            if (value2.isName()) {
                                String s = value2.asStringValue().get();
                                switch (NNameFormat.LOWER_KEBAB_CASE.format(s)) {
                                    case "∞":
                                    case "infinity":
                                    case "positive-infinity": {
                                        permittivity = Double.POSITIVE_INFINITY;
                                        break;
                                    }
                                    default: {
                                        err = true;
                                        permittivity = 1;
                                        NLog.ofScoped(Material.class).log(NMsg.ofC("invalid relative permittivity %s", value2).asError());
                                    }
                                }
                            } else {
                                NOptional<Double> v = value2.asDoubleValue().filter(x -> x >= 1);
                                if (!v.isPresent()) {
                                    err = true;
                                    NLog.ofScoped(Material.class).log(NMsg.ofC("invalid relative permittivity %s", value2).asError());
                                }
                                permittivity = v.orElse(1.0);
                            }
                            break;
                        }
                        case "μ":
                        case "μr":
                        case "permeability": {
                            if (value2.isName()) {
                                String s = value2.asStringValue().get();
                                switch (NNameFormat.LOWER_KEBAB_CASE.format(s)) {
                                    case "∞":
                                    case "infinity":
                                    case "positive-infinity": {
                                        permeability = Double.POSITIVE_INFINITY;
                                        break;
                                    }
                                    default: {
                                        err = true;
                                        permeability = 1;
                                        NLog.ofScoped(Material.class).log(NMsg.ofC("invalid relative permeability %s", value2).asError());
                                    }
                                }
                            } else {
                                NOptional<Double> v = value2.asDoubleValue().filter(x -> x >= 1);
                                if (!v.isPresent()) {
                                    err = true;
                                    NLog.ofScoped(Material.class).log(NMsg.ofC("invalid relative permeability %s", value2).asError());
                                }
                                permeability = v.orElse(1.0);
                                break;
                            }
                        }
                        case "σ":
                        case "conductivity":
                        case "electricConductivity": {
                            if (value2.isName()) {
                                String s = value2.asStringValue().get();
                                switch (NNameFormat.LOWER_KEBAB_CASE.format(s)) {
                                    case "∞":
                                    case "infinity":
                                    case "positive-infinity": {
                                        electricConductivity = Complex.of(Double.POSITIVE_INFINITY, 0);
                                        break;
                                    }
                                    default: {
                                        err = true;
                                        electricConductivity = Complex.ZERO;
                                        NLog.ofScoped(Material.class).log(NMsg.ofC("invalid electricConductivity %s", value2).asError());
                                    }
                                }
                            } else if (value2.isNumber()) {
                                NDoubleComplex cc = value2.asDoubleComplexValue().get();
                                electricConductivity = Complex.of(cc.realValue(), cc.imagValue());
                            } else {
                                err = true;
                                NLog.ofScoped(Material.class).log(NMsg.ofC("invalid electricConductivity %s", value2).asError());
                            }
                            break;
                        }
                        default: {
                            err = true;
                            NLog.ofScoped(Material.class).log(NMsg.ofC("unknown property %s", value2).asError());
                        }
                    }
                }
            }
            Material cm = null;
            for (Material m : new Material[]{Material.VACUUM, Material.PEC}) {
                if (m.electricConductivity == electricConductivity.doubleValue()
                        && m.permittivity == permittivity
                        && m.permeability == permeability
                        && (NBlankable.isBlank(name) || m.getName().equalsIgnoreCase(name))
                ) {
                    cm = m;
                    break;
                }
            }
            if (cm == null) {
                cm = new Material(
                        NStringUtils.firstNonBlank(name, "CustomMaterial"), permittivity, permeability,
                        electricConductivity.doubleValue() // actual implementation is always real, will handle that later!
                );
            }

            if (err) {
                Material finalCm = cm;
                return NOptional.<Material>ofNamedEmpty(NMsg.ofC("unknown material %s", value).asError())
                        .withDefaultOptional(
                                () -> NOptional.of(finalCm)
                        );

            }
            return NOptional.of(cm);
        } else {
            NLog.ofScoped(Material.class).log(NMsg.ofC("unknown material (should be an object) : %s", value).asError());
            return NOptional.<Material>ofNamedEmpty(NMsg.ofC("unknown material (should be an object) : %s", value).asError())
                    .withDefaultOptional(() -> NOptional.of(Material.VACUUM))
                    ;
        }
    }

    public Material(String name, double permittivity, double permeability, double electricConductivity) {
        this.name = name;
        this.permittivity = permittivity;
        this.permeability = permeability;
        this.electricConductivity = electricConductivity;
    }

    public static Material substrate(double epsr) {
        return substrate("Substrate" + new DecimalFormat("0.0####").format(epsr).replace('.', '_').replace('-', '_'), epsr);
    }

    public static Material substrate(String name, double epsr) {
        return new Material(name, epsr, 1, 0);
    }

    public boolean isSubstrate() {
        return electricConductivity == 0 && permeability == 1;
    }

    public boolean isPEC() {
        return this.equals(PEC);
    }

    public boolean isVacuum() {
        return this.equals(VACUUM);
    }

    public String getName() {
        return name;
    }

    public double getPermittivity() {
        return permittivity;
    }

    public double getPermeability() {
        return permeability;
    }

    public double getElectricConductivity() {
        return electricConductivity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, permittivity, permeability, electricConductivity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Material material = (Material) o;
        return Double.compare(material.permittivity, permittivity) == 0
                && Double.compare(material.permeability, permeability) == 0
                && Double.compare(material.electricConductivity, electricConductivity) == 0
                && Objects.equals(name, material.name);
    }

    @Override
    public NElement toElement() {
        if(this.equals(VACUUM)) {
            return NElement.ofName("Vacuum");
        }
        if(this.equals(PEC)) {
            return NElement.ofName("PEC");
        }
        if(permeability==1 && electricConductivity==0){
            return NElement.ofObjectBuilder(name)
                    .add("permittivity", permittivity)
                    .build();
        }
        if(permittivity==1 && electricConductivity==0){
            return NElement.ofObjectBuilder(name)
                    .add("permeability", permeability)
                    .build();
        }
        if(permittivity==1 && permeability==1){
            return NElement.ofObjectBuilder(name)
                    .add("electricConductivity", electricConductivity == Double.POSITIVE_INFINITY ? NElement.ofName("∞") : NElement.ofDouble(electricConductivity))
                    .build();
        }
        return NElement.ofObjectBuilder(name)
                .add("permittivity", permittivity)
                .add("permeability", permeability)
                .add("electricConductivity", electricConductivity == Double.POSITIVE_INFINITY ? NElement.ofName("∞") : NElement.ofDouble(electricConductivity))
                .build();
    }

    @Override
    public String toString() {
        return String.valueOf(name);
    }

}
