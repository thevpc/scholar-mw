package net.thevpc.scholar.hadrumaths.util.config;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 17:20:07
 * To change this template use File | Settings | File Templates.
 */
public class PrimitiveBooleanConverter extends ConfigConverter {
    public PrimitiveBooleanConverter() {
        super(boolean[].class, "booleanArray");
    }

    public String objectToString(Object o) {
        boolean[] ints = (boolean[]) o;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ints.length; i++) {
            if (i > 0) {
                sb.append(";");
            }
            sb.append(ints[i]);
        }
        return sb.toString();
    }

    public Object stringToObject(String s) throws ParseException {
        ArrayList arrayList = new ArrayList();
        for (StringTokenizer st = new StringTokenizer(s, ";"); st.hasMoreTokens(); ) {
            arrayList.add(Boolean.valueOf(st.nextToken().trim()));
        }
        boolean[] ints = new boolean[arrayList.size()];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = ((Boolean) arrayList.get(i)).booleanValue();
        }
        return ints;
    }
}
