package net.vpc.scholar.hadrumaths.scalarproducts.formal;

import java.io.Serializable;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 10 juin 2007 13:51:49
 */
final class ClassClassKey implements Serializable {
    private static final long serialVersionUID = 1L;
    int domainDimension;
    Class c1;
    Class c2;
    private int h;

    public ClassClassKey(Class c1, Class c2,int domainDimension) {
        this.c1 = c1;
        this.c2 = c2;
        this.domainDimension = domainDimension;
        h=(c1.getName().hashCode()*31+c2.getName().hashCode())*31+domainDimension;
    }

    public ClassClassKey invert(){
        return new ClassClassKey(c2,c1,domainDimension);
    }

    public boolean equals(Object obj) {
        ClassClassKey obj2=(ClassClassKey) obj;
        return obj2.c1.equals(c1) && obj2.c2.equals(c2) && obj2.domainDimension==domainDimension;
    }


    public int hashCode() {
        return h;
    }

    public String toString() {
        return "("+c1.getSimpleName()+","+c2.getSimpleName()+")";
    }
}
