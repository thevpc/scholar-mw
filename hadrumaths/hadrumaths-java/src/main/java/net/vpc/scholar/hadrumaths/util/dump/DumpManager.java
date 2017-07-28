package net.vpc.scholar.hadrumaths.util.dump;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.params.DebugFormat;
import net.vpc.scholar.hadrumaths.util.ClassMap;

import java.util.Collection;
import java.util.Map;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 1 juin 2007 16:10:45
 */
public class DumpManager {
    private final DumpDelegate simpleArrayHandler = new ArrayDumpDelegate();
    private DumpDelegate nullHandler = new NullDumpDelegate();
    private DumpDelegate defaultHandler = new ToStringDumpDelegate();
    private DumpDelegate arrayHandler = new ArrayDumpDelegate();
    private ClassMap<DumpDelegate> registered = new ClassMap<DumpDelegate>(Object.class, DumpDelegate.class);

    {
        register(Dumpable.class, new DumpableDelegate());
        register(Map.class, new MapDumpDelegate());
        register(Collection.class, new CollectionDumpDelegate());
        register(Expr.class, new DumpDelegate() {
            @Override
            public String getDumpString(Object object) {
                return FormatFactory.format(object, DebugFormat.INSTANCE);
            }
        });
    }

    public DumpDelegate getSimpleArrayHandler() {
        return simpleArrayHandler;
    }

    public DumpDelegate getNullHandler() {
        return nullHandler;
    }

    public void setNullHandler(DumpDelegate nullHandler) {
        this.nullHandler = nullHandler;
    }

    public DumpDelegate getDefaultHandler() {
        return defaultHandler;
    }

    public void setDefaultHandler(DumpDelegate defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    //    public static void main(String[] args) {
//        Hashtable t=new Hashtable();
//        TreeSet ts=new TreeSet();
//        ts.add("a");
//        ts.add("b");
//        t.put("x",ts);
//        t.put("y",ts);
//        System.out.println(dump(t));
//    }

    public void register(Class clz, DumpDelegate h) {
        if (clz == null) {
            nullHandler = h;
        } else {
            registered.put(clz, h);
        }
    }

    public DumpDelegate getDumpDelegate(Object o,boolean simple) {
        if (o == null) {
            return nullHandler;
        }
        if (o.getClass().isArray()) {
            if(simple){
                return simpleArrayHandler;
            }
            return arrayHandler;
        }
        DumpDelegate h = registered.get(o.getClass());
        if (h == null) {
            Dump d = o.getClass().getAnnotation(Dump.class);
            if(d!=null){
                h= DumperDelegate.INSTANCE;
            }else {
                h = defaultHandler;
            }
        }
        return h;
    }

    public static String getStackDepthWhites() {
        int len = new Throwable().getStackTrace().length;
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }
}
