package net.vpc.scholar.hadrumaths;

public class BooleanHolder {
    private Boolean value;

    public BooleanHolder() {

    }
    public BooleanHolder(Boolean value) {
        this.value = value;
    }
    public Boolean get(){
        return value;
    }
    public boolean get(boolean defaultValue){
        return value==null?defaultValue:value;
    }

    public void set(Boolean value) {
        this.value = value;
    }

    public void setIfNull(boolean value) {
        if (this.value == null) {
            this.value = value;
        }
    }

}
