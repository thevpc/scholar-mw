package net.vpc.scholar.hadrumaths.util;

/**
 * Created by vpc on 4/10/16.
 */
public class MathsConfigFlag {
    private static boolean accessed=false;
    public static void setAccessed(){
        MathsConfigFlag.accessed=true;
    }
    public static void setAccessed(boolean accessed){
        MathsConfigFlag.accessed=accessed;
    }

    public static void checkAccessed(){
        if(accessed){
            throw new IllegalStateException("Unable to Configure. This operation should be the first operation to do");
        }
    }
}
