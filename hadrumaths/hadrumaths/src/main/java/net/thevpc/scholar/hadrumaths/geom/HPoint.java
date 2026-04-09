package net.thevpc.scholar.hadrumaths.geom;


import net.thevpc.nuts.elem.NElement;

import net.thevpc.scholar.hadrumaths.HSerializable;
import net.thevpc.scholar.hadrumaths.Maths;

public class HPoint implements HSerializable {
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

    public HPoint(double x, double y) {
        this.x = x;
        this.y = y;
        this.z = 0;
        this.dimension = 2;
    }

    public HPoint(double x) {
        this.x = x;
        this.y = 0;
        this.z = 0;
        this.dimension = 1;
    }

    public HPoint(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = 3;
    }

    private HPoint(double x, double y, double z, int dim) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dim;
    }

    public static HPoint create(double x) {
        return new HPoint(x, 0, 0, 1);
    }

    public static HPoint create(double x, double y, double z) {
        return new HPoint(x, y, z, 3);
    }

    public double distance(HPoint q) {
        int d = Math.max(dimension, q.dimension);
        switch (d) {
            case 1: {
                return Math.sqrt((x - q.x) * (x - q.x));
            }
            case 2: {
                return (Math.sqrt((x - q.x) * (x - q.x) + (y - q.y) * (y - q.y)));
            }
        }
        return (Math.sqrt((x - q.x) * (x - q.x) + (y - q.y) * (y - q.y) + (z - q.z) * (z - q.z)));
    }

    public HPoint translate(double dx, double dy) {
        switch (dimension) {
            case 1: {
                if (y == 0) {
                    return new HPoint(x + dx);

                } else {
                    throw new IllegalArgumentException("Unsupported");
                }
            }
            case 2: {
                return new HPoint(x + dx, y + dy);
            }
            case 3: {
                return new HPoint(x + dx, y + dy, z + 0);
            }
        }
        throw new IllegalArgumentException("Unsupported dimension");
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }


    @Override
    public int hashCode() {
        int result = 13;
        result = 31 * result + Double.hashCode(x);
        result = 31 * result + Double.hashCode(y);
        result = 31 * result + Double.hashCode(z);
        result = 31 * result + dimension;
        return result;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof HPoint)) {
            return false;
        }
        HPoint p = (HPoint) obj;
        return x == p.x && y == p.y && z == p.z && dimension == p.dimension;
    }

    protected HPoint clone() {
        return new HPoint(x, y, z, dimension);
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

    public static HPoint create(double x, double y) {
        return new HPoint(x, y, 0, 2);
    }

    public boolean roundEquals(HPoint p, double epsilon) {
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

    @Override
    public NElement toElement() {
        switch (dimension) {
            case 1: {
                return NElement.ofUplet(NElement.ofDouble(x));
            }
            case 2: {
                return NElement.ofUplet(NElement.ofDouble(x), NElement.ofDouble(y));
            }
        }
        return NElement.ofUplet(NElement.ofDouble(x), NElement.ofDouble(y), NElement.ofDouble(z));
    }
}
