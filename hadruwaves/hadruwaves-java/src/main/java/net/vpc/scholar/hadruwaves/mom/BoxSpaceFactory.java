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
        return new BoxSpace(BoxLimit.OPEN, epsr,width,0);
    }

    public static BoxSpace openCircuit(double epsr,double width,double electricConductivity){
        return new BoxSpace(BoxLimit.OPEN, epsr,width,electricConductivity);
    }

    public static BoxSpace matchedLoad(double epsr){
        return new BoxSpace(BoxLimit.MATCHED_LOAD, epsr,Double.NaN,0);
    }

    public static BoxSpace matchedLoad(double epsr,double electricConductivity){
        return new BoxSpace(BoxLimit.MATCHED_LOAD, epsr,Double.NaN,electricConductivity);
    }

    public static BoxSpace shortCircuit(double epsr,double width){
        return new BoxSpace(BoxLimit.SHORT, epsr,width,0);
    }
    public static BoxSpace shortCircuit(double epsr,double width,double electricConductivity){
        return new BoxSpace(BoxLimit.SHORT, epsr,width,electricConductivity);
    }

    public static BoxSpace nothing(){
        return new BoxSpace(BoxLimit.NOTHING, 1,Double.NaN,0);
    }
}
