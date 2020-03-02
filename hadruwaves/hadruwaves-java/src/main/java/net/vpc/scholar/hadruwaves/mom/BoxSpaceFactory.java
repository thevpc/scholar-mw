/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.AbstractFactory;
import net.vpc.scholar.hadruwaves.Material;

/**
 * @author vpc
 */
public class BoxSpaceFactory extends AbstractFactory {

    private BoxSpaceFactory() {
    }

    public static BoxSpace openCircuit(Material material, double width) {
        return new BoxSpace(BoxLimit.OPEN, material, width);
    }

    public static BoxSpace matchedLoad() {
        return matchedLoad(Material.VACUUM);
    }

    public static BoxSpace matchedLoad(Material material) {
        return new BoxSpace(BoxLimit.MATCHED_LOAD, material, Double.NaN);
    }

    public static BoxSpace shortCircuit(Material material, double width) {
        return new BoxSpace(BoxLimit.SHORT, material, width);
    }

    public static BoxSpace nothing() {
        return new BoxSpace(BoxLimit.NOTHING, Material.VACUUM, Double.NaN);
    }
}
