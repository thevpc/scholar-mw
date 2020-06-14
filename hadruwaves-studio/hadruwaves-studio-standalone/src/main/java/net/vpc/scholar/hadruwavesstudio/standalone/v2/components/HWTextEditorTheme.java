package net.vpc.scholar.hadruwavesstudio.standalone.v2.components;

import org.fife.ui.rsyntaxtextarea.Theme;

import java.io.IOException;

public class HWTextEditorTheme {
    private String id;
    private String name;
    private String resourceUrl;

    public HWTextEditorTheme(String id,String name, String resourceUrl) {
        this.name = name;
        this.id = id;
        this.resourceUrl = resourceUrl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public Theme loadTheme() {
        try {
            return Theme.load(getClass().getClassLoader().getResourceAsStream(resourceUrl));
        } catch (IOException e) {
            return null;
        }
    }
}
