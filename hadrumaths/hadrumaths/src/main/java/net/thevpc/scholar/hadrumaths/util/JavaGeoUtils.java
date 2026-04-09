package net.thevpc.scholar.hadrumaths.util;

import java.awt.geom.Area;
import java.awt.geom.PathIterator;

public class JavaGeoUtils {
    public static int countPoints(Area area) {
        int count = 0;
        PathIterator pi = area.getPathIterator(null);
        double[] coords = new double[6];
        while (!pi.isDone()) {
            int type = pi.currentSegment(coords);
            if (type == PathIterator.SEG_MOVETO || type == PathIterator.SEG_LINETO) {
                count++;
            }
            // SEG_CLOSE doesn't add a new point (it repeats the first)
            pi.next();
        }
        return count;
    }
}
