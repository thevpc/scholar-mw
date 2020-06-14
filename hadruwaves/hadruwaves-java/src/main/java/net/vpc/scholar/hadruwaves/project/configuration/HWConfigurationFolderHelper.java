package net.vpc.scholar.hadruwaves.project.configuration;

import net.vpc.common.props.PropertyEvent;
import net.vpc.common.props.PropertyListener;
import net.vpc.common.props.Props;
import net.vpc.common.props.WritablePLMap;
import net.vpc.scholar.hadrumaths.util.PlatformUtils;
import net.vpc.scholar.hadruwaves.project.HWProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class HWConfigurationFolderHelper {
    protected WritablePLMap<String, HWConfigurationElement> children = Props.of("children").lmap2Of(String.class, HWConfigurationElement.class, x -> x.name());
    private HWConfigurationFolder thisAsParent;

    public HWConfigurationFolderHelper(HWConfigurationFolder thisAsParent, Supplier<HWProject> prj) {
        this.thisAsParent = thisAsParent;
        children.listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                AbstractHWConfigurationElement o = event.getOldValue();
                if (o != null) {
                    o.parentPath.set(null);
                    o.parent.set(null);
                }
                AbstractHWConfigurationElement n = event.getNewValue();
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

    public WritablePLMap<String, HWConfigurationElement> children() {
        return children;
    }


    public List<HWConfigurationRun> findRuns() {
        Stack<HWConfigurationElement> stack = new Stack<>();
        List<HWConfigurationRun> all = new ArrayList<>();
        for (HWConfigurationElement component : children().values().toList()) {
            if (component instanceof HWConfigurationRun) {
                all.add((HWConfigurationRun) component);
            }
            if (component instanceof HWConfigurationFolder) {
                for (HWConfigurationElement child : ((HWConfigurationFolder) component).children().values().toList()) {
                    stack.push(child);
                }
            }
        }
        while (!stack.isEmpty()) {
            HWConfigurationElement component = stack.pop();
            if (component instanceof HWConfigurationRun) {
                all.add((HWConfigurationRun) component);
            }
            if (component instanceof HWConfigurationFolder) {
                for (HWConfigurationElement child : ((HWConfigurationFolder) component).children().values().toList()) {
                    stack.push(child);
                }
            }
        }
        return all;
    }

    public HWConfigurationElement findElement(String path) {
        return findElement(path, false);
    }

    public HWConfigurationElement findElement(String path, boolean createFolder) {
        String[] ipath = PlatformUtils.splitPath(path);
        HWConfigurationFolder t = null;
        for (int i = 0; i < ipath.length; i++) {
            String n = ipath[i];
            if (t == null) {
                HWConfigurationElement u = children.get(n);
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
                    } else if (u instanceof HWConfigurationFolder) {
                        t = (HWConfigurationFolder) u;
                    } else {
                        return null;
                    }
                }
            } else {
                HWConfigurationElement u = t.children().get(n);
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
                    } else if (u instanceof HWConfigurationFolder) {
                        t = (HWConfigurationFolder) u;
                    } else {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    public HWConfigurationElement remove(String path) {
        HWConfigurationElement r = findElement(path);
        if (r == null) {
            return null;
        }
        HWConfigurationElement p = r.parent().get();
        if (p == null) {
            return children.remove(p.name().get());
        }
        return p.parent().get().children().remove(p.name().get());
    }

    public void add(HWConfigurationElement element, String path) {
        String[] ipath = PlatformUtils.splitPath(path);
        HWConfigurationFolder t = null;
        for (int i = 0; i < ipath.length; i++) {
            String n = ipath[i];
            if (t == null) {
                HWConfigurationElement u = children.get(n);
                if (u == null) {
                    HWConfigurationFolder folder = createFolder(n);
                    children.add(folder);
                    t = folder;
                } else if (u instanceof HWConfigurationFolder) {
                    t = (HWConfigurationFolder) u;
                } else {
                    throw new IllegalArgumentException("Invalid path " + path + ". Folder expected");
                }
            } else {
                HWConfigurationElement u = t.children().get(n);
                if (u == null) {
                    HWConfigurationFolder folder = createFolder(n);
                    t.children().add(folder);
                    t = folder;
                } else if (u instanceof HWConfigurationFolder) {
                    t = (HWConfigurationFolder) u;
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

    public List<HWConfigurationElement> removeDeepComponents(Predicate<HWConfigurationElement> filter, boolean prune) {
        List<HWConfigurationElement> all = new ArrayList<>();
        List<HWConfigurationElement> readjust = new ArrayList<>();
        for (HWConfigurationElement child : children.values().toList()) {
            if (filter.test(child)) {
                if (prune) {
                    if (child instanceof HWConfigurationFolder) {
                        all.addAll(((HWConfigurationFolder) child).removeDeepComponents((x) -> true, true));
                    }
                } else {
                    readjust.add(child);
                }
                children.remove(child.name().get());
                all.add(child);
            }
        }
        for (HWConfigurationElement e : readjust) {
            children.add(e);
        }
        return all;
    }

    private HWConfigurationFolder createFolder(String name) {
        return new HWConfigurationFolder(name);
    }
}
