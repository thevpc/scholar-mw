/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruplot;

import java.util.Arrays;

/**
 *
 * @author vpc
 */
public class PlotPath {

    public static final PlotPath ROOT = new PlotPath(new String[0]);
    private String[] elems;

    public static PlotPath nonnull(PlotPath s) {
        if (s == null) {
            return ROOT;
        }
        return s;
    }

    public static PlotPath of(String s) {
        String[] r = split(s);
        if (r.length == 0) {
            return ROOT;
        }
        return new PlotPath(r);
    }

    public String get(int i) {
        return elems[i];
    }

    public PlotPath removeFirst() {
        if (elems.length == 0) {
            return this;
        }
        return new PlotPath(Arrays.copyOfRange(elems, 1, elems.length));
    }

    public PlotPath getParent() {
        if (elems.length == 0) {
            return this;
        }
        return new PlotPath(Arrays.copyOfRange(elems, 0, elems.length - 1));
    }

    public PlotPath append(String other) {
        return append(of(other));
    }

    public PlotPath append(PlotPath other) {
        if (other == null || other.size() == 0) {
            return this;
        }
        if (size() == 0) {
            return other;
        }
        String[] n = new String[size() + other.size()];
        System.arraycopy(elems, 0, n, 0, elems.length);
        System.arraycopy(other.elems, 0, n, elems.length, other.elems.length);
        return new PlotPath(n);
    }

    public boolean isRoot() {
        return elems.length == 0;
    }

    public int size() {
        return elems.length;
    }

    private PlotPath(String[] elems) {
        this.elems = elems;
    }

    private static String[] split(String s) {
        if (s == null) {
            return new String[0];
        }
        return Arrays.stream(s.split("/")).filter(x -> x.length() > 0).toArray(String[]::new);
    }

    @Override
    public String toString() {
        if (elems.length == 0) {
            return "/";
        }
        StringBuilder sb = new StringBuilder();
        for (String elem : elems) {
            sb.append("/").append(elem);
        }
        return sb.toString();
    }

}
