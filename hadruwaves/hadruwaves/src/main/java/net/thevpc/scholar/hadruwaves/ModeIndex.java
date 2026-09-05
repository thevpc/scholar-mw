package net.thevpc.scholar.hadruwaves;


import net.thevpc.common.swing.util.LRUMap;
import net.thevpc.nuts.elem.NElement;

import net.thevpc.nuts.elem.NNumberElement;
import net.thevpc.nuts.elem.NTupleElement;
import net.thevpc.nuts.util.*;
import net.thevpc.scholar.hadrumaths.HSerializable;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;

import java.io.ObjectStreamException;
import java.util.List;


public final class ModeIndex implements HSerializable {
    public final ModeType mtype;
    public final int m;
    public final int n;
    private static boolean cacheEnabled =true;
    private static int cacheSize =10000;
    private static final net.thevpc.scholar.hadrumaths.util.HLRUMap<ModeIndex, ModeIndex> cache = net.thevpc.scholar.hadrumaths.util.HLRUMap.of(cacheSize);

    @SuppressWarnings("unchecked")
    public static NOptional<ModeIndex> parse(NElement s) {
        if (s == null) {
            return NOptional.ofNamedEmpty("mode");
        }
        List<NElement> params = null;
        String name = null;
        try {
            java.lang.reflect.Method m = s.getClass().getMethod("params");
            params = (List<NElement>) m.invoke(s);
            java.lang.reflect.Method mName = s.getClass().getMethod("name");
            Object no = mName.invoke(s);
            if (no instanceof NOptional) {
                name = (String) ((NOptional<?>) no).orNull();
            } else if (no != null) {
                name = no.toString();
            }
        } catch (Throwable ex) {
            // ignore
        }
        if (params != null && name != null) {
            NOptional<ModeType> mt = ModeType.parse(name);
            if (mt.isPresent() && params.size() == 2) {
                NOptional<Integer> m = params.get(0).asIntValue();
                NOptional<Integer> n = params.get(1).asIntValue();
                if (m.isPresent() && n.isPresent()) {
                    return NOptional.of(mode(mt.get(), m.get(), n.get()));
                }
            }
        }
        return NOptional.ofNamedError("mode");
    }

    public static NOptional<ModeIndex> parse(String s) {
        if(s==null){
            return NOptional.ofNamedEmpty("mode");
        }
        String lo = s.toLowerCase();
        ModeType mtype;
        if(lo.startsWith("tem")) {
            mtype = ModeType.TEM;
            lo = lo.substring(3);
        }else if(lo.startsWith("te")){
            mtype = ModeType.TE;
            lo = lo.substring(2);
        }else if(lo.startsWith("tm")){
            mtype = ModeType.TM;
            lo = lo.substring(2);
        }else{
            return NOptional.ofNamedError("mode");
        }
        if(lo.startsWith("(") && lo.endsWith(")")){
            lo=lo.substring(1,lo.length()-1);
        }
        List<String> z = NStringUtils.split(lo, "_,/:",true,true);
        if(z.size()==2){
            NOptional<Integer> m = NLiteral.of(z.get(0)).asInt();
            NOptional<Integer> n = NLiteral.of(z.get(1)).asInt();
            if(m.isPresent() && n.isPresent()){
                return NOptional.of(mode(mtype,m.get(),n.get()));
            }
        }
        return NOptional.ofNamedError("mode");
    }

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
    public NElement toElement() {
        return NElementHelper.ofNamedTuple(mtype.name(),NElement.ofInt(m),NElement.ofInt(n));
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
