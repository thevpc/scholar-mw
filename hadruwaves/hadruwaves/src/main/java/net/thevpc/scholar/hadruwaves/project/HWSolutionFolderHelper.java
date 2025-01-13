package net.thevpc.scholar.hadruwaves.project;

import net.thevpc.common.props.PropertyEvent;
import net.thevpc.common.props.PropertyListener;
import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritableLiMap;
import net.thevpc.scholar.hadrumaths.util.PlatformUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class HWSolutionFolderHelper {

    protected WritableLiMap<String, HWSolutionElement> children = Props.of("children").lmap2Of(String.class, HWSolutionElement.class, x -> x.name());
    private HWSolutionFolder thisAsParent;

    public HWSolutionFolderHelper(HWSolutionFolder thisAsParent, Supplier<HWSolution> solution) {
        this.thisAsParent = thisAsParent;
        children.onChange(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                if (event.eventPath().equals("/")) {
                    AbstractHWSolutionElement o = event.oldValue();
                    if (o != null) {
                        o.solution.set(null);
                        o.parentPath.set(null);
                        o.parent.set(null);
                    }
                    AbstractHWSolutionElement n = event.newValue();
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
            }
        });
    }

    public WritableLiMap<String, HWSolutionElement> children() {
        return children;
    }

    public List<HWProject> findModifiedProjects() {
        return findProjects().stream().filter(x -> x.modified().get()).collect(Collectors.toList());
    }

    public List<HWProject> findProjects() {
        Stack<HWSolutionElement> stack = new Stack<>();
        List<HWProject> all = new ArrayList<>();
        for (HWSolutionElement component : children().values().toList()) {
            if (component instanceof HWProject) {
                all.add((HWProject) component);
            }
            if (component instanceof DefaultHWSolutionFolder) {
                for (HWSolutionElement child : ((HWSolutionFolder) component).children().values().toList()) {
                    stack.push(child);
                }
            }
        }
        while (!stack.isEmpty()) {
            HWSolutionElement component = stack.pop();
            if (component instanceof HWProject) {
                all.add((HWProject) component);
            }
            if (component instanceof DefaultHWSolutionFolder) {
                for (HWSolutionElement child : ((HWSolutionFolder) component).children().values().toList()) {
                    stack.push(child);
                }
            }
        }
        return all;
    }

    public HWSolutionElement findElement(String path) {
        return findElement(path, false);
    }

    public HWSolutionElement findElement(String path, boolean createFolder) {
        String[] ipath = PlatformUtils.splitPath(path);
        HWSolutionFolder t = null;
        for (int i = 0; i < ipath.length; i++) {
            String n = ipath[i];
            if (t == null) {
                HWSolutionElement u = children.get(n);
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
                    } else if (u instanceof DefaultHWSolutionFolder) {
                        t = (HWSolutionFolder) u;
                    } else {
                        return null;
                    }
                }
            } else {
                HWSolutionElement u = t.children().get(n);
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
                    } else if (u instanceof DefaultHWSolutionFolder) {
                        t = (HWSolutionFolder) u;
                    } else {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    public HWSolutionElement remove(String path) {
        HWSolutionElement r = findElement(path);
        if (r == null) {
            return null;
        }
        HWSolutionElement p = r.parent().get();
        if (p == null) {
            return children.remove(p.name().get());
        }
        return p.parent().get().children().remove(p.name().get());
    }

    public void add(HWSolutionElement element, String path) {
        String[] ipath = PlatformUtils.splitPath(path);
        HWSolutionFolder t = null;
        for (int i = 0; i < ipath.length; i++) {
            String n = ipath[i];
            if (t == null) {
                HWSolutionElement u = children.get(n);
                if (u == null) {
                    HWSolutionFolder folder = createFolder(n);
                    children.add(folder);
                    t = folder;
                } else if (u instanceof DefaultHWSolutionFolder) {
                    t = (HWSolutionFolder) u;
                } else {
                    throw new IllegalArgumentException("Invalid path " + path + ". Folder expected");
                }
            } else {
                HWSolutionElement u = t.children().get(n);
                if (u == null) {
                    HWSolutionFolder folder = createFolder(n);
                    t.children().add(folder);
                    t = folder;
                } else if (u instanceof DefaultHWSolutionFolder) {
                    t = (HWSolutionFolder) u;
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

    public HWProject addProject(String namePrefix, String path) {
        if (namePrefix == null) {
            namePrefix = "New Project";
        }
        if (path == null) {
            path = "/";
        }
        HWProject project = new DefaultHWProject();
        Set<String> s = findProjects().stream().map(x -> x.name().get()).collect(Collectors.toSet());
        int index = 1;
        String goodName = null;
        while (true) {
            String n = namePrefix;
            if (index > 1) {
                n += " " + index;
            }
            if (!s.contains(n)) {
                goodName = n;
                break;
            }
            index++;
        }
        project.name().set(goodName);
        add(project, path);
        return project;
    }

    public List<HWSolutionElement> removeDeepComponents(Predicate<HWSolutionElement> filter, boolean prune) {
        List<HWSolutionElement> all = new ArrayList<>();
        List<HWSolutionElement> readjust = new ArrayList<>();
        for (HWSolutionElement child : children.values().toList()) {
            if (filter.test(child)) {
                if (prune) {
                    if (child instanceof DefaultHWSolutionFolder) {
                        all.addAll(((HWSolutionFolder) child).removeDeepComponents((x) -> true, true));
                    }
                } else {
                    readjust.add(child);
                }
                children.remove(child.name().get());
                all.add(child);
            }
        }
        for (HWSolutionElement e : readjust) {
            children.add(e);
        }
        return all;
    }

    private HWSolutionFolder createFolder(String name) {
        return new DefaultHWSolutionFolder(name);
    }
}
