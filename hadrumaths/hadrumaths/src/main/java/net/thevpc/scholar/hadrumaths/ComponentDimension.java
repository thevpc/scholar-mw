/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths;

import java.io.Serializable;

/**
 * @author vpc
 */
public class ComponentDimension implements Serializable {
    private static final ComponentDimension[][] CACHE = new ComponentDimension[3][3];
    private static final int CACHE_SIZE = CACHE.length;
    public static final ComponentDimension SCALAR = of(1, 1);
    public static final ComponentDimension VECTOR2 = of(2, 1);
    public static final ComponentDimension VECTOR3 = of(3, 1);

    public final int rows;
    public final int columns;

    public ComponentDimension(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    public static ComponentDimension min(ComponentDimension a, ComponentDimension b) {
        return of(Math.min(a.rows, b.rows), Math.min(a.columns, b.columns));
    }

    public static ComponentDimension of(int rows, int columns) {
        if (rows <= CACHE_SIZE && columns <= CACHE.length) {
            ComponentDimension v = CACHE[rows - 1][columns - 1];
            if (v == null) {
                v = new ComponentDimension(rows, columns);
                CACHE[rows - 1][columns - 1] = v;
            }
            return v;
        }
        return new ComponentDimension(rows, columns);
    }

    public static ComponentDimension max(ComponentDimension a, ComponentDimension b) {
        return of(Math.max(a.rows, b.rows), Math.max(a.columns, b.columns));
    }

    @Override
    public int hashCode() {
        int result = rows;
        result = 31 * result + columns;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComponentDimension)) return false;

        ComponentDimension that = (ComponentDimension) o;

        if (columns != that.columns) return false;
        return rows == that.rows;
    }

    @Override
    public String toString() {
        return "(" +
                rows +
                ", " + columns +
                ')';
    }

    public int[][] iterate() {
        int[][] all = new int[rows * columns][2];
        int p = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int[] ints = all[p];
                ints[0] = i;
                ints[1] = j;
                p++;
            }
        }
        return all;
    }

    public ComponentDimension expand(ComponentDimension other) {
        return ComponentDimension.of(
                Math.max(this.rows, other.rows),
                Math.max(this.columns, other.columns)
        );
    }

    public boolean is(int rows, int columns) {
        return this.rows == rows && this.columns == columns;
    }
}
