package net.vpc.scholar.hadrumaths.meshalgo.rect;

import net.vpc.scholar.hadrumaths.dump.Dumpable;
import net.vpc.scholar.hadrumaths.dump.Dumper;

import java.io.Serializable;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 10 mai 2007 19:50:52
 */
public final class GridPrecision implements Cloneable, Serializable, Dumpable {
    private static final long serialVersionUID = 1L;
    private int xmax = 1;
    private int ymax = 1;
    private int xmin = 1;
    private int ymin = 1;
    public static final GridPrecision LEAST_PRECISION = new GridPrecision(0, 0, 0, 0);

    public static GridPrecision forX(int min, int max) {
        return new GridPrecision(min, max, 0, 0);
    }

    public static GridPrecision forX(int value) {
        return new GridPrecision(value, value, 0, 0);
    }

    public static GridPrecision forY(int min, int max) {
        return new GridPrecision(0, 0, min, max);
    }

    public static GridPrecision forY(int value) {
        return new GridPrecision(0, 0, value, value);
    }

    public static GridPrecision forXY(int min, int max) {
        return new GridPrecision(min, max, min, max);
    }

    public static GridPrecision forXY(int value) {
        return new GridPrecision(value, value, value, value);
    }

    public static GridPrecision forXY(int xmin, int xmax, int ymin, int ymax) {
        return new GridPrecision(xmin, xmax, ymin, ymax);
    }

    private GridPrecision(int xmin, int xmax, int ymin, int ymax) {
        this.xmin = (xmin);
        this.xmax = (xmax);
        this.ymin = (ymin);
        this.ymax = (ymax);
    }

    public GridPrecision(int val) {
        this(val, val, -1, -1);
    }

    public GridPrecision(int xmin, int xmax) {
        this(xmin, xmax, -1, -1);
    }

    public int getXmax() {
        return xmax;
    }

    public int getYmax() {
        return ymax;
    }

    public int getXmin() {
        return xmin;
    }

    public int getYmin() {
        return ymin;
    }

    public String dump() {
        Dumper h = new Dumper(getClass().getSimpleName(), Dumper.Type.SIMPLE);
        h.add("xmin", xmin);
        h.add("xmax", xmax);
        h.add("ymin", ymin);
        h.add("ymax", ymax);
        return h.toString();
    }

    public String toString() {
        return dump();
    }
}
