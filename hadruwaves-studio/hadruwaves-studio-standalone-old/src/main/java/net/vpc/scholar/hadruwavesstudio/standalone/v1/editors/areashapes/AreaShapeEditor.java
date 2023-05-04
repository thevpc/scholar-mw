/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.areashapes;

import javax.swing.JComponent;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.MomUIFactory;
import net.thevpc.scholar.hadruwaves.mom.project.shapes.AreaShape;

/**
 *
 * @author vpc
 */
public interface AreaShapeEditor extends MomUIFactory{
    public JComponent getComponent();
    public AreaShape getAreaShape();
    public void setAreaShape(AreaShape shape);
}
