/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadruwaves.studio.standalone;

import javax.swing.JComponent;

/**
 *
 * @author vpc
 */
public interface MomUIFactory {
    public MomUIFactory create();
    public String getId();
    public JComponent getComponent();
}
