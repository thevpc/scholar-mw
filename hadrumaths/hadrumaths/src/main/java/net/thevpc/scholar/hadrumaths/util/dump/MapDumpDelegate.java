package net.thevpc.scholar.hadrumaths.util.dump;

import net.thevpc.scholar.hadrumaths.Maths;

import java.util.Map;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 1 juin 2007 16:24:38
 */
public class MapDumpDelegate implements DumpDelegate {

    public MapDumpDelegate() {
    }

    public String getDumpString(Object object) {
        Dumper h = new Dumper(null);
        Map m = (Map) object;
        for (Object o : m.entrySet()) {
            Map.Entry e = (Map.Entry) o;
            h.add(Maths.dump(e.getKey()), e.getValue());
        }
        return h.toString();
    }
}
