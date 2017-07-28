package net.vpc.scholar.hadruwaves;

public enum Wall {
    ELECTRIC, MAGNETIC, PERIODIC;
    
    public static Wall wallForChar(char c){
        switch(c){
            case 'M':return Wall.MAGNETIC;
            case 'E':return Wall.ELECTRIC;
            case 'P':return Wall.PERIODIC;
        }
        throw new IllegalArgumentException("Unknown Wall "+c);
    }
    
    public static char charForWall(Wall w){
        switch(w){
            case ELECTRIC:return 'E';
            case MAGNETIC:return 'M';
            case PERIODIC:return 'P';
        }
        throw new IllegalArgumentException("Wall "+w+" not supported");
    }
    
    public boolean is(char c){
        return charForWall()==c;
    }
    
    public char charForWall(){
        return charForWall(this);
    }
}
