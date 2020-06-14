package net.vpc.scholar.hadrumaths;

public enum Axis {
    X(new Coordinates[]{Coordinates.CARTESIAN}, 0),
    Y(new Coordinates[]{Coordinates.CARTESIAN}, 1),
    Z(new Coordinates[]{Coordinates.CARTESIAN,Coordinates.CYLINDRICAL}, 2),
    R(new Coordinates[]{Coordinates.SPHERICAL,Coordinates.CYLINDRICAL}, 0),
    THETA(new Coordinates[]{Coordinates.SPHERICAL,Coordinates.CYLINDRICAL}, 1),
    PHI(new Coordinates[]{Coordinates.SPHERICAL}, 2)

    ;
    private int index;
    private Coordinates[] coordinates;

    Axis(Coordinates[] coordinates, int index) {
        this.coordinates = coordinates;
        this.index = index;
    }

    public static Axis cartesian(int i) {
        switch(i){
            case 0:return X;
            case 1:return Y;
            case 2:return Z;
        }
        throw new IllegalArgumentException("Illegal");
    }
    
    public static Axis spherical(int i) {
        switch(i){
            case 0:return R;
            case 1:return THETA;
            case 2:return PHI;
        }
        throw new IllegalArgumentException("Illegal");
    }
    
    public static Axis cylindrical(int i) {
        switch(i){
            case 0:return R;
            case 1:return THETA;
            case 2:return Z;
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
        if(!acceptCoordinates(coordinate)){
            throw new ClassCastException(name()+" does not match coordinates "+coordinate);
        }
    }

    public boolean acceptCoordinates(Coordinates coordinate) {
        for (Coordinates c : coordinates) {
            if(c==coordinate){
                return true;
            }
        }
        return false;
    }
    public Coordinates[] coordinates() {
        return coordinates;
    }
}
