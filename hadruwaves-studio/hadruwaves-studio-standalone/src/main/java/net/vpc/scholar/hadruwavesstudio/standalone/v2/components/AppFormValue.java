/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.components;

/**
 *
 * @author vpc
 */
public interface AppFormValue {

    Object getValue(AppFormField item);

    void setValue(AppFormField item, Object value);

    
}
