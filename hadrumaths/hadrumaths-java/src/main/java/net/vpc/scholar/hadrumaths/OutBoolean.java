package net.vpc.scholar.hadrumaths;

/**
 * Created by vpc on 4/17/14.
 */
public class OutBoolean {
    public boolean value;
    public boolean get(){return value;}
    public void set(boolean t){value=t;}
    public void set(){value=true;}

    public void unset() {
        value=false;
    }

    public boolean isSet() {
        return value;
    }
}
