package net.vpc.scholar.hadrumaths;

public class IntegerHolder {
    private Integer value;

    public IntegerHolder() {

    }
    public IntegerHolder(Integer value) {
        this.value = value;
    }
    public Integer get(){
        return value;
    }
    public int get(int defaultValue){
        return value==null?defaultValue:value;
    }

    public void set(Integer value) {
        this.value = value;
    }

    public void setIfNull(int value) {
        if (this.value == null) {
            this.value = value;
        }
    }

}
