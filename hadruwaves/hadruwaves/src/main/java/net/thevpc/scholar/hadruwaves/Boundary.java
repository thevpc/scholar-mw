package net.thevpc.scholar.hadruwaves;

public enum Boundary {
    ELECTRIC,
    MAGNETIC,
    PERIODIC,
    // Matched Load
    INFINITE,
    //Open
    OPEN,
    //Open
    NOTHING,
    ;

    public static Boundary of(char c) {
        switch (c) {
            case 'M':
                return Boundary.MAGNETIC;
            case 'E':
                return Boundary.ELECTRIC;
            case 'P':
                return Boundary.PERIODIC;
            case 'I':
                return Boundary.INFINITE;
            case 'O':
                return Boundary.OPEN;
            case 'N':
                return Boundary.NOTHING;
        }
        throw new IllegalArgumentException("Unknown Wall " + c);
    }

    public static char idChar(Boundary w) {
        switch (w) {
            case ELECTRIC:
                return 'E';
            case MAGNETIC:
                return 'M';
            case PERIODIC:
                return 'P';
            case INFINITE:
                return 'I';
            case OPEN:
                return 'O';
            case NOTHING:
                return 'N';
        }
        throw new IllegalArgumentException("Wall " + w + " not supported");
    }

    public boolean is(char c) {
        return idChar() == c;
    }

    public char idChar() {
        return idChar(this);
    }
}
