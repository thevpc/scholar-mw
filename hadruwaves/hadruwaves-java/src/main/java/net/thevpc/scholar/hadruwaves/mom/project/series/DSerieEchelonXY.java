package net.thevpc.scholar.hadruwaves.mom.project.series;

import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.thevpc.scholar.hadrumaths.Domain;

import static net.thevpc.scholar.hadrumaths.Maths.*;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 11 août 2005
 * Time: 14:49:16
 * To change this template use File | Settings | File Templates.
 */
public class DSerieEchelonXY extends DSerieDXY implements Cloneable {
    public DSerieEchelonXY(Domain domain) {
        super(domain);
    }

    public DoubleToDouble getFunction(int n) {
        return expr(Math.sqrt(1 / getDomain().xwidth()),
                getDomain()
        );
    }

    public int getMinIndex() {
        return 0;
    }

    public int getMaxIndex() {
        return 0;
    }
}
