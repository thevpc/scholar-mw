/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.mom.sources;

import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;

import java.io.Serializable;

/**
 * @author vpc
 */
public interface Source extends Serializable, Cloneable {

    public DoubleToVector getFunction();

    public Source clone();
}
