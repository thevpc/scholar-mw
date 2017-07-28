/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadruwaves.mom.console.params;

import net.vpc.scholar.hadrumaths.Param;
import net.vpc.scholar.hadrumaths.plot.console.params.CoupleValue;
import net.vpc.scholar.hadrumaths.plot.console.params.ParamSet;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 * permit to specify different Params for Reference and Modeled Structures
 * @author vpc
 */
public class TargetParamSet<T> extends ParamSet<CoupleValue<T>> {
    private ParamSet<T> paramRef;
    private ParamSet<T> paramMod;

    public TargetParamSet(final ParamSet<T> ref, ParamSet<T> mod) {
        super(new Param() {
            @Override
            public String getName() {
                return "DistinctParam(" + ref.getName() + ")";
            }

            @Override
            public void configure(Object source, Object value) {
                throw new RuntimeException("Unsupported");
            }
        });
        this.paramRef = ref;
        this.paramMod = mod;
    }

    @Override
    protected int getSizeImpl() {
        return paramRef.getSize();
    }

    @Override
    protected CoupleValue<T> getValueImpl() {
       return new CoupleValue<T>(paramRef.getValue(),(paramMod==null)?null:paramMod.getValue());
    }

    @Override
    protected CoupleValue<T> getValueImpl(int index) {
       return new CoupleValue<T>(paramRef.getValue(index),(paramMod==null)?null:paramMod.getValue(index));
    }

    @Override
    protected boolean hasNextImpl() {
        boolean b = paramRef.hasNext();
        if (paramMod != null) {
            paramMod.hasNext();
        }
        return b;
    }

    @Override
    protected CoupleValue<T> nextImpl() {
        T t1 = (T)paramRef.next();
        T t2=null;
        if (paramMod != null) {
            paramMod.next();
        }
        return new CoupleValue<T>(t1, t2);
    }

    @Override
    protected void resetImpl() {
        paramRef.reset();
        if (paramMod != null) {
            paramMod.reset();
        }
    }

    @Override
    public void setParameter(Object source) {
        switch (((MomStructure) source).getTarget()) {
            case REFERENCE: {
                paramRef.setParameter(source);
                break;
            }
            case MODELED: {
                if (paramMod != null) {
                    paramMod.setParameter(source);
                } else {
                    paramRef.setParameter(source);
                }
                break;
            }
        }
    }
}
