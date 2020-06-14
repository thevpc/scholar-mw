package net.vpc.scholar.hadruwaves.project;

import net.vpc.common.props.PropertyEvent;
import net.vpc.common.props.PropertyListener;
import net.vpc.common.props.Props;
import net.vpc.common.props.WritablePLMap;
import net.vpc.scholar.hadrumaths.util.PlatformUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.vpc.scholar.hadruwaves.project.scene.HWProjectComponentGroup;

public class HWProjectFolderHelper {
    protected WritablePLMap<String, HWProjectComponent> children = Props.of("children").lmap2Of(String.class, HWProjectComponent.class, x -> x.name());
    private HWProjectComponentGroup thisAsParent;

    public HWProjectFolderHelper(HWProjectComponentGroup thisAsParent, Supplier<HWSolution> solution) {
        this.thisAsParent = thisAsParent;
        children.listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                AbstractHWProjectComponent o = event.getOldValue();
                if (o != null) {
                    o.solution.set(null);
                    o.parentPath.set(null);
                    o.parent.set(null);
                }
                AbstractHWProjectComponent n = event.getNewValue();
                if (n != null) {
                    n.solution.set(solution.get());
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

    public WritablePLMap<String, HWProjectComponent> children() {
        return children;
    }

    public List<HWProject> findModifiedProjects() {
        return findProjects().stream().filter(x -> x.modified().get()).collect(Collectors.toList());
    }

    public List<HWProject> findProjects() {
        Stack<HWProjectComponent> stack = new Stack<>();
        List<HWProject> all = new ArrayList<>();
        for (HWProjectComponent component : children().values().toList()) {
            if (component instanceof HWProject) {
                all.add((HWProject) component);
            }
            if (component instanceof HWProjectComponentGroup) {
                for (HWProjectComponent child : ((HWProjectComponentGroup) component).children().values().toList()) {
                    stack.push(child);
                }
            }
        }
        while (!stack.isEmpty()) {
            HWProjectComponent component = stack.pop();
            if (component instanceof HWProject) {
                all.add((HWProject) component);
            }
            if (component instanceof HWProjectComponentGroup) {
                for (HWProjectComponent child : ((HWProjectComponentGroup) component).children().values().toList()) {
                    stack.push(child);
                }
            }
        }
        return all;
    }

    public HWProjectComponent findElement(String path) {
        return findElement(path, false);
    }

    public HWProjectComponent findElement(String path, boolean createFolder) {
        String[] ipath = PlatformUtils.splitPath(path);
        HWProjectComponentGroup t = null;
        for (int i = 0; i < ipath.length; i++) {
            String n = ipath[i];
            if (t == null) {
                HWProjectComponent u = children.get(n);
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
                    } else if (u instanceof HWProjectComponentGroup) {
                        t = (HWProjectComponentGroup) u;
                    } else {
                        return null;
                    }
                }
            } else {
                HWProjectComponent u = t.children().get(n);
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
                    } else if (u instanceof HWProjectComponentGroup) {
                        t = (HWProjectComponentGroup) u;
                    } else {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    public HWProjectComponent remove(String path) {
        HWProjectComponent r = findElement(path);
        if (r == null) {
            return null;
        }
        HWProjectComponent p = r.parent().get();
        if (p == null) {
            return children.remove(p.name().get());
        }
        return p.parent().get().children().remove(p.name().get());
    }

    public void add(HWProjectComponent element, String path) {
        String[] ipath = PlatformUtils.splitPath(path);
        HWProjectComponentGroup t = null;
        for (int i = 0; i < ipath.length; i++) {
            String n = ipath[i];
            if (t == null) {
                HWProjectComponent u = children.get(n);
                if (u == null) {
                    HWProjectComponentGroup folder = createFolder(n);
                    children.add(folder);
                    t = folder;
                } else if (u instanceof HWProjectComponentGroup) {
                    t = (HWProjectComponentGroup) u;
                } else {
                    throw new IllegalArgumentException("Invalid path " + path + ". Folder expected");
                }
            } else {
                HWProjectComponent u = t.children().get(n);
                if (u == null) {
                    HWProjectComponentGroup folder = createFolder(n);
                    t.children().add(folder);
                    t = folder;
                } else if (u instanceof HWProjectComponentGroup) {
                    t = (HWProjectComponentGroup) u;
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

    public List<HWProjectComponent> removeDeepComponents(Predicate<HWProjectComponent> filter, boolean prune) {
        List<HWProjectComponent> all = new ArrayList<>();
        List<HWProjectComponent> readjust = new ArrayList<>();
        for (HWProjectComponent child : children.values().toList()) {
            if (filter.test(child)) {
                if (prune) {
                    if (child instanceof HWProjectComponentGroup) {
                        all.addAll(((HWProjectComponentGroup) child).removeDeepComponents((x) -> true, true));
                    }
                } else {
                    readjust.add(child);
                }
                children.remove(child.name().get());
                all.add(child);
            }
        }
        for (HWProjectComponent e : readjust) {
            children.add(e);
        }
        return all;
    }

    private HWProjectComponentGroup createFolder(String name) {
        return new DefaultHWProjectComponentGroup(name);
    }
}
