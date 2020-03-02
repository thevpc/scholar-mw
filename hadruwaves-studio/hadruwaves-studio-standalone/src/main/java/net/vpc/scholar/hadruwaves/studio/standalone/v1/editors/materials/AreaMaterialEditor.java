/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadruwaves.studio.standalone.v1.editors.materials;

import java.awt.Color;
import javax.swing.JComponent;
import net.vpc.scholar.hadruwaves.studio.standalone.v1.MomUIFactory;
import net.vpc.scholar.hadruwaves.mom.project.areamaterial.AreaMaterial;

/**
 *
 * @author vpc
 */
public interface AreaMaterialEditor extends MomUIFactory{
    public Color getDefaultColor();
    public JComponent getComponent();
    public AreaMaterial getAreaMaterial();
    public void setAreaMaterial(AreaMaterial material);
}
