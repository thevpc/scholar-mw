package net.vpc.scholar.hadruwaves.mom.project.series;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.AbstractDoubleToDouble;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 11 août 2005
 * Time: 14:49:16
 * To change this template use File | Settings | File Templates.
 */
public class DSerieSineMagneticXY extends DSerieDXY implements Cloneable {
    private int max;
    public DSerieSineMagneticXY(Domain domain,int max) {
        super(domain);
        this.max=max;
    }

    public AbstractDoubleToDouble getFunction(int n) {
        throw new IllegalArgumentException("Not yet implemented");
        //return Physics.fnMagnetic(n, getDomain());
    }

    public int getMinIndex() {
        return 0;
    }

    public int getMaxIndex() {
        return max;
    }
}
