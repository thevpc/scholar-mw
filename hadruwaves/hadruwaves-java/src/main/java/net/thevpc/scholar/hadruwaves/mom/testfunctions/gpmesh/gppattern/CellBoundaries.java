package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
* @creationtime 12 juil. 2007 01:32:42
*/
public enum CellBoundaries {
    /**
     * Up to Down according to X
     * Up to Up according to Y
     * <pre>
     * 11111111111111111110
     * 1                  0
     * 1                  0
     * 11111111111111111110
     * </pre>
     */
    UDxUUy,
    DDxUUy,
    DUxUUy,
    UUxUUy,

    UDxDDy,
    DDxDDy,
    DUxDDy,
    UUxDDy,

    UDxDUy,
    DDxDUy,
    DUxDUy,
    UUxDUy,

    UDxUDy,
    DDxUDy,
    DUxUDy,
    UUxUDy;

    public static CellBoundaries eval(boolean east,boolean west,boolean north,boolean south){
        return CellBoundaries.valueOf(
                ((east)?"U":"D")+
                ((west)?"U":"D")+
                        "x"+
                ((north)?"U":"D")+
                ((south)?"U":"D")+
                        "y");
    }

    public boolean isEast(){
        return this.toString().charAt(0)=='U';
    }
    public boolean isWest(){
        return this.toString().charAt(1)=='U';
    }
    public boolean isNorth(){
        return this.toString().charAt(3)=='U';
    }
    public boolean isSouth(){
        return this.toString().charAt(4)=='U';
    }
}
