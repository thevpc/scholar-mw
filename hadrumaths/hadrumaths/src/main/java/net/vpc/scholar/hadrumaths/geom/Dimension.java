package net.vpc.scholar.hadrumaths.geom;

import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.HSerializable;
import net.vpc.scholar.hadrumaths.Maths;

public class Dimension implements HSerializable {
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

    public Dimension(double x, double y) {
        this.x = x;
        this.y = y;
        this.z = 0;
        this.dimension = 2;
    }

    public Dimension(double x) {
        this.x = x;
        this.y = 0;
        this.z = 0;
        this.dimension = 1;
    }

    public Dimension(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = 3;
    }

    private Dimension(double x, double y, double z, int dim) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dim;
    }

    public static Dimension create(double x) {
        return new Dimension(x, 0, 0, 1);
    }

    public static Dimension create(double x, double y, double z) {
        return new Dimension(x, y, z, 3);
    }

    @Override
    public int hashCode() {
        int result = 13;
        result = 31 * Double.hashCode(x);
        result = 31 * result + Double.hashCode(y);
        result = 31 * result + Double.hashCode(z);
        result = 31 * result + dimension;
        return result;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Dimension)) {
            return false;
        }
        Dimension p = (Dimension) obj;
        return x == p.x && y == p.y && z == p.z && dimension == p.dimension;
    }

    protected Dimension clone() {
        return Dimension.create(x, y);
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

    public static Dimension create(double x, double y) {
        return new Dimension(x, y, 0, 2);
    }

    public boolean roundEquals(Dimension p, double epsilon) {
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
    public TsonElement toTsonElement(TsonObjectContext context) {
        switch (dimension) {
            case 1: {
                return Tson.uplet(Tson.elem(x)).build();
            }
            case 2: {
                return Tson.uplet(Tson.elem(x), Tson.elem(y)).build();
            }
        }
        return Tson.uplet(Tson.elem(x), Tson.elem(y), Tson.elem(z)).build();
    }
}
