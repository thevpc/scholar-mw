package net.vpc.scholar.hadrumaths.util.dump;


import net.vpc.scholar.hadrumaths.Maths;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 1 juin 2007 17:54:12
 */
public class Dumper {
    private Object baseRef;
    private final StringBuilder sb = new StringBuilder();
    private String name;
    private String elementSeparator = ";";
    private Type type = Type.OBJECT;
    private boolean containsLineSeparator = false;

    public Dumper(Object c) {
        this(c, Type.OBJECT);
    }

    public Dumper(Object c, Type type) {
        this(c.getClass().getSimpleName(), type);
        this.baseRef = c;
        Dump d = this.baseRef.getClass().getAnnotation(Dump.class);
        if (d != null) {
            addFields(c, false);
        }
    }

    public Dumper() {
        this("", Type.OBJECT);
    }

    public Dumper(String name, Type type) {
        this.name = name == null ? "" : name;
        this.type = type;
    }

    public Dumper(String name) {
        this(name, Type.OBJECT);
    }

    public String getName() {
        return name;
    }

    public Dumper setName(String name) {
        this.name = name;
        return this;
    }

    public Dumper addFields(Object c, boolean ignoreNulls) {
        if (c == null) {
            throw new NullPointerException();
        }
        HashSet<String> found = new HashSet<String>();
        Stack<Class> stack = new Stack<Class>();
        stack.add(c.getClass());
        while (!stack.isEmpty()) {
            Class cls = stack.pop();
            for (Field field : cls.getDeclaredFields()) {
                Dump d = field.getAnnotation(Dump.class);
                if (d != null) {
                    String n0 = field.getName();
                    String n = n0;
                    int i = 1;
                    while (found.contains(n)) {
                        i++;
                        n = n0 + i;
                    }
                    found.add(n);
                    field.setAccessible(true);
                    try {
                        if (ignoreNulls) {
                            addNonNull(n, field.get(c));
                        } else {
                            add(n, field.get(c));
                        }
                    } catch (IllegalAccessException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            }
            Class sc = cls.getSuperclass();
            if (sc != null && !sc.equals(Object.class)) {
                stack.push(sc);
            }
        }
        return this;
    }

    public Dumper addFields() {
        return addFields(baseRef, false);
    }

    public Dumper add(Object value) {
        return add(null, value);
    }

    public Dumper add(String varName, Object value) {
        boolean first = sb.length() == 0;
        if (!first) {
            if (type == Type.SIMPLE) {
                sb.append(getElementSeparator());
            }
        }
        String p = null;
        if (varName != null) {
            if (type == Type.SIMPLE) {
                sb.append(varName).append("=");
            } else {
                sb.append("  ").append(varName).append(" = ");
            }
        } else {
            if (type != Type.SIMPLE) {
                sb.append("  ");
            }
        }
        String str = (type == Type.SIMPLE) ? Maths.dumpSimple(value) : Maths.dump(value);
        sb.ensureCapacity(sb.length() + str.length());
        StringTokenizer stok = new StringTokenizer(str, "\r\n");
        if (stok.hasMoreTokens()) {
            sb.append(stok.nextToken());
            boolean b = stok.hasMoreTokens();
            if (b || !(type == Type.SIMPLE)) {
                sb.append("\n");
                if (b) {
                    containsLineSeparator = true;
                    sb.append("  ");
                    sb.append(stok.nextToken());
                    sb.append("\n");
                    while (stok.hasMoreTokens()) {
                        sb.append("  ");
                        sb.append(stok.nextToken());
                        sb.append("\n");
                    }
                }
            }
        }
        return this;
    }

    public String getElementSeparator() {
        return elementSeparator;
    }

    public Dumper setElementSeparator(String elementSeparator) {
        this.elementSeparator = elementSeparator == null ? ";" : elementSeparator;
        return this;
    }

    public Dumper addNonNull(String varName, Object value) {
        if (value != null) {
            add(varName, value);
        }
        return this;
    }

    public String toString() {
        String s = sb.toString();
        switch (type) {
            case SIMPLE: {
                return name + "(" + s + ")";
            }
            case ARRAY: {
                if (containsLineSeparator) {
                    return name + "[\n" + s + "]";
                } else {
                    return name + "[" + s + "]";
                }
            }
            case OBJECT: {
                if (containsLineSeparator) {
                    return name + "{\n" + s + "}";
                } else {
                    return name + "{" + s + "}";
                }
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public enum Type {
        SIMPLE,
        OBJECT,
        ARRAY
    }
}
