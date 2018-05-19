package net.vpc.scholar.hadrumaths.derivation.formal;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.derivation.FunctionDifferentiatorManager;
import net.vpc.scholar.hadrumaths.symbolic.DDxyAbstractSum;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 6 juil. 2007 12:36:49
 */
public class DAbstractSumFunctionXYDerivator {

    public Expr derive(Expr f, Axis varIndex, FunctionDifferentiatorManager d) {
        DDxyAbstractSum c = (DDxyAbstractSum) f;
        DoubleToDouble[] s = c.getSegments();
        DoubleToDouble[] s2 = new DoubleToDouble[s.length];
        for (int i = 0; i < s.length; i++) {
            s2[i] = d.derive(s[i], varIndex).toDD();
        }
        return Maths.sum(s2);
    }
}