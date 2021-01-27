package net.thevpc.scholar.hadruwaves.mom.project.series;

import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.thevpc.scholar.hadrumaths.Domain;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 11 août 2005
 * Time: 14:42:31
 * To change this template use File | Settings | File Templates.
 */
public abstract class DSerieDXY implements Cloneable{
    private Domain domain;
    private int maxXIndex=0;
    private int maxYIndex=0;

    public DSerieDXY(Domain domain) {
        this.domain = domain;
    }

    public abstract DoubleToDouble getFunction(int n);
    public abstract int getMinIndex();
    public abstract int getMaxIndex();

    public DSerieDXY clone(){
        try {
            return (DSerieDXY) super.clone();
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

    public int getMaxXIndex() {
        return maxXIndex;
    }

    public void setMaxXIndex(int maxXIndex) {
        this.maxXIndex = maxXIndex;
    }

    public int getMaxYIndex() {
        return maxYIndex;
    }

    public void setMaxYIndex(int maxYIndex) {
        this.maxYIndex = maxYIndex;
    }

}
