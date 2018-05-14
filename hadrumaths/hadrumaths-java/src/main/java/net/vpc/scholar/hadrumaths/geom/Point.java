package net.vpc.scholar.hadrumaths.geom;

import net.vpc.scholar.hadrumaths.Maths;

import java.io.Serializable;

public class Point implements Serializable,Cloneable {
    private static final long serialVersionUID = -1010101010101001002L;
    /**
     * Created by IntelliJ IDEA.
     * User: EZZET
     * Date: 2 mars 2007
     * Time: 01:52:38
     * To change this template use File | Settings | File Templates.
     */
    public final double x, y, z;
    public final int dimension;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        this.z = 0;
        this.dimension = 2;
    }

    public Point(double x) {
        this.x = x;
        this.y = 0;
        this.z = 0;
        this.dimension = 1;
    }

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = 3;
    }

    private Point(double x, double y, double z, int dim) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dim;
    }

    public double distance(Point q) {
        int d = Maths.max(dimension, q.dimension);
        switch (d) {
            case 1: {
                return Maths.sqrt((x - q.x) * (x - q.x));
            }
            case 2: {
                return (Maths.sqrt((x - q.x) * (x - q.x) + (y - q.y) * (y - q.y)));
            }
        }
        return (Maths.sqrt((x - q.x) * (x - q.x) + (y - q.y) * (y - q.y) + (z - q.z) * (z - q.z)));
    }
    public Point translate(double dx,double dy){
        switch (dimension){
            case 1:{
                if(y==0){
                    return new Point(x+dx);

                }else {
                    throw new IllegalArgumentException("Unsupported");
                }
            }
            case 2:{
                return new Point(x+dx,y+dy);
            }
            case 3:{
                return new Point(x+dx,y+dy,z+0);
            }
        }
        throw new IllegalArgumentException("Unsupported dimension");
    }

    protected Point clone() {
        return Point.create(x, y);
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Point)) {
            return false;
        }
        Point p = (Point) obj;
        return x == p.x && y == p.y && z == p.z && dimension == p.dimension;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + dimension;
        return result;
    }

    public boolean roundEquals(Point p, double epsilon) {
        if (p == null) {
            return false;
        }
        if (dimension != p.dimension) {
            return false;
        }
        switch (dimension) {
            case 1: {
                return Maths.roundEquals(x, p.x, epsilon);
            }
            case 2: {
                return Maths.roundEquals(x, p.x, epsilon) && Maths.roundEquals(y, p.y, epsilon);
            }
            case 3: {
                return Maths.roundEquals(x, p.x, epsilon) && Maths.roundEquals(y, p.y, epsilon) && Maths.roundEquals(z, p.z, epsilon);
            }
        }
        return Maths.roundEquals(x, p.x, epsilon) && Maths.roundEquals(y, p.y, epsilon) && Maths.roundEquals(z, p.z, epsilon);
    }

    public String toString() {
        switch (dimension) {
            case 1: {
                return "(" + x + ")";
            }
            case 2: {
                return "(" + x + "," + y + ")";
            }
        }
        return "(" + x + "," + y + "," + z + ")";
    }

    public static Point create(double x) {
        return new Point(x, 0, 0, 1);
    }

    public static Point create(double x, double y) {
        return new Point(x, y, 0, 2);
    }

    public static Point create(double x, double y, double z) {
        return new Point(x, y, z, 3);
    }
}
