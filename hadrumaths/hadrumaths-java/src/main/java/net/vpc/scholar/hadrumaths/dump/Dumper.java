package net.vpc.scholar.hadrumaths.dump;


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
    private StringBuilder sb = new StringBuilder();
    private String name;
    private Type type = Type.OBJECT;

    public enum Type {
        SIMPLE,
        OBJECT,
        ARRAY
    }

    ;

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

    public Dumper(String name) {
        this(name, Type.OBJECT);
    }

    public Dumper(String name, Type type) {
        this.name = name == null ? "" : name;
        this.type = type;
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

    public Dumper addNonNull(String varName, Object value) {
        if (value != null) {
            add(varName, value);
        }
        return this;
    }

    public Dumper add(String varName, Object value) {
        boolean first = sb.length() == 0;
        String p = null;
        if (type == Type.SIMPLE) {
            p = varName == null ? "" : (varName + "=");
        } else {
            p = varName == null ? "  " : ("  " + varName + " = ");
        }
//        StringBuilder prefix=new StringBuilder();
//        for (int i = 0; i < p.length(); i++) {
//              prefix.append(' ');
//        }
        if (!first) {
            if (type == Type.SIMPLE) {
                sb.append(";");
            }
        }
        sb.append(p);
        String str = (type == Type.SIMPLE) ? Maths.dumpSimple(value) : Maths.dump(value);
        if (str.indexOf('\n') >= 0 || str.indexOf('\r') >= 0) {
            boolean firstLine = true;
            for (StringTokenizer stok = new StringTokenizer(str, "\r\n"); stok.hasMoreTokens(); ) {
                if (firstLine) {
                    firstLine = false;
                } else {
                    sb.append("  ");
                }
                sb.append(stok.nextToken()).append("\n");
            }
        } else {
            sb.append(str);
            if (!(type == Type.SIMPLE)) {
                sb.append("\n");
            }
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
                if (s.indexOf('\n') >= 0) {
                    return name + "[\n" + s + "]";
                } else {
                    return name + "[" + s + "]";
                }
            }
            case OBJECT: {
                if (s.indexOf('\n') >= 0) {
                    return name + "{\n" + s + "}";
                } else {
                    return name + "{" + s + "}";
                }
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }
}
