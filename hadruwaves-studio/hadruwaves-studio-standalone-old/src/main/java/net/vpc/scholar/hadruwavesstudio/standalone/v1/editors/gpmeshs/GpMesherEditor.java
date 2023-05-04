/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.gpmeshs;

import javax.swing.JComponent;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.MomUIFactory;
import net.thevpc.scholar.hadruwaves.mom.project.gpmesher.GpMesher;

/**
 *
 * @author vpc
 */
public interface GpMesherEditor extends MomUIFactory{
    public JComponent getComponent();
    public GpMesher getGpMesher();
    public void setGpMesher(GpMesher mesher);
}
