package net.vpc.scholar.hadrumaths.meshalgo;

import java.io.Serializable;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 15 mai 2007 21:53:32
 */
public class MeshZoneType implements Comparable<MeshZoneType>, Serializable {

    public static final int ID_MAIN = 0;
    public static final int ID_ATTACHX = 1;
    public static final int ID_ATTACHY = 2;
    public static final int ID_BORDER_NORTH = 3;
    public static final int ID_BORDER_SOUTH = 4;
    public static final int ID_BORDER_EAST = 5;
    public static final int ID_BORDER_WEST = 6;
    public static MeshZoneType MAIN = new MeshZoneType(ID_MAIN, "main");
    public static MeshZoneType ATTACHX = new MeshZoneType(ID_ATTACHX, "attach_x");
    public static MeshZoneType ATTACHY = new MeshZoneType(ID_ATTACHY, "attach_y");
    public static MeshZoneType BORDER_EAST = new MeshZoneType(ID_BORDER_EAST, "attach_e");
    public static MeshZoneType BORDER_WEST = new MeshZoneType(ID_BORDER_WEST, "attachx_w");
    public static MeshZoneType BORDER_NORTH = new MeshZoneType(ID_BORDER_NORTH, "attach_n");
    public static MeshZoneType BORDER_SOUTH = new MeshZoneType(ID_BORDER_SOUTH, "attach_s");

    public static MeshZoneTypeFilter FILTER_ALL = new MeshZoneTypeFilterAll();

    /**
     * MAIN
     */
    public static MeshZoneTypeFilter FILTER_M = new MeshZoneTypeFilterAccepted(MAIN);

    /**
     * MAIN,ATTACHX,BORDER_EAST,BORDER_WEST
     */
    public static MeshZoneTypeFilter FILTER_MXEW = new MeshZoneTypeFilterAccepted(MAIN, ATTACHX, BORDER_EAST, BORDER_WEST);

    /**
     * MAIN,ATTACHY,BORDER_NORTH,BORDER_SOUTH
     */
    public static MeshZoneTypeFilter FILTER_MYNS = new MeshZoneTypeFilterAccepted(MAIN, ATTACHY, BORDER_NORTH, BORDER_SOUTH);

    /**
     * MAIN,ATTACHX
     */
    public static MeshZoneTypeFilter FILTER_MX = new MeshZoneTypeFilterAccepted(MAIN, ATTACHX);
    /**
     * MAIN,ATTACHY
     */
    public static MeshZoneTypeFilter FILTER_MY = new MeshZoneTypeFilterAccepted(MAIN, ATTACHY);
    /**
     * MAIN,ATTACHX,ATTACHY
     */
    public static MeshZoneTypeFilter FILTER_MXY = new MeshZoneTypeFilterAccepted(MAIN, ATTACHX, ATTACHY);

    private int value;
    private String name;

    public MeshZoneType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public int compareTo(MeshZoneType o) {
        return value - (o == null ? -1 : o.value);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MeshZoneType that = (MeshZoneType) o;

        if (value != that.value) return false;

        return true;
    }

    public int hashCode() {
        return value;
    }
}
