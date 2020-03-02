/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.io.HFile;

/**
 *
 * @author vpc
 */
public class MomCache {

    private ObjectCache cache;

    public MomCache(ObjectCache cache) {
        this.cache = cache;
    }

    public HFile getFolder() {
        return cache.getFolder();
    }

    public Map<String, String> parseCacheValues() {
        return parseCacheValues("projectType", "circuitType", "frequency", "width", "height");
    }

    public double parseCacheDouble(String name, double defaultValue) {
        try {
            return Double.parseDouble(parseCacheValue(name));
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public String parseCacheValue(String name) {
        return parseCacheValues(name).get(name);
    }

    public Map<String, String> parseCacheValues(String... names) {
        HashMap<String, String> parsedValues = new HashMap<String, String>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new StringReader(cache.getKey().getDump()));
            String line;
            while ((line = in.readLine()) != null) {
                //TODO
                for (String s : names) {
                    if (line.startsWith("  " + s + " =")) {
                        int ieq = line.indexOf('=');
                        String k = line.substring(0, ieq).trim();
                        String v = line.substring(ieq + 1).trim();
                        parsedValues.put(k, v);
                        break;
                    }
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        }
        return parsedValues;
    }

}
