/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadruwavesstudio.standalone.v1;

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
