package net.vpc.scholar.hadrumaths;


public class VoidParam extends AbstractParam implements Cloneable {

    public VoidParam(String name) {
        super(name);
    }

    @Override
    public void configure(Object source, Object value) {
        //do nothing
    }
}
