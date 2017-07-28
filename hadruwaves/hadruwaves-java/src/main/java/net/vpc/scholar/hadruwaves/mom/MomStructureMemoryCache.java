package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.cache.HashValue;

import java.util.WeakHashMap;

/**
 * @author taha.bensalah@gmail.com on 7/21/16.
 */
public class MomStructureMemoryCache {
//    transient Domain cachedDomain;
//    transient Matrix cachedZin;
//    transient Matrix cachedMatrixA;
//    transient Matrix cachedMatrixB;
//    transient Matrix cachedMatrixUnknown;
//    transient ScalarProductCache cachedGpFnSp;
//    transient ScalarProductCache cachedGpSrcSp;
    WeakHashMap<String,Object> values=new WeakHashMap<String, Object>();
    HashValue hash;
    MomStructure momStructure;

    public MomStructureMemoryCache(MomStructure momStructure) {
        this.momStructure = momStructure;
    }

    public boolean dirty(){
        HashValue strHash = momStructure.hashValue();
        if(!strHash.equals(this.hash)){
            reset();
            this.hash= strHash;
            return true;
        }
        return false;
    }

    public void reset(){
        values.clear();
        hash=null;
    }
    public Object get(String name){
        HashValue strHash = momStructure.hashValue();
        if(strHash.equals(this.hash)){
            return values.get(name);
        }
        return null;
    }

    public void set(String name,Object value){
        HashValue strHash = momStructure.hashValue();
        if(strHash.equals(this.hash)){
            values.put(name, value);
        }else{
            values.clear();
            this.hash=strHash;
            values.put(name, value);
        }
    }
}
