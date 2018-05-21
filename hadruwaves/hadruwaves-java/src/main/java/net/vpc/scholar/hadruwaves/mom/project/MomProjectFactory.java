/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.project;

import java.util.*;

import net.vpc.scholar.hadruwaves.mom.project.areamaterial.ModalSourceMaterial;
import net.vpc.scholar.hadruwaves.mom.project.areamaterial.PecMaterial;
import net.vpc.scholar.hadruwaves.mom.project.areamaterial.PlanarSourceMaterial;
import net.vpc.scholar.hadruwaves.mom.project.areamaterial.SurfaceImpedanceMaterial;
import net.vpc.scholar.hadruwaves.mom.project.common.Area;
import net.vpc.scholar.hadruwaves.mom.project.common.AreaGroup;
import net.vpc.scholar.hadruwaves.mom.project.gpmesher.BoxModesGpMesher;
import net.vpc.scholar.hadruwaves.mom.project.gpmesher.ConstantGpMesher;
import net.vpc.scholar.hadruwaves.mom.project.gpmesher.RooftopGpMesher;
import net.vpc.scholar.hadruwaves.mom.project.shapes.RectAreaShape;
import net.vpc.scholar.hadrumaths.util.config.Configuration;

/**
 *
 * @author vpc
 */
public class MomProjectFactory {

    public static final MomProjectFactory INSTANCE = new MomProjectFactory(true);
    private Map<String, MomProjectItem> items = new HashMap<String, MomProjectItem>();

    public MomProjectFactory(boolean doRegisterDefaults) {
        if(doRegisterDefaults){
            registerDefaults();
        }
    }

    
    public void registerDefaults() {
        register(new MomProjectList());
        register(new RectAreaShape());
        register(new BoxModesGpMesher());
        register(new RooftopGpMesher());
        register(new ConstantGpMesher());
        
        register(new PecMaterial());
        register(new ModalSourceMaterial());
        register(new PlanarSourceMaterial());
        register(new SurfaceImpedanceMaterial());
        register(new Area());
        register(new AreaGroup());
        register(new MomProjectExtraLayer());
    }

    public void register(MomProjectItem item) {
        items.put(item.getId(), item);
    }

    public List<MomProjectItem> getAllByType(Class clz) {
        ArrayList<MomProjectItem> a = new ArrayList<MomProjectItem>();
        for (MomProjectItem momProjectItem : items.values()) {
            if (clz.isInstance(momProjectItem)) {
                a.add(momProjectItem.create());
            }
        }
        return a;
    }

    private MomProjectItem get(String id) {
        MomProjectItem ii = items.get(id);
        if (ii == null) {
            throw new IllegalArgumentException("Unknown MomProjectItem : " + id);
        }
        return ii;
    }

    public MomProjectItem create(String id) {
        return get(id).create();
    }

    public MomProjectItem load(Configuration conf, String key) {
        String tt = conf.getString(key + "._type");
        if (tt == null) {
            return null;
        }
        MomProjectItem ii = get(tt).create();
        ii.load(conf, key);
        return ii;
    }

    public void store(Configuration conf, String key, MomProjectItem item) {
        if (item == null) {
            conf.remove(key + "._type");
        } else {
            conf.setString(key + "._type", item.getId());
            item.store(conf, key);
        }
    }
}
