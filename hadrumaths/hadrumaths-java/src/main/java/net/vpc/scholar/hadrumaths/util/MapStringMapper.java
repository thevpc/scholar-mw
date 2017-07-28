package net.vpc.scholar.hadrumaths.util;

import java.util.Map;

/**
 * Created by vpc on 1/17/17.
 */
public class MapStringMapper implements StringMapper {

    private Map<String, String> map;

    public MapStringMapper(Map<String, String> map) {
        this.map = map;
    }

    @Override
    public String get(String key) {
        return map.get(key);
    }
}
