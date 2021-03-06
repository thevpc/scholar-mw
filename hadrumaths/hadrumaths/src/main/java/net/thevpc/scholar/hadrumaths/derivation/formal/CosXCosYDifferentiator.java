package net.thevpc.scholar.hadrumaths.derivation.formal;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.FunctionFactory;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.derivation.FunctionDifferentiator;
import net.thevpc.scholar.hadrumaths.derivation.FunctionDifferentiatorManager;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.CosXCosY;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 6 juil. 2007 10:04:00
 */
public class CosXCosYDifferentiator implements FunctionDifferentiator {
    public Expr derive(Expr f, Axis varIndex, FunctionDifferentiatorManager d) {
        CosXCosY c = (CosXCosY) f;
        switch (varIndex) {
            case X: {
                return new CosXCosY(c.getAmp() * c.getA(), c.getA(), c.getB() - Maths.PI / 2, c.getC(), c.getD(), c.getDomain());
            }
            case Y: {
                return new CosXCosY(c.getAmp() * c.getC(), c.getA(), c.getB(), c.getC(), c.getD() - Maths.PI / 2, c.getDomain());
            }
        }
        return Maths.DZEROXY;
    }
}
