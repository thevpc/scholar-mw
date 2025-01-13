package net.thevpc.scholar.hadruwaves.mom.project.common;

import java.awt.BasicStroke;
import java.awt.Color;

import net.thevpc.scholar.hadrumaths.util.config.Configuration;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadruwaves.mom.project.AbstractMomProjectItem;
import net.thevpc.scholar.hadruwaves.mom.project.MomProject;
import net.thevpc.scholar.hadruwaves.mom.project.MomProjectFactory;
import net.thevpc.scholar.hadruwaves.mom.project.areamaterial.AreaMaterial;
import net.thevpc.scholar.hadruwaves.mom.project.shapes.AreaShape;
import net.thevpc.scholar.hadruwaves.mom.project.shapes.RectAreaShape;

public final class Area extends AbstractMomProjectItem implements Cloneable, ExpressionAware, AreaZone {

    protected static BasicStroke STROKE_THIN = new BasicStroke(1);
    protected static BasicStroke STROKE_BOLD = new BasicStroke(2);
    private String name;
    private MomProject project;
    private boolean enabled;
    private Color color = null;
    private AreaGroup parentGroup = null;
    private AreaShape shape = new RectAreaShape();
    private AreaMaterial material;

    public Area() {
    }

    public Area(String name) {
        this.name = name;
        this.enabled = true;
        color = null;
    }

    public String getId() {
        return "Area";
    }

    public String toString() {
        return name;
    }

    public MomProject getProject() {
        return project;
    }

    public void setProject(MomProject project) {
        this.project = project;
    }

    public AreaGroup getParentGroup() {
        return parentGroup;
    }

    public AreaGroup getTopLevelGroup() {
        AreaGroup p = getParentGroup();
        if (p != null) {
            return p.getTopLevelGroup();
        }
        return null;
    }

    public void store(Configuration c, String key) {
        c.setString(key + ".name", name);
        c.setString(key + ".group", parentGroup.getPath());
        c.setBoolean(key + ".enabled", enabled);
        c.setColor(key + ".color", color);
        MomProjectFactory.INSTANCE.store(c, key + ".shape", shape);
        MomProjectFactory.INSTANCE.store(c, key + ".material", material);
    }

    public void load(Configuration conf, String key) {
        this.name = conf.getString(key + ".name");
        setEnabled(conf.getBoolean(key + ".enabled", true));
        setColor(conf.getColor(key + ".color", null));
        shape = (AreaShape) MomProjectFactory.INSTANCE.load(conf, key + ".shape");
        material = (AreaMaterial) MomProjectFactory.INSTANCE.load(conf, key + ".material");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (project != null) {
            project.updateArea(this, this);
        }
    }


    @Override
    public AreaZone clone() {
        try {
            Area a = (Area) super.clone();
            return a;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (project != null) {
            project.updateArea(this, this);
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Domain intersect(Area other) {
        return getDomain().intersect(other.getDomain());
    }

    @Override
    public MomProject getContext() {
        return project == null ? null : project;
    }

    @Override
    public void setContext(MomProject context) {
        if (project != null && context != project) {
            throw new IllegalStateException();
        }
    }

    @Override
    public void recompile() {
        MomProject _c = getContext();
        if (_c == null) {
            return;
        }
        shape.setContext(_c);
        shape.recompile();
        material.setContext(_c);
        material.recompile();
    }


    public void setParentGroup(AreaGroup areaGroup) {
        if (parentGroup == areaGroup) {
            return;
        }
//        AreaGroup ag=getParentGroup();
//        if(ag!=null){
//            ag.removeArea(this);
//        }
        this.parentGroup = areaGroup;
    }


    public Domain getDomain() {
        shape.setContext(getContext());
        return shape.getDomain();
    }

    public void setHeight(double d) {
        shape.setContext(getContext());
        shape.setHeight(d);
    }

    public void setWidth(double d) {
        shape.setContext(getContext());
        shape.setWidth(d);
    }

    public void setX(double d) {
        shape.setContext(getContext());
        shape.setX(d);
    }

    public void setY(double d) {
        shape.setContext(getContext());
        shape.setY(d);
    }


    public AreaShape getShape() {
        return shape;
    }

    public AreaMaterial getMaterial() {
        return material;
    }

    public void setMaterial(AreaMaterial material) {
        this.material = material;
        if (project != null) {
            project.updateArea(this, this);
        }
    }


    public void setShape(AreaShape shape) {
        this.shape = shape;
    }
}
