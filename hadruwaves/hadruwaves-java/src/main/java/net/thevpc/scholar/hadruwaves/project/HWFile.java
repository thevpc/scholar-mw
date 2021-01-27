package net.thevpc.scholar.hadruwaves.project;

public class HWFile {
    private String path;
    private String type;

    public HWFile(String path, String type) {
        this.path = path;
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }
}
