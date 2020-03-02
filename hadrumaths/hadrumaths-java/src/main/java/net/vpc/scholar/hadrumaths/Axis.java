package net.vpc.scholar.hadrumaths;

public enum Axis {
    X(Coordinates.CARTESIAN, 0),
    Y(Coordinates.CARTESIAN, 1),
    Z(Coordinates.CARTESIAN, 2),
    THETA(Coordinates.SPHERICAL, 0),
    PHI(Coordinates.SPHERICAL, 1),
    R(Coordinates.SPHERICAL, 2);
    private int index;
    private Coordinates coordinates;

    Axis(Coordinates coordinates, int index) {
        this.coordinates = coordinates;
        this.index = index;
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

    public Coordinates coordinates() {
        return coordinates;
    }
}
