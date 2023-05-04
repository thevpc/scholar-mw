/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors;

import java.awt.Dimension;
import java.awt.Graphics2D;
import net.thevpc.scholar.hadruwaves.mom.project.MomProject;
import net.thevpc.scholar.hadruwaves.mom.project.common.Area;


/**
 *
 * @author vpc
 */
public interface AreaRenderer {

    public void paintArea(Area area, MomProject project, Graphics2D g2d, Dimension boxSize, boolean selected);
}
