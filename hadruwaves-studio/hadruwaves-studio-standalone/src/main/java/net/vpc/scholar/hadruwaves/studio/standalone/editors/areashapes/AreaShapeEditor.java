/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadruwaves.studio.standalone.editors.areashapes;

import javax.swing.JComponent;
import net.vpc.scholar.hadruwaves.studio.standalone.MomUIFactory;
import net.vpc.scholar.hadruwaves.mom.project.shapes.AreaShape;

/**
 *
 * @author vpc
 */
public interface AreaShapeEditor extends MomUIFactory{
    public JComponent getComponent();
    public AreaShape getAreaShape();
    public void setAreaShape(AreaShape shape);
}
