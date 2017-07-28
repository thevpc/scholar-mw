/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadruwaves.mom.project;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vpc
 */
public abstract class AbstractMomProjectItem implements MomProjectItem{
    private MomProject context;
    public AbstractMomProjectItem() {
    }

    public MomProjectItem create() {
        try {
            return getClass().newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(AbstractMomProjectItem.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(AbstractMomProjectItem.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public MomProject getContext() {
        return context;
    }

    public void recompile() {
        //
    }

    public void setContext(MomProject context) {
        this.context=context;
    }

}
