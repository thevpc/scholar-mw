/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadruwaves.mom.project;

import java.util.ArrayList;
import java.util.List;
import net.vpc.scholar.hadrumaths.util.config.Configuration;

/**
 *
 * @author vpc
 */
public class MomProjectList<T extends MomProjectItem> implements MomProjectItem{
    private List<T> list=new ArrayList<T>();
    private MomProject context;

    public MomProjectList() {
        
    }
    
    public MomProjectList(List list) {
        this.list = list;
    }

    public MomProjectItem create() {
        return new MomProjectList<T>();
    }

    public String getId() {
        return "MomProjectList";
    }

    public void load(Configuration conf, String prefix) {
        list=new ArrayList<T>();
        int i = 1;
        while (true) {
            String key = prefix + ".[" + i + "]";
            T loaded=(T)MomProjectFactory.INSTANCE.load(conf, key);
            if (loaded == null) {
                break;
            }
            list.add(loaded);
            i++;
        }
    }

    public void store(Configuration c, String key) {
        int i = 1;
        for (T object : list) {
            String key2 = key + ".[" + i + "]";
            MomProjectFactory.INSTANCE.store(c, key2,object);
            i++;
        }
    }

    public MomProject getContext() {
        return context;
    }

    public void recompile() {
        
    }

    public void setContext(MomProject context) {
        this.context=context;
    }
    
    public List<T> getList(){
        return list;
    }
    
}
