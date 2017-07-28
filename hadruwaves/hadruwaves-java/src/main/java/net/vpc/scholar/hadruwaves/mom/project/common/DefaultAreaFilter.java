package net.vpc.scholar.hadruwaves.mom.project.common;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 22 oct. 2005
 * Time: 12:50:17
 * To change this template use File | Settings | File Templates.
 */
public class DefaultAreaFilter implements AreaFilter {
    Class[] acceptedClasses;
    Class[] rejectedClasses;
    Boolean enabled;
    String name;

    public DefaultAreaFilter(Class[] acceptedClasses, Class[] rejectedClasses, Boolean enabled, String name) {
        this.acceptedClasses = acceptedClasses;
        this.rejectedClasses = rejectedClasses;
        this.enabled = enabled;
        this.name = name;
    }

    public boolean accept(AreaZone area) {
        if (enabled != null) {
            if (area instanceof Area) {
                if (((Area) area).isEnabled() != enabled.booleanValue()) {
                    return false;
                }
            }
        }
        if (name != null) {
            if (!name.equals(((Area) area).getName())) {
                return false;
            }
        }
        if(!(area instanceof Area)){
            return false;
        }
        Area aa=(Area)area;
        if (acceptedClasses != null) {
            for (int i = 0; i < acceptedClasses.length; i++) {
                Class acceptedClass = acceptedClasses[i];
                if (acceptedClass.isAssignableFrom(aa.getMaterial().getClass())) {
                    if (rejectedClasses != null) {
                        for (int j = 0; j < rejectedClasses.length; j++) {
                            Class rejectedClass = rejectedClasses[j];
                            if (rejectedClass.isAssignableFrom(aa.getMaterial().getClass())) {
                                return false;
                            }
                        }
                    }
                    return true;
                }
            }
            return false;
        }
        if (rejectedClasses != null) {
            for (int i = 0; i < rejectedClasses.length; i++) {
                Class rejectedClass = rejectedClasses[i];
                if (rejectedClass.isAssignableFrom(area.getClass())) {
                    return false;
                }
            }
        }
        return true;
    }
}
