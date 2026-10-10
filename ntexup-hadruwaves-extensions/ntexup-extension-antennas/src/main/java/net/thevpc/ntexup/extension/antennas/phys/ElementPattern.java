package net.thevpc.ntexup.extension.antennas.phys;

/**
 * Far-field element pattern of a single array element.
 *
 * The angle convention follows the array: theta is measured from broadside (+Y),
 * in [-90 deg, +90 deg]. Pure Java (no ntexup dependency).
 */
public enum ElementPattern {

    /**
     * Omnidirectional element: f(theta) = 1.
     */
    ISOTROPIC("isotropic") {
        @Override
        public double factor(double thetaDeg) {
            return 1.0;
        }
    },

    /**
     * Half-wave dipole aligned with the array axis (X axis).
     * f(theta) = |cos((PI/2)*sin(theta)) / cos(theta)|, guarded at the endfire
     * singularities (theta = +/- 90 deg) where both numerator and denominator vanish.
     */
    DIPOLE("dipole") {
        @Override
        public double factor(double thetaDeg) {
            double t = Math.toRadians(thetaDeg);
            double c = Math.cos(t);
            if (Math.abs(c) < 1e-3) {
                return 0.0;
            }
            return Math.abs(Math.cos(Math.PI / 2.0 * Math.sin(t)) / c);
        }
    },

    /**
     * Directional cardioid element pointed at broadside:
     * f(theta) = (1 + cos(theta)) / 2.
     */
    CARDIOID("cardioid") {
        @Override
        public double factor(double thetaDeg) {
            return (1.0 + Math.cos(Math.toRadians(thetaDeg))) / 2.0;
        }
    };

    private final String id;

    ElementPattern(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    /**
     * Amplitude factor (>= 0) of the element pattern in direction {@code thetaDeg}.
     */
    public abstract double factor(double thetaDeg);

    /**
     * Parses a pattern id ({@code isotropic}, {@code dipole}, {@code cardioid});
     * unknown values fall back to {@link #ISOTROPIC}.
     */
    public static ElementPattern parse(String s) {
        if (s == null) {
            return ISOTROPIC;
        }
        switch (s.trim().toLowerCase()) {
            case "dipole":
            case "dip":
            case "hf-dipole":
            case "half-wave-dipole":
                return DIPOLE;
            case "cardioid":
            case "card":
            case "directional":
                return CARDIOID;
            case "isotropic":
            case "iso":
            case "omni":
            case "omnidirectional":
            default:
                return ISOTROPIC;
        }
    }

    /** Display label used in legends / badges. */
    public String label() {
        switch (this) {
            case DIPOLE:
                return "Half-wave dipole";
            case CARDIOID:
                return "Cardioid";
            default:
                return "Isotropic";
        }
    }
}