package net.vpc.scholar.hadruwaves.mom.project.common;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadruwaves.mom.project.MomProjectItem;
import net.vpc.scholar.hadrumaths.util.config.Configuration;
import net.vpc.common.util.MinMax;
import net.vpc.scholar.hadruwaves.mom.project.MomProject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;
import net.vpc.scholar.hadruwaves.mom.project.MomProjectFactory;
import net.vpc.scholar.hadruwaves.mom.project.MomProjectList;
import net.vpc.scholar.hadruwaves.mom.project.gpmesher.GpMesher;

/**
 * User: vpc
 * Date: 22 juin 2005
 * Time: 13:48:49
 */
public class AreaGroup implements AreaZone, Cloneable {

    protected MomProject context;
    protected MomProject project;
    protected AreaGroup parentGroup;
    protected String name;
    protected String desc;
    protected ArrayList<AreaGroup> areaGroupsList = new ArrayList<AreaGroup>();
    protected ArrayList<Area> areas = new ArrayList<Area>();
    public static final String ROOT_PATH = "";
    private GpMesher gpMesher;

    public AreaGroup() {
        
    }
    
    public AreaGroup(String name, MomProject project) {
        this.name = name;
        this.project = project;
        this.context = project==null?null:project;
    }

    
    public MomProject getProject() {
        return project;
    }

    public void setProject(MomProject project) {
        this.project = project;
        this.context = project==null?null:project;
        for (AreaGroup areaGroup : new ArrayList<AreaGroup>(areaGroupsList)) {
            areaGroup.setProject(project);
        }
        for (Area area : new ArrayList<Area>(areas)) {
            area.setProject(project);
        }
    }

    public AreaGroup getParentGroup() {
        return parentGroup;
    }

    public AreaGroup getTopLevelGroup() {
        if (isTopLevel()) {
            return this;
        }
        AreaGroup p = this;
        while (p != null) {
            if (p.isTopLevel()) {
                return p;
            }
            p = p.getParentGroup();
        }
        return null;
    }

    public boolean isRoot() {
        return ROOT_PATH.equals(getPath());
    }

    public boolean isTopLevel() {
        AreaGroup g = getParentGroup();
        return g != null && g.isRoot();
    }

    public void setParentGroup(AreaGroup parentGroup) {
//        if (this.parentGroup != null) {
//            for (Iterator<Area> i = areas.iterator(); i.hasNextImpl();) {
//                Area area = i.next();
//                removeArea(area);
//            }
//            for (Iterator<AreaGroup> i = children.iterator(); i.hasNextImpl();) {
//                removeGroup(i.next());
//            }
//            this.parentGroup.children.removeProperty(this);
//        }
        this.parentGroup = parentGroup;
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Empty Name not allowed");
        }
        if (name.equals(this.name)) {
            return;
        }
        if (parentGroup != null) {
            int x = parentGroup.indexOfGroupByName(this.name);
            int y = parentGroup.indexOfGroupByName(name);
            if (y == -1) {
                this.name = name;
                project.fireProperty(MomProject.AREAGROUP_UPDATED, null, this);
            return;
            }

        } else {
            this.name = name;
            project.fireProperty(MomProject.AREAGROUP_UPDATED, null, this);
            return;
        }
        throw new IllegalArgumentException("name already exists");
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPath() {
        if (parentGroup == null) {
            return AreaGroup.ROOT_PATH;
        }
        String s = getParentGroup().getPath();
        if (!s.endsWith("/")) {
            s = s + "/";
        }
        return s + getName();
    }

    public String getName() {
        return name;
    }

