package net.thevpc.scholar.hadrumaths.test.format;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.CosXCosY;

import static net.thevpc.scholar.hadrumaths.Maths.mul;

public class TestToString {




    @org.junit.jupiter.api.Test
    public void testToString1() {
        CosXCosY c = new CosXCosY(1, 0, 0, 0, 0, Domain.ofBounds(0, 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        System.out.println(c);
    }

}
