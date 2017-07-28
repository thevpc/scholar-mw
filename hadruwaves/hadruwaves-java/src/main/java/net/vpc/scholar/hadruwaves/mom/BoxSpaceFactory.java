/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.AbstractFactory;

/**
 *
 * @author vpc
 */
public class BoxSpaceFactory extends AbstractFactory{

    private BoxSpaceFactory() {
    }

    public static BoxSpace openCircuit(double epsr,double width){
        return new BoxSpace(BoxLimit.OPEN, epsr,width);
    }

    public static BoxSpace matchedLoad(double epsr){
        return new BoxSpace(BoxLimit.MATCHED_LOAD, epsr,Double.NaN);
    }

    public static BoxSpace shortCircuit(double epsr,double width){
        return new BoxSpace(BoxLimit.SHORT, epsr,width);
    }

    public static BoxSpace nothing(){
        return new BoxSpace(BoxLimit.NOTHING, 1,Double.NaN);
    }
}
