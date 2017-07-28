package net.vpc.scholar.hadruwaves;

public enum WallBorders {
    EEEE //ok
   ,EEEM //ok
   ,EEME //ok
   ,EEMM//ok
   ,EMEE//ok
   ,EMEM //ok
   ,EMME //ok
   ,EMMM //ok
   ,MEEE //ok
   ,MEEM //ok
   ,MEME //ok
   ,MEMM //ok
   ,MMEE //ok
   ,MMEM //ok
   ,MMME //ok
   ,MMMM //
   ,PPPP
    ;

    public Wall north;
    public Wall south;
    public Wall east;
    public Wall west;

    private WallBorders() {
        String pattern=toString();
        init(
                Wall.wallForChar(pattern.charAt(0)),
                Wall.wallForChar(pattern.charAt(1)),
                Wall.wallForChar(pattern.charAt(2)),
                Wall.wallForChar(pattern.charAt(3))
       );
    }

    private void init(Wall north, Wall east, Wall south, Wall west) {
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
    }

    public Wall getEast() {
        return east;
    }

    public Wall getNorth() {
        return north;
    }

    public Wall getSouth() {
        return south;
    }

    public Wall getWest() {
        return west;
    }

    public boolean is(String pattern){
        return(
                north.is(pattern.charAt(0))
                && east.is(pattern.charAt(1))
                && south.is(pattern.charAt(2))
                && west.is(pattern.charAt(3))
        );
    }
    
    public static WallBorders valueOf(Wall north, Wall east, Wall south, Wall west){
        return valueOf(new String(
                new char[]{
                        north.charForWall(),
                        east.charForWall(),
                        south.charForWall(),
                        west.charForWall(),
                }
        ));
    }

    public String getJxDescription(){
        return getExDescription();
    }

    public String getJyDescription(){
        return getEyDescription();
    }

    public String getExDescription(){
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

    public String getEyDescription(){
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
