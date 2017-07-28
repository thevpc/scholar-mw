package net.vpc.scholar.hadruwaves.mom.project.series;

import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.Domain;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 11 août 2005
 * Time: 14:42:31
 * To change this template use File | Settings | File Templates.
 */
public abstract class CSerieXY implements Cloneable{
    private Domain domain;

    public CSerieXY(Domain domain) {
        this.domain = domain;
    }

    public abstract DoubleToComplex getFunction(int n);
    public abstract int getMinIndex();
    public abstract int getMaxIndex();


    public abstract int getMaxXIndex();
    public abstract int getMaxYIndex();

    public abstract void setMaxXIndex(int x);
    public abstract void setMaxYIndex(int y);

    public CSerieXY clone(){
        try {
            return (CSerieXY) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public Domain getDomain() {
        return domain;
    }

    public int getCount(){
      return getMaxIndex()-getMinIndex()+1;
    }
}