    public int indexOfGroupByName(String name) {
        for (int i = 0; i < areaGroupsList.size(); i++) {
            if (areaGroupsList.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public boolean containsZone(AreaZone zone) {
        for (int i = 0; i < areaGroupsList.size(); i++) {
            if (areaGroupsList.get(i) == zone) {
                return true;
            }
        }
        for (int i = 0; i < areas.size(); i++) {
            if (areas.get(i) == zone) {
                return true;
            }
        }
        return false;
    }

    public AreaGroup removeGroup(int index) {
        AreaGroup z = areaGroupsList.remove(index);
        if (z != null) {
            z.setParentGroup(null);
            z.setProject(null);
            project.fireProperty(MomProject.AREAGROUP_REMOVED, z, this);
        }
        return z;
    }

    public boolean removeGroupButRetainChildren() {
        AreaGroup p = getParentGroup();
        if (p == null && project != null) {
            p = project.getRootAreaGroup();
        }
        if (p != null) {
            if (p.removeAreaGroup(this)) {
                for (AreaGroup areaGroup : new ArrayList<AreaGroup>(areaGroupsList)) {
                    p.addAreaGroup(areaGroup);
                }
                for (Area area : new ArrayList<Area>(areas)) {
                    p.addArea(area);
                }
                return true;
            }
        }
        return false;
    }

    public AreaGroup getGroup(int index) {
        return areaGroupsList.get(index);
    }

    public int getAreaZoneSize() {
        return areaGroupsList.size() + areas.size();
    }

    public AreaZone removeAreaZone(int index) {
        AreaZone areaZone = getAreaZone(index);
        if (areaZone instanceof Area) {
            removeArea((Area) areaZone);
        } else if (areaZone instanceof AreaGroup) {
            removeAreaGroup((AreaGroup) areaZone);
        }
        return areaZone;
    }

    public AreaZone getAreaZone(int index) {
        if (index < areaGroupsList.size()) {
            return areaGroupsList.get(index);
        } else {
            return areas.get(index - areaGroupsList.size());
        }
    }

    public int getAreaZoneIndex(AreaZone areaZone) {
        int i = areaGroupsList.indexOf((AreaGroup) areaZone);
        if (i >= 0) {
            return i;
        }
        i = areas.indexOf((Area) areaZone);
        if (i >= 0) {
            return i + areaGroupsList.size();
        }
        return -1;
    }

    public void addAreaZone(AreaZone areaZone) {
        if (areaZone instanceof Area) {
            addArea((Area) areaZone);
        } else {
            addAreaGroup((AreaGroup) areaZone);
        }
    }

    public int getGroupsCount() {
        return areaGroupsList.size();
    }

    public void addArea(Area area, int index) {
        if (!areas.contains(area)) {
            area.setParentGroup(this);
            area.setProject(project);
            areas.add(index, area);
            area.setProject(project);
            if (project != null) {
                project.fireProperty(MomProject.AREA_ADDED, area, index);
            }
        }
    }

    public void addArea(Area area) {
        area.setParentGroup(this);
        area.setProject(project);
        areas.add(area);
        if (project != null) {
            area.setProject(project);
            project.fireProperty(MomProject.AREA_ADDED, area, areas.size() - 1);
        }
    }

    public void updateArea(Area oldArea, Area newArea) {
        if (oldArea != newArea) {
            int j = areas.indexOf(oldArea);
            oldArea.setParentGroup(null);
            oldArea.setProject(null);
            newArea.setParentGroup(this);
            newArea.setProject(project);
            newArea.recompile();
            areas.set(j, newArea);
            if (project != null) {
                project.fireProperty(MomProject.AREA_UPDATED, oldArea, newArea);
            }
        }
    }

    public boolean removeArea(Area area) {
        if (areas.remove(area)) {
            area.setParentGroup(null);
            area.setProject(null);
            if (project != null) {
                project.fireProperty(MomProject.AREA_REMOVED, area, this);
            }
            return true;
        }
        return false;
    }

    public boolean remove() {
        if (parentGroup != null) {
            return parentGroup.removeAreaGroup(this);
        }
        return false;
    }

    public boolean removeAreaGroup(AreaGroup group) {
        if (areaGroupsList.remove(group)) {
            group.setParentGroup(null);
            group.setProject(null);
            project.fireProperty(MomProject.AREAGROUP_REMOVED, group, this);
            return true;
        }
        return false;
    }

    public Area removeArea(int index) {
        Area area = areas.remove(index);
        area.setParentGroup(null);
        area.setProject(null);
        if (project != null) {
            project.fireProperty(MomProject.AREA_REMOVED, area, this);
        }
        return area;
    }

    public Area getArea(int index) {
        return areas.get(index);
    }

    public int getAreasCount() {
        return areas.size();
    }

    public AreaGroup addNewGroup() {
        for (int i = 1;; i++) {
            if (indexOfGroupByName("group" + i) == -1) {
                AreaGroup g = new AreaGroup("group" + i, getProject());
                addAreaGroup(g);
                return g;
            }
        }
    }

    public String generateNewChildName() {
        for (int i = 1;; i++) {
            if (indexOfGroupByName("group" + i) == -1) {
                return "group" + i;
            }
        }
    }

    public boolean addAreaGroup(AreaGroup areaGroup) {
        if (indexOfGroupByName(areaGroup.getName()) == -1) {
            areaGroup.setParentGroup(this);
            areaGroup.setProject(project);
            areaGroupsList.add(areaGroup);
            if (project != null) {
                project.fireProperty(MomProject.AREAGROUP_ADDED, areaGroup, areaGroupsList.size() - 1);
            }
            return true;
        }
        return false;
    }

    public AreaGroup getGroupByPath(String path) {
        String myPath = getPath();
        if (myPath.equals(path)) {
            return this;
        }
        if (path.startsWith(myPath + "/")) {
            String[] paths = path.substring((myPath + "/").length()).split("/");
            AreaGroup parent = this;
            for (int i = 0; i < paths.length; i++) {
                boolean found = false;
                for (AreaGroup areaGroup : parent.areaGroupsList) {
                    if (areaGroup.getName().equals(paths[i])) {
                        parent = areaGroup;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return null;
                }
            }
            return parent;
        }
        return null;
    }

    public int indexOfArea(Area a) {
        return areas.indexOf(a);
    }

    public int indexOfAreaGroup(AreaGroup a) {
        return areaGroupsList.indexOf(a);
    }

    public Area[] getAllInnerAreas() {
        ArrayList<Area> all = new ArrayList<Area>();
        Stack<AreaGroup> stack = new Stack<AreaGroup>();
        stack.add(this);
        while (!stack.isEmpty()) {
            AreaGroup g = stack.pop();
            all.addAll(g.areas);
            stack.addAll(g.areaGroupsList);
        }
        return (Area[]) all.toArray(new Area[all.size()]);
    }

    @Override
    public AreaGroup clone() {
        try {
            AreaGroup g = (AreaGroup) super.clone();
            g.parentGroup = null;
            g.areaGroupsList = new ArrayList<AreaGroup>();
            g.areas = new ArrayList<Area>();
            for (AreaGroup areaGroup : areaGroupsList) {
                AreaGroup g1 = (AreaGroup) areaGroup.clone();
                g1.parentGroup = g;
                g.areaGroupsList.add(g1);
            }
            for (Area area : areas) {
                Area g1 = (Area) area.clone();
                g1.setParentGroup(g);
                g.areas.add(g1);
            }
            return g;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void setEnabled(boolean enabled) {
        for (AreaGroup areaGroup : areaGroupsList) {
            areaGroup.setEnabled(enabled);
        }
        for (Area area : areas) {
            area.setEnabled(enabled);
        }
    }

    public void recompile() {
        for (AreaGroup areaGroup : areaGroupsList) {
            areaGroup.recompile();
        }
        for (Area area : areas) {
            area.recompile();
        }
    }

    public void setWidth(double newWidth) {
        Domain d=getDomain();
        double oldX = d.xmin();
        double oldW = d.xwidth();
        for (AreaGroup areaGroup : areaGroupsList) {
            Domain agd=areaGroup.getDomain();
            double x = agd.xmin();
            double w = agd.xwidth();
            areaGroup.setX(oldX + (x - oldX) * newWidth / oldW);
            areaGroup.setWidth(w * newWidth / oldW);
        }
        for (Area area : areas) {
            Domain ad=area.getDomain();
            double x = ad.xmin();
            double w = ad.xwidth();
            area.setX(oldX + (x - oldX) * newWidth / oldW);
            area.setWidth(w * newWidth / oldW);
        }
    }

    public void setHeight(double newHeigth) {
        Domain d=getDomain();
        double oldY = d.ymin();
        double oldH = d.ywidth();
        for (AreaGroup areaGroup : areaGroupsList) {
            Domain agd=areaGroup.getDomain();
            double y = agd.ymin();
            double h = agd.ywidth();
            areaGroup.setY(oldY + (y - oldY) * newHeigth / oldH);
            areaGroup.setHeight(h * newHeigth / oldH);
        }
        for (Area area : areas) {
            Domain ad=area.getDomain();
            double y = ad.ymin();
            double h = ad.ywidth();
            area.setY(oldY + (y - oldY) * newHeigth / oldH);
            area.setHeight(h * newHeigth / oldH);
        }
    }

    public void setX(double newX) {
        double oldX = getDomain().xmin();
        for (AreaGroup areaGroup : areaGroupsList) {
            double x = areaGroup.getDomain().xmin();
            areaGroup.setX(x + (newX - oldX));
        }
        for (Area area : areas) {
            Domain ad=area.getDomain();
            double x = ad.xmin();
            area.setX(x + (newX - oldX));
        }
    }

    public void setY(double newY) {
        double oldY = getDomain().ymin();
        for (AreaGroup areaGroup : areaGroupsList) {
            double y = areaGroup.getDomain().ymin();
            areaGroup.setY(y + (newY - oldY));
        }
        for (Area area : areas) {
            Domain ad=area.getDomain();
            double y = ad.ymin();
            area.setY(y + (newY - oldY));
        }
    }

    public Collection<Area> findAreas(AreaFilter areaFilter) {
        ArrayList<Area> found = new ArrayList<Area>();
        for (Iterator<Area> i = areas.iterator(); i.hasNext();) {
            Area area = i.next();
            if (areaFilter.accept(area)) {
                found.add(area);
            }
        }
        for (Iterator<AreaGroup> i = areaGroupsList.iterator(); i.hasNext();) {
            AreaGroup areaGroup = i.next();
            found.addAll(areaGroup.findAreas(areaFilter));
        }
        return found;
    }

    public boolean contains(Area anyArea) {
        for (Iterator<Area> i = areas.iterator(); i.hasNext();) {
            Area area = i.next();
            if (anyArea == area) {
                return true;
            }
        }
        for (Iterator<AreaGroup> i = areaGroupsList.iterator(); i.hasNext();) {
            AreaGroup areaGroup = i.next();
            if (areaGroup.contains(anyArea)) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(AreaGroup anyAreaGroup) {
        for (Iterator<AreaGroup> i = areaGroupsList.iterator(); i.hasNext();) {
            AreaGroup areaGroup = i.next();
            if (anyAreaGroup == areaGroup) {
                return true;
            }
            if (areaGroup.contains(anyAreaGroup)) {
                return true;
            }
        }
        return false;
    }



    public GpMesher getGpMesher() {
        return gpMesher;
    }

    public Domain getDomain() {
        MinMax xminMax = new MinMax();
        MinMax yminMax = new MinMax();
        for (AreaGroup areaGroup : areaGroupsList) {
            Domain d=areaGroup.getDomain();
            xminMax.registerValue(d.xmin());
            xminMax.registerValue(d.xmax());
            yminMax.registerValue(d.ymin());
            yminMax.registerValue(d.ymax());
        }
        for (Area area : areas) {
            Domain d=area.getDomain();
            xminMax.registerValue(d.xmin());
            xminMax.registerValue(d.xmax());
            yminMax.registerValue(d.ymin());
            yminMax.registerValue(d.ymax());
        }
        return Domain.ofBounds(xminMax.getMin(), xminMax.getMax(), yminMax.getMin(), xminMax.getMax());
    }

    public void setGpMesher(GpMesher gpMesher) {
        this.gpMesher = gpMesher;
    }

    public MomProjectItem create() {
        return new AreaGroup();
    }

    public String getId() {
        return "AreaGroup";
    }

    public void load(Configuration c, String key) {
        name=c.getString(key+ ".name");
        desc=c.getString(key+ ".desc");
        parentGroup=null;
        gpMesher=(GpMesher)MomProjectFactory.INSTANCE.load(c, key + ".gpMesher");
        MomProjectList list;
        list=(MomProjectList)MomProjectFactory.INSTANCE.load(c, key + ".groups");
        for (Object o : list.getList()) {
            AreaGroup g=(AreaGroup)o;
            addAreaGroup(g);
        }
        list=(MomProjectList)MomProjectFactory.INSTANCE.load(c, key + ".areas");
        for (Object o : list.getList()) {
            Area a=(Area)o;
            addArea(a);
        }
    }
    
    public void store(Configuration c, String key) {
        c.setString(key + ".name", name);
        c.setString(key + ".desc", this.desc);
        c.setString(key + ".path", parentGroup == null ? "" : parentGroup.getPath());
        MomProjectFactory.INSTANCE.store(c, key + ".gpMesher", gpMesher);
        MomProjectFactory.INSTANCE.store(c, key + ".groups", new MomProjectList(areaGroupsList));
        MomProjectFactory.INSTANCE.store(c, key + ".areas", new MomProjectList(areas));
    }

    public MomProject getContext() {
        return context;
    }

    public void setContext(MomProject context) {
        this.context=context;
        if(gpMesher!=null){
            this.gpMesher.setContext(context);
        }
        for (Area area : areas) {
            area.setContext(context);
        }
        for (AreaGroup areaGroup : areaGroupsList) {
            areaGroup.setContext(context);
        }

    }
    
    
    
}
