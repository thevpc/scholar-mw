package net.vpc.scholar.hadrumaths.geom;

import java.io.Serializable;

public class IntPoint implements Serializable,Cloneable {
    private static final long serialVersionUID = -1010101010101001002L;
    /**
     * Created by IntelliJ IDEA.
     * User: EZZET
     * Date: 2 mars 2007
     * Time: 01:52:38
     * To change this template use File | Settings | File Templates.
     */
        public final int x,y,z;
        public final int dimension;
    public IntPoint(int x, int y) {
        this.x = x;
        this.y = y;
        this.z = 0;
        this.dimension = 2;
    }

    public IntPoint(int x) {
        this.x = x;
        this.y = 0;
        this.z = 0;
        this.dimension = 1;
    }

    public IntPoint(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = 3;
    }

    private IntPoint(int x, int y, int z, int dim) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dim;
    }

    public IntPoint clone(){
        try {
            return (IntPoint) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean equals(Object obj) {
        if(obj ==null || !(obj instanceof IntPoint)){
            return false;
        }
        IntPoint p=(IntPoint) obj;
        return x==p.x && y==p.y && z==p.z && dimension==p.dimension;
    }

    public String toString() {
        switch (dimension){
            case 1:{
                return "("+x+")";
            }
            case 2:{
                return "("+x+","+y+")";
            }
        }
        return "("+x+","+y+","+z+")";
    }

    public static IntPoint create(int x){
        return new IntPoint(x,0,0,1);
    }

    public static IntPoint create(int x,int y){
        return new IntPoint(x,y,0,2);
    }

    public static IntPoint create(int x,int y,int z){
        return new IntPoint(x,y,z,3);
    }
}
