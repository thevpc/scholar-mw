package net.thevpc.scholar.hadruwaves.project.parameter;

import net.thevpc.common.props.*;
import net.thevpc.scholar.hadrumaths.util.PlatformUtils;
import net.thevpc.scholar.hadruwaves.project.HWProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class HWParameterFolderHelper {
    protected WritableLiMap<String, HWParameterElement> children = Props.of("children").lmap2Of(String.class, HWParameterElement.class, x -> x.name());
    private HWParameterFolder thisAsParent;

    public HWParameterFolderHelper(HWParameterFolder thisAsParent, Supplier<HWProject> prj) {
        this.thisAsParent = thisAsParent;
        children.onChange(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                AbstractHWParameterElement o = event.oldValue();
                if (o != null) {
                    o.parentPath.set(null);
                    o.parent.set(null);
                }
                AbstractHWParameterElement n = event.newValue();
                if (n != null) {
                    n.project.set(prj.get());
                    n.parent.set(thisAsParent);
                    if (thisAsParent == null) {
                        n.parentPath.set("/");
                    } else {
                        n.parentPath.set(thisAsParent.parentPath().get() + "/" + thisAsParent.name().get());
                    }
                }
            }
        });
    }

    public WritableLiMap<String, HWParameterElement> children() {
        return children;
    }


    public HWParameterValue getValue(String name) {
        Stack<HWParameterElement> stack = new Stack<>();
        for (HWParameterElement component : children().values().toList()) {
            if (component instanceof HWParameterValue) {
                HWParameterValue v = (HWParameterValue) component;
                if (name.equals(v.name().get())) {
                    return v;
                }
            }
            if (component instanceof HWParameterFolder) {
                for (HWParameterElement child : ((HWParameterFolder) component).children().values().toList()) {
                    stack.push(child);
                }
            }
        }
        while (!stack.isEmpty()) {
            HWParameterElement component = stack.pop();
            if (component instanceof HWParameterValue) {
                HWParameterValue v = (HWParameterValue) component;
                if (name.equals(v.name().get())) {
                    return v;
                }
            }
            if (component instanceof HWParameterFolder) {
                for (HWParameterElement child : ((HWParameterFolder) component).children().values().toList()) {
                    stack.push(child);
                }
            }
        }
        return null;
    }

    public List<HWParameterValue> findValues() {
        Stack<HWParameterElement> stack = new Stack<>();
        List<HWParameterValue> all = new ArrayList<>();
        for (HWParameterElement component : children().values().toList()) {
            if (component instanceof HWParameterValue) {
                all.add((HWParameterValue) component);
            }
            if (component instanceof HWParameterFolder) {
                for (HWParameterElement child : ((HWParameterFolder) component).children().values().toList()) {
                    stack.push(child);
                }
            }
        }
        while (!stack.isEmpty()) {
            HWParameterElement component = stack.pop();
            if (component instanceof HWParameterValue) {
                all.add((HWParameterValue) component);
            }
            if (component instanceof HWParameterFolder) {
                for (HWParameterElement child : ((HWParameterFolder) component).children().values().toList()) {
                    stack.push(child);
                }
            }
        }
        return all;
    }

    public HWParameterValue removeValue(String name) {
        HWParameterValue u = getValue(name);
        if(u!=null){
            HWParameterFolder p = u.parent().get();
            if(p==null){
                HWParameterElement o = children.get(name);
                if(o instanceof HWParameterValue){
                    return (HWParameterValue) children.remove(name);
                }
            }else{
                HWParameterElement o = p.children().get(name);
                if(o instanceof HWParameterValue){
                    return (HWParameterValue) p.children().remove(name);
                }
            }
        }
        return null;
    }

    public HWParameterElement findElement(String path) {
        return findElement(path, false);
    }

    public HWParameterElement findElement(String path, boolean createFolder) {
        String[] ipath = PlatformUtils.splitPath(path);
        HWParameterFolder t = null;
        for (int i = 0; i < ipath.length; i++) {
            String n = ipath[i];
            if (t == null) {
                HWParameterElement u = children.get(n);
                if (u == null) {
                    if (createFolder) {
                        t = createFolder(n);
                        children.add(t);
                    } else {
                        return null;
                    }
                } else {
                    if (i == ipath.length - 1) {
                        return u;
                    } else if (u instanceof HWParameterFolder) {
                        t = (HWParameterFolder) u;
                    } else {
                        return null;
                    }
                }
            } else {
                HWParameterElement u = t.children().get(n);
                if (u == null) {
                    if (createFolder) {
                        t = createFolder(n);
                        t.children().add(t);
                    } else {
                        return null;
                    }
                } else {
                    if (i == ipath.length - 1) {
                        return u;
                    } else if (u instanceof HWParameterFolder) {
                        t = (HWParameterFolder) u;
                    } else {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    public HWParameterElement remove(String path) {
        HWParameterElement r = findElement(path);
        if (r == null) {
            return null;
        }
        HWParameterElement p = r.parent().get();
        if (p == null) {
            return children.remove(p.name().get());
        }
        return p.parent().get().children().remove(p.name().get());
    }

    public void addAt(String path, HWParameterElement element) {
        String[] ipath = PlatformUtils.splitPath(path);
        HWParameterFolder t = null;
        for (int i = 0; i < ipath.length; i++) {
            String n = ipath[i];
            if (t == null) {
                HWParameterElement u = children.get(n);
                if (u == null) {
                    HWParameterFolder folder = createFolder(n);
                    children.add(folder);
                    t = folder;
                } else if (u instanceof HWParameterFolder) {
                    t = (HWParameterFolder) u;
                } else {
                    throw new IllegalArgumentException("Invalid path " + path + ". Folder expected");
                }
            } else {
                HWParameterElement u = t.children().get(n);
                if (u == null) {
                    HWParameterFolder folder = createFolder(n);
                    t.children().add(folder);
                    t = folder;
                } else if (u instanceof HWParameterFolder) {
                    t = (HWParameterFolder) u;
                } else {
                    throw new IllegalArgumentException("Invalid path " + path + ". Folder expected");
                }
            }
        }
        if (t == null) {
            children.add(element);
        } else {
            t.children().add(element);
        }
    }

    public List<HWParameterElement> removeDeepComponents(Predicate<HWParameterElement> filter, boolean prune) {
        List<HWParameterElement> all = new ArrayList<>();
        List<HWParameterElement> readjust = new ArrayList<>();
        for (HWParameterElement child : children.values().toList()) {
            if (filter.test(child)) {
                if (prune) {
                    if (child instanceof HWParameterFolder) {
                        all.addAll(((HWParameterFolder) child).removeDeepComponents((x) -> true, true));
                    }
                } else {
                    readjust.add(child);
                }
                children.remove(child.name().get());
                all.add(child);
            }
        }
        for (HWParameterElement e : readjust) {
            children.add(e);
        }
        return all;
    }

    private HWParameterFolder createFolder(String name) {
        return new HWParameterFolder(name);
    }
}
