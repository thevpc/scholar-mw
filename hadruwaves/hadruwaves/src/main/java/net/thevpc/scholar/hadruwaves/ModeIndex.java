package net.thevpc.scholar.hadruwaves;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.HSerializable;
import net.thevpc.scholar.hadrumaths.Maths;

import java.io.ObjectStreamException;
import net.thevpc.common.collections.LRUMap;

public final class ModeIndex implements HSerializable {
    public final ModeType mtype;
    public final int m;
    public final int n;
    private static boolean cacheEnabled =true;
    private static int cacheSize =10000;
    private static final LRUMap<ModeIndex, ModeIndex> cache = new LRUMap<ModeIndex, ModeIndex>(cacheSize);

    private static boolean isCacheEnabled() {
        return cacheEnabled && Maths.Config.isCacheEnabled();
    }

    public static void setCachedEnabled(boolean cacheEnabled){
        synchronized (cache) {
            ModeIndex.cacheEnabled = cacheEnabled;
            if (cacheEnabled) {
                cache.resize(cacheSize);
            } else {
                cache.resize(0);
            }
        }
    }

    public static void updateCache(){
        synchronized (cache) {
            if (isCacheEnabled()) {
                cache.resize(cacheSize);
            } else {
                cache.resize(0);
            }
        }
    }

    public static void setCachedModeIndexSize(int size){
        synchronized (cache) {
            cacheSize=size;
            if(isCacheEnabled()) {
                cache.resize(cacheSize);
            }
        }
    }


//    public static void main(String[] args) {
//        long count=0;
//        Chronometer c= Maths.chrono();
//        for (int m = 0; m < 10000000; m++) {
//            for (int n = 0; n < 10000000; n++) {
//                for (Mode mod : ModeType.values()) {
//                    mode(mod,m,n);
//                    count++;
//                    if(count%10000000==0) {
//                        System.out.println(count + " : " + (c.getTime() * 10000000L / count));
//                    }
//                }
//            }
//        }
//    }

    public static ModeIndex mode(ModeType mode, int m, int n) {
        return mode(new ModeIndex(mode, m, n));
    }

    public static ModeIndex mode(ModeIndex modeIndex0) {
        if(!isCacheEnabled()) {
            return modeIndex0;
        }
        synchronized (cache) {
            ModeIndex modeIndex = cache.get(modeIndex0);
            if (modeIndex != null) {
                return modeIndex;
            }
            return modeIndex0;
        }
    }

    private ModeIndex(ModeType mtype, int m, int n) {
        this.mtype = mtype;
        this.m = m;
        this.n = n;
    }

    public ModeType getModeType() {
        return mtype;
    }

    public int getM() {
        return m;
    }

    public int getN() {
        return n;
    }

    public int n() {
        return n;
    }

    public int m() {
        return m;
    }

    public ModeType type() {
        return mtype;
    }

//    public String dump() {
//        return toString();
//    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModeIndex)) return false;

        ModeIndex modeIndex = (ModeIndex) o;

        if (m != modeIndex.m) return false;
        if (n != modeIndex.n) return false;
        if (mtype != modeIndex.mtype) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (mtype != null ? mtype.hashCode() : 0);
        result = 31 * result + m;
        result = 31 * result + n;
        return result;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.ofFunction(mtype.name(),Tson.of(m),Tson.of(n)).build();
    }
    public String toString() {
        return  mtype.toString()+m+"."+n;
    }

    private Object readResolve() throws ObjectStreamException {
        return mode(this);
    }

    public ModeIndex intern(){
        return mode(this);
    }

}
