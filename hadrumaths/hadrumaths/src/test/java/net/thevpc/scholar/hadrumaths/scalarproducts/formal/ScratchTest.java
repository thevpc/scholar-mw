package net.thevpc.scholar.hadrumaths.scalarproducts.formal;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.*;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.CosXCosY;
import org.junit.jupiter.api.Test;

public class ScratchTest {
    @Test
    public void testScratch() {
        try {
            Maths.Config.setCacheEnabled(false);
            Domain d = Domain.ofBounds(0, 2 * Math.PI);
            
            // f1opt is cos(x)
            CosXCosY f = new CosXCosY(1, 1, 0, 0, 0, d);
            // f2opt is sin(x) = cos(x - pi/2)
            CosXCosY g = new CosXCosY(1, 1, -Math.PI / 2, 0, 0, d);
            
            Domain inter = f.getDomain().intersect(g.getDomain()).intersect(d);
            System.out.println("inter = " + inter);
            
            Domain d2 = Domain.ofBounds(inter.xmin(), inter.xmax(), 0, 1);
            
            double b1 = d2.xmin();
            double b2 = d2.xmax();
            double b3 = d2.ymin();
            double b4 = d2.ymax();
            System.out.println("b1=" + b1 + " b2=" + b2 + " b3=" + b3 + " b4=" + b4);
            
            double fa = f.getA();
            double fb = f.getB();
            double fc = f.getC();
            double fd = f.getD();
            double famp = f.getAmp();

            double ga = g.getA();
            double gb = g.getB();
            double gc = g.getC();
            double gd = g.getD();
            double gamp = g.getAmp();
            
            System.out.println("fa=" + fa + " fb=" + fb + " fc=" + fc + " fd=" + fd + " famp=" + famp);
            System.out.println("ga=" + ga + " gb=" + gb + " gc=" + gc + " gd=" + gd + " gamp=" + gamp);
            
            double sum1 = (2.0 * Maths.cos2(fb - gb) * b1 * fa + Maths.sin2(2.0 * fa * b1 + fb + gb));
            double sum2 = (2.0 * Maths.cos2(fb - gb) * b2 * fa + Maths.sin2(2.0 * fa * b2 + fb + gb));
            System.out.println("sum1(b1) = " + sum1);
            System.out.println("sum2(b2) = " + sum2);
            
            double d_0 = CosCosVsCosCosScalarProduct.INSTANCE.eval(d2, f, g, null);
            System.out.println("Result = " + d_0);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
