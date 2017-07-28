package net.vpc.scholar.hadruwaves.mom.sources.modal;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;
import net.vpc.scholar.hadruwaves.mom.ModeFunctions;
import net.vpc.scholar.hadruwaves.mom.sources.Sources;
import static net.vpc.scholar.hadruwaves.Physics.lambda;

import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 * 0 value means computed
 *
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 8 mars 2007 16:22:56
 */
public abstract class AbstractPropagatingModalSources implements ModalSources {
    // 0 means computed
    private int defaultSourceCount;
    private int[] sourceCountPerDimension;

    public AbstractPropagatingModalSources(int defaultSourceCount, int... sourceCountPerDimension) {
        this.defaultSourceCount = defaultSourceCount;
        this.sourceCountPerDimension = sourceCountPerDimension;
//        if(defaultSourceCount>=sourceCountPerDimension.length){
//            throw new IllegalArgumentException("Too few S")
//        }
    }

    public int getSourceCountForDimensions(ModeFunctions fn) {
        int x = ((int) (Math.max(fn.getDomain().xwidth(),fn.getDomain().ywidth()) / lambda(fn.getFrequency()))) + 1;
        int val = (x < 0 || x >= this.sourceCountPerDimension.length) ? this.defaultSourceCount : this.sourceCountPerDimension[x - 1];
        if (val == 0) {
            return x;
        } else if (val > 0) {
            return val;
        } else {
            throw new RuntimeException("Unexpected mode value " + val);
        }
    }

    @Override
    public String toString() {
        return dump();
    }

    public Dumper getDumpStringHelper() {
        Dumper h=new Dumper(this,Dumper.Type.SIMPLE);
        h.add("default", defaultSourceCount == 0 ? "%scale" : defaultSourceCount);
        if (sourceCountPerDimension.length > 0) {
            StringBuilder sb=new StringBuilder("");
            for (int i = 0; i < sourceCountPerDimension.length; i++) {
                if (i > 0) {
                    sb.append(",");
                }
                if(sourceCountPerDimension[i]>0){
                    sb.append(sourceCountPerDimension[i]);
                }else{
                    sb.append("*");
                }
            }
            //bydimSourceCount
            h.add("bydim",sb.toString());
        }
        return h;
    }
    public final String dump() {
        return getDumpStringHelper().toString();
    }

    @Override
    public Sources clone() {
        try {
            AbstractPropagatingModalSources s = (AbstractPropagatingModalSources) super.clone();
            int[] o = new int[sourceCountPerDimension.length];
            System.arraycopy(s.sourceCountPerDimension, 0, o, 0, s.sourceCountPerDimension.length);
            s.sourceCountPerDimension = o;
            return s;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(AbstractPropagatingModalSources.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
    
    public void setStructure(MomStructure str) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getDefaultSourceCount() {
        return defaultSourceCount;
    }

    public int[] getSourceCountPerDimension() {
        int[] cpy=new int[sourceCountPerDimension.length];
        System.arraycopy(sourceCountPerDimension,0,cpy,0,sourceCountPerDimension.length);
        return cpy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractPropagatingModalSources that = (AbstractPropagatingModalSources) o;

        if (defaultSourceCount != that.defaultSourceCount) return false;
        return Arrays.equals(sourceCountPerDimension, that.sourceCountPerDimension);
    }

    @Override
    public int hashCode() {
        int result = defaultSourceCount;
        result = 31 * result + Arrays.hashCode(sourceCountPerDimension);
        return result;
    }
}
