package net.thevpc.scholar.hadrumaths.util.config;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 17:21:33
 * To change this template use File | Settings | File Templates.
 */
public class ObfuscatedValue {
    String val;

    ObfuscatedValue(Object o) {
        throw new RuntimeException("I dont know how to obfuscate");
        //val = SecurityTools.obfuscate(String.valueOf(o));
    }

    public boolean equals(Object o) {
        return o != null && val.equals(((ObfuscatedValue) o).val);
    }

    public String toString() {
        return val;
    }

    public String getValue() {
        return val;
    }
}
