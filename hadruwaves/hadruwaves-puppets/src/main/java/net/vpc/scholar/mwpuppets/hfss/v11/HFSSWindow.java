/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.mwpuppets.hfss.v11;

import net.vpc.common.deskauto.DEWindow;

/**
 *
 * @author vpc
 */
public class HFSSWindow {
    private DEWindow win;

    public HFSSWindow(DEWindow win) {
        this.win = win;
    }
    
    public DEWindow getWindow(){
        return win;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+"{" + win + '}';
    }
    
}
