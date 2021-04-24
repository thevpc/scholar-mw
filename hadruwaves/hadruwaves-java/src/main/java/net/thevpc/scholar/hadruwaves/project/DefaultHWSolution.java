package net.thevpc.scholar.hadruwaves.project;

import net.thevpc.common.props.*;
import net.thevpc.common.props.impl.PropertyListenersImpl;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import net.thevpc.tson.TsonObjectContext;

public class DefaultHWSolution implements HWSolution {

    private String uuid;
    private WritableValue<String> filePath = Props.of("path").valueOf(String.class, null);
    private WritableValue<Boolean> modified = Props.of("modified").valueOf(Boolean.class, false);
    private WritableValue<String> name = Props.of("name").valueOf(String.class, null);
    private WritableValue<String> description = Props.of("description").valueOf(String.class, null);
    private WritableValue<HWProject> activeProject = Props.of("activeProject").valueOf(HWProject.class, null);
    private PropertyListenersImpl listeners = new PropertyListenersImpl(this);
    private HWSolutionFolderHelper childrenHelper = new HWSolutionFolderHelper(null, () -> this);

    public DefaultHWSolution(String uuid) {
        this.uuid = uuid;
        listeners.addDelegate(filePath);
        listeners.addDelegate(childrenHelper.children, () -> null);
        listeners.addDelegate(name);
        listeners.addDelegate(description);
        listeners.addDelegate(activeProject);
        listeners.add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                modified.set(true);
            }

        });

    }

    public DefaultHWSolution() {
        this("hwss-" + UUID.randomUUID().toString());
    }

    @Override
    public String uuid() {
        return uuid;
    }

    @Override
    public WritableValue<HWProject> activeProject() {
        return activeProject;
    }

    @Override
    public WritableValue<String> description() {
        return description;
    }

    @Override
    public WritableLiMap<String, HWSolutionElement> children() {
        return childrenHelper.children();
    }

    @Override
    public List<HWProject> findModifiedProjects() {
        return childrenHelper.findModifiedProjects();
    }

    @Override
    public List<HWProject> findProjects() {
        return childrenHelper.findProjects();
    }

    @Override
    public HWSolutionElement findElement(String path) {
        return childrenHelper.findElement(path);
    }

    @Override
    public HWSolutionElement findElement(String path, boolean createFolder) {
        return childrenHelper.findElement(path, createFolder);
    }

    @Override
    public HWSolutionElement remove(String path) {
        return childrenHelper.remove(path);
    }

    @Override
    public void add(HWSolutionElement element, String path) {
        childrenHelper.add(element, path);
    }

    @Override
    public HWProject addProject(String namePrefix, String path) {
        return childrenHelper.addProject(namePrefix, path);
    }

    @Override
    public List<HWSolutionElement> removeDeepComponents(Predicate<HWSolutionElement> filter, boolean prune) {
        return childrenHelper.removeDeepComponents(filter, prune);
    }

    @Override
    public PropertyListeners listeners() {
        return listeners;
    }

    @Override
    public WritableValue<String> name() {
        return name;
    }

    @Override
    public WritableValue<Boolean> modified() {
        return modified;
    }

    public WritableValue<String> filePath() {
        return filePath;
    }

    @Override
    public String defaultFileSuffix() {
        return "hws.tson";
    }

    @Override
    public void save() {
        String f = filePath().get();
        if (f == null || f.length() == 0) {
            throw new UncheckedIOException(new IOException("Missing File"));
        }
        try {
            Tson.writer().write(new File(f), toTsonElement());
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    public void load() {
        //TODO
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.obj("solution")
                .add("path", filePath().get())
                .build();
    }

    @Override
    public String fileTypeTitle() {
        return "Solution";
    }

}