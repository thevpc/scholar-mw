package net.vpc.scholar.hadrumaths.plot.console;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 10 janv. 2007 21:03:13
 */
public class WindowPath implements Serializable {
    private int h = 0;
    private ArrayList<String> path = new ArrayList<String>();

    public WindowPath(String path) {
        if(path==null){
            path="";
        }
        for (String s : path.split("/")) {
            s=s.trim();
            if(s.length()>0){
                add(s);
            }
        }
    }

    public WindowPath(WindowPath parent, String preferredName) {
        if(parent==null){
            add("MyProject");
            add(preferredName);
        }else{
            for (String element : parent.path) {
                if (element == null || element.length() == 0) {
                    add(preferredName);
                } else {
                    add(element);
                }
            }
        }
    }

    public WindowPath(String... elements) {
        for (String element : elements) {
            add(element);
        }
    }

    private WindowPath add(String path) {
        this.path.add(path);
        h = 31 * h + path.hashCode();
        return this;
    }

    public String[] toArray() {
        return path.toArray(new String[path.size()]);
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof WindowPath)) {
            return false;
        }
        WindowPath wp = (WindowPath) obj;
        return path.equals(wp.path);
    }

    public int hashCode() {
        return h;
    }
}
