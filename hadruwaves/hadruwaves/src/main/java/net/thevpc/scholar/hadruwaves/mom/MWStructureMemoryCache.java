package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.scholar.hadrumaths.cache.CacheKey;
import net.thevpc.scholar.hadruwaves.str.MWStructure;

import java.util.WeakHashMap;

/**
 * @author taha.bensalah@gmail.com on 7/21/16.
 */
public class MWStructureMemoryCache {
    WeakHashMap<String,Object> values=new WeakHashMap<String, Object>();
    CacheKey hash;
    MWStructure momStructure;

    public MWStructureMemoryCache(MWStructure momStructure) {
        this.momStructure = momStructure;
    }

    public boolean dirty(){
        CacheKey strHash = momStructure.getKey();
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
        CacheKey strHash = momStructure.getKey();
        if(strHash.equals(this.hash)){
            return values.get(name);
        }
        return null;
    }

    public void set(String name,Object value){
        CacheKey strHash = momStructure.getKey();
        if(strHash.equals(this.hash)){
            values.put(name, value);
        }else{
            values.clear();
            this.hash=strHash;
            values.put(name, value);
        }
    }
}
