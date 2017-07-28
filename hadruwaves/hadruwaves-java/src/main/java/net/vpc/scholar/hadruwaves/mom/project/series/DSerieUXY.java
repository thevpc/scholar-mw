package net.vpc.scholar.hadruwaves.mom.project.series;

import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.FunctionFactory;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 11 août 2005
 * Time: 14:49:16
 * To change this template use File | Settings | File Templates.
 */
public class DSerieUXY extends DSerieDXY implements Cloneable {
    public DSerieUXY(Domain domain) {
        super(domain);
    }

    public DoubleToDouble getFunction(int n) {
        return FunctionFactory.uXY(n,getDomain());
    }

    public int getMinIndex() {
        return 0;
    }

    public int getMaxIndex() {
        return getMaxXIndex();
    }
}
