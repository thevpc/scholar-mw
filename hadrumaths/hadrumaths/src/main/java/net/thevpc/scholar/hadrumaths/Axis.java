package net.thevpc.scholar.hadrumaths;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.log.NLog;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.util.NNameFormat;
import net.thevpc.nuts.util.NOptional;

public enum Axis {
    X(new Coordinates[]{Coordinates.CARTESIAN}, 0),
    Y(new Coordinates[]{Coordinates.CARTESIAN}, 1),
    Z(new Coordinates[]{Coordinates.CARTESIAN, Coordinates.CYLINDRICAL}, 2),
    R(new Coordinates[]{Coordinates.SPHERICAL, Coordinates.CYLINDRICAL}, 0),
    THETA(new Coordinates[]{Coordinates.SPHERICAL, Coordinates.CYLINDRICAL}, 1),
    PHI(new Coordinates[]{Coordinates.SPHERICAL}, 2);

    public static NOptional<Axis> parse(NElement value) {
        if (value == null || value.isNull()) {
            return NOptional.<Axis>ofNamedEmpty(NMsg.ofC("axis %s", value).asError()).withDefault(() -> Axis.X);
        }
        if (value.isAnyStringOrName()) {
            switch (NNameFormat.LOWER_KEBAB_CASE.format(value.asStringValue().get())) {
                case "x":
                    return NOptional.of(Axis.X);
                case "y":
                    return NOptional.of(Axis.Y);
                case "z":
                    return NOptional.of(Axis.Z);
                case "r":
                    return NOptional.of(Axis.R);
                case "theta":
                case "θ":
                case "ϑ":
                    return NOptional.of(Axis.THETA);
                case "phi":
                case "ɸ":
                case "Φ":
                case "ᵠ":
                    return NOptional.of(Axis.PHI);
                default: {
                }
            }
        }
        NLog.ofScoped(Axis.class).log(NMsg.ofC("invalid axis %s", value).asError());
        return NOptional.<Axis>ofError(NMsg.ofC("invalid axis %s", value).asError()).withDefault(() -> Axis.X);
    }

    private final int index;
    private final Coordinates[] coordinates;

    Axis(Coordinates[] coordinates, int index) {
        this.coordinates = coordinates;
        this.index = index;
    }

    public static Axis cartesian(int i) {
        switch (i) {
            case 0:
                return X;
            case 1:
                return Y;
            case 2:
                return Z;
        }
        throw new IllegalArgumentException("Illegal");
    }

    public static Axis spherical(int i) {
        switch (i) {
            case 0:
                return R;
            case 1:
                return THETA;
            case 2:
                return PHI;
        }
        throw new IllegalArgumentException("Illegal");
    }

    public static Axis cylindrical(int i) {
        switch (i) {
            case 0:
                return R;
            case 1:
                return THETA;
            case 2:
                return Z;
        }
        throw new IllegalArgumentException("Illegal");
    }

    public static Axis[] cartesianValues() {
        return new Axis[]{X, Y, Z};
    }

    public static Axis[] sphericalValues() {
        return new Axis[]{PHI, THETA, R};
    }

    public static Axis[] cylindricalValues() {
        return new Axis[]{THETA, R, Z};
    }

    public int index() {
        return index;
    }

    public void castCoordinates(Coordinates coordinate) {
        if (!acceptCoordinates(coordinate)) {
            throw new ClassCastException(name() + " does not match coordinates " + coordinate);
        }
    }

    public boolean acceptCoordinates(Coordinates coordinate) {
        for (Coordinates c : coordinates) {
            if (c == coordinate) {
                return true;
            }
        }
        return false;
    }

    public Coordinates[] coordinates() {
        return coordinates;
    }
}
