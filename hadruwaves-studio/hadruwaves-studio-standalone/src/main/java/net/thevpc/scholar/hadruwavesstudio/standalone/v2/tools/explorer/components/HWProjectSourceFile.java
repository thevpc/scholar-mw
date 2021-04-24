package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.components;

import net.thevpc.common.props.FileObject;
import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritableValue;
import net.thevpc.scholar.hadruwaves.project.HWProject;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HWProjectSourceFile extends HWProjectSource implements FileObject {
    private final WritableValue<Boolean> modified = Props.of("modified").valueOf(Boolean.class, false);
    private final WritableValue<String> filePath = Props.of("filePath").valueOf(String.class, null);
    private final WritableValue<String> content = Props.of("content").valueOf(String.class, "");
    private WritableValue<String> name = Props.of("name").valueOf(String.class, null);
    public HWProjectSourceFile(String path, HWProject project, File file) {
        super(path, project, file);
        filePath.set(file.toPath().toString());
        name.set(file.getName());
    }

    public WritableValue<String> content() {
        return content;
    }

    public WritableValue<Boolean> modified() {
        return modified;
    }

    public WritableValue<String> name() {
        return name;
    }

    @Override
    public WritableValue<String> filePath() {
        return filePath;
    }

    @Override
    public String defaultFileSuffix() {
        String n = file.getName();
        int u = n.indexOf('.');
        if(u>=0) {
            return n.substring(u+1);
        }
        return null;
    }

    @Override
    public String fileTypeTitle() {
        return null;
    }

    @Override
    public void save() {
        try {
            Files.write(Paths.get(filePath.get()),content.get().getBytes());
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    public void load() {
        try {
            byte[] s = Files.readAllBytes(Paths.get(filePath.get()));
            content.set(new String(s));
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}