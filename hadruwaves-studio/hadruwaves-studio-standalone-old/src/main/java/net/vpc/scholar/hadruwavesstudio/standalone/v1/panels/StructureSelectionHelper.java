/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadruwavesstudio.standalone.v1.panels;


import java.awt.*;
import java.awt.geom.Area;
import java.util.Collection;
import java.util.HashSet;
import net.thevpc.scholar.hadruwaves.mom.project.common.AreaZone;

/**
 * @author vpc
 */
class StructureSelectionHelper {
    Point oldPosition;
    Collection<AreaZone> motionAreas = new HashSet<AreaZone>();
    Collection<AreaZone> selectedZones = new HashSet<AreaZone>();
    RectInfo[] oldRealPos;
    RectInfo selectionArea;
    Collection<Area> cached_selectedAreas = null;
    boolean startMotion = false;

    public void doEndMotion() {
        for (AreaZone areaZone : motionAreas) {
            areaZone.recompile();
        }

        motionAreas.clear();
        oldPosition = null;
        oldRealPos = null;
    }

    private static class RectInfo {

        double x;
        double y;
        double w;
        double h;
        public RectInfo(double x, double y, double w, double h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
    }
}
