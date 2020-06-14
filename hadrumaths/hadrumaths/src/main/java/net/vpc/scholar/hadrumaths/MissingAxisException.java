package net.vpc.scholar.hadrumaths;

public class MissingAxisException extends HException{
    private Axis[] axis;
    public MissingAxisException(Axis axis) {
        super("Missing Axis "+axis);
        this.axis=new Axis[]{axis};
    }
    public MissingAxisException(Axis axis1,Axis axis2) {
        super("Missing Axis "+axis1+" and "+axis2);
        this.axis=new Axis[]{axis1,axis2};
    }
}
