package net.vpc.scholar.hadruwaves.mom.project.series;

import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.Maths;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 11 août 2005
 * Time: 15:04:40
 * To change this template use File | Settings | File Templates.
 */
public class CSerieDefaultXY extends CSerieXY implements Cloneable {
    DSerieDXY base;

    public CSerieDefaultXY(DSerieDXY base) {
        super(base.getDomain());
        this.base = base;
    }

    public DoubleToComplex getFunction(int n) {
        return Maths.complex(base.getFunction(n));
    }

    public int getMinIndex() {
        return base.getMinIndex();
    }

    public int getMaxIndex() {
        return base.getMaxIndex();
    }

    public DSerieDXY getBaseSerie() {
        return base;
    }

    public int getMaxXIndex() {
        return base.getMaxXIndex();
    }

    public void setMaxXIndex(int x) {
        base.setMaxXIndex(x);
    }

    public int getMaxYIndex() {
        return base.getMaxYIndex();
    }

    public void setMaxYIndex(int y) {
        base.setMaxYIndex(y);
    }

}
