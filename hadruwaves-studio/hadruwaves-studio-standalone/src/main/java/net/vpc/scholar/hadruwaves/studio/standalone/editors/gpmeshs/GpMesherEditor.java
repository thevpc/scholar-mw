/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadruwaves.studio.standalone.editors.gpmeshs;

import javax.swing.JComponent;
import net.vpc.scholar.hadruwaves.studio.standalone.MomUIFactory;
import net.vpc.scholar.hadruwaves.mom.project.gpmesher.GpMesher;

/**
 *
 * @author vpc
 */
public interface GpMesherEditor extends MomUIFactory{
    public JComponent getComponent();
    public GpMesher getGpMesher();
    public void setGpMesher(GpMesher mesher);
}