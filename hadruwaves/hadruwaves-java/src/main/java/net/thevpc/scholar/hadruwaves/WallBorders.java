package net.thevpc.scholar.hadruwaves;

public enum WallBorders {
    EEEE //ok
    , EEEM //ok
    , EEME //ok
    , EEMM//ok
    , EMEE//ok
    , EMEM //ok
    , EMME //ok
    , EMMM //ok
    , MEEE //ok
    , MEEM //ok
    , MEME //ok
    , MEMM //ok
    , MMEE //ok
    , MMEM //ok
    , MMME //ok
    , MMMM //
    , PPPP;

    public Boundary north;
    public Boundary south;
    public Boundary east;
    public Boundary west;

    WallBorders() {
        String pattern = toString();
        init(
                Boundary.of(pattern.charAt(0)),
                Boundary.of(pattern.charAt(1)),
                Boundary.of(pattern.charAt(2)),
                Boundary.of(pattern.charAt(3))
        );
    }

    public static WallBorders of(Boundary north, Boundary east, Boundary south, Boundary west) {
        return valueOf(new String(
                new char[]{
                        north.idChar(),
                        east.idChar(),
                        south.idChar(),
                        west.idChar(),
                }
        ));
    }

    public static WallBorders of(String str) {
        return valueOf(str);
    }

    public static WallBorders valueOf(Boundary north, Boundary east, Boundary south, Boundary west) {
        return valueOf(new String(
                new char[]{
                        north.idChar(),
                        east.idChar(),
                        south.idChar(),
                        west.idChar(),
                }
        ));
    }

    private void init(Boundary north, Boundary east, Boundary south, Boundary west) {
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
    }

    public Boundary[] toArray() {
        return new Boundary[]{
                north, east, south, west
        };
    }

    public Boundary getEast() {
        return east;
    }

    public Boundary getNorth() {
        return north;
    }

    public Boundary getSouth() {
        return south;
    }

    public Boundary getWest() {
        return west;
    }

    public boolean is(String pattern) {
        return (
                north.is(pattern.charAt(0))
                        && east.is(pattern.charAt(1))
                        && south.is(pattern.charAt(2))
                        && west.is(pattern.charAt(3))
        );
    }

    public String getJxDescription() {
        return getExDescription();
    }

    public String getJyDescription() {
        return getEyDescription();
    }

    public String getExDescription() {
        StringBuilder xx = new StringBuilder(4);
        String str = toString();
        switch (str.charAt(0)) {
            case 'E': {
                xx.append('-');
                break;
            }
            case 'M': {
                xx.append('+');
                break;
            }
            case 'P': {
                xx.append('.');
                break;
            }
        }
        switch (str.charAt(1)) {
            case 'M': {
                xx.append('-');
                break;
            }
            case 'E': {
                xx.append('+');
                break;
            }
            case 'P': {
                xx.append('.');
                break;
            }
        }
        switch (str.charAt(2)) {
            case 'E': {
                xx.append('-');
                break;
            }
            case 'M': {
                xx.append('+');
                break;
            }
            case 'P': {
                xx.append('.');
                break;
            }
        }
        switch (str.charAt(3)) {
            case 'M': {
                xx.append('-');
                break;
            }
            case 'E': {
                xx.append('+');
                break;
            }
            case 'P': {
                xx.append('.');
                break;
            }
        }
        return xx.toString();
    }

    public String getEyDescription() {
        StringBuilder yy = new StringBuilder(4);
        String str = toString();
        switch (str.charAt(0)) {
            case 'E': {
                yy.append('+');
                break;
            }
            case 'M': {
                yy.append('-');
                break;
            }
            case 'P': {
                yy.append('.');
                break;
            }
        }
        switch (str.charAt(1)) {
            case 'M': {
                yy.append('+');
                break;
            }
            case 'E': {
                yy.append('-');
                break;
            }
            case 'P': {
                yy.append('.');
                break;
            }
        }
        switch (str.charAt(2)) {
            case 'E': {
                yy.append('+');
                break;
            }
            case 'M': {
                yy.append('-');
                break;
            }
            case 'P': {
                yy.append('.');
                break;
            }
        }
        switch (str.charAt(3)) {
            case 'M': {
                yy.append('+');
                break;
            }
            case 'E': {
                yy.append('-');
                break;
            }
            case 'P': {
                yy.append('.');
                break;
            }
        }
        return yy.toString();
    }
}
