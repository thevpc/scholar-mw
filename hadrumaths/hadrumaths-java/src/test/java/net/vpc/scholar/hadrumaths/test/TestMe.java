package net.vpc.scholar.hadrumaths.test;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.CosXCosY;

public class TestMe {
    public static void main(String[] args) {
        CosXCosY c = new CosXCosY(1, 0, 0, 0, 0, Domain.forBounds(0, 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        System.out.println(c);
    }
}
