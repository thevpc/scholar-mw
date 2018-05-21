package net.vpc.scholar.hadruwaves.mom.str;

import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.dump.Dumper;

import java.io.Serializable;
import java.util.Arrays;

public class GpPropertyUserOrderComparator implements TestFunctionsComparator, Serializable {
    private String propertyName;
    private Object[] values;
    public GpPropertyUserOrderComparator(String propertyName,Object ... values) {
        this.propertyName=propertyName;
        this.values=values;
    }

    public String dump() {
        Dumper d=new Dumper(this,Dumper.Type.SIMPLE);
        d.add("propertyName",propertyName);
        d.add("values",values);
        return d.toString();
    }

    public int compare(DoubleToVector o1, DoubleToVector o2) {
        Integer index1= indexOf(o1.getProperty(propertyName));
        if(index1<0){
            throw new IllegalArgumentException("value "+(o1.getProperty(propertyName))+" for property "+propertyName+" not found in "+Arrays.asList(values));
        }
        Integer index2= indexOf(o2.getProperty(propertyName));
        if(index2<0){
            throw new IllegalArgumentException("value "+(o2.getProperty(propertyName))+" for property "+propertyName+" not found in "+Arrays.asList(values));
        }
        return index1.compareTo(index2);
    }

    private int indexOf(Object value){
        if(value ==null){
            for (int i = 0; i < values.length; i++) {
                if(values[i]==null){
                    return i;
                }
            }
        }else{
            for (int i = 0; i < values.length; i++) {
                if(value.equals(values[i])){
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GpPropertyUserOrderComparator that = (GpPropertyUserOrderComparator) o;

        if (propertyName != null ? !propertyName.equals(that.propertyName) : that.propertyName != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        int result = propertyName != null ? propertyName.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(values);
        return result;
    }
}