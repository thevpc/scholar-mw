package net.thevpc.scholar.hadruplot;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 9 sept. 2005
 * Time: 22:34:35
 * To change this template use File | Settings | File Templates.
 */
public enum PlotType {
    CURVE, BAR, AREA, FIELD, PIE, RING, BUBBLE, MESH, HEATMAP, MATRIX, POLAR, AUTO, TABLE, ALL;

    public static PlotType of(String s) {
        return valueOf(s);
    }
}
