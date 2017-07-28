package net.vpc.scholar.hadrumaths.util.config;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 17:20:03
 * To change this template use File | Settings | File Templates.
 */
public class PrimitiveIntArrayConverter extends ConfigConverter {
    public PrimitiveIntArrayConverter() {
        super(int[].class, "intArray");
    }

    public String objectToString(Object o) {
        int[] ints = (int[]) o;
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
        for (StringTokenizer st = new StringTokenizer(s, ";"); st.hasMoreTokens();) {
            arrayList.add(new Integer(st.nextToken().trim()));
        }
        int[] ints = new int[arrayList.size()];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = ((Integer) arrayList.get(i)).intValue();
        }
        return ints;
    }
}
