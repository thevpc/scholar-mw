package net.vpc.scholar.hadrumaths.plot.console;

import java.awt.*;

public class FrameInfo {
    private Component component;
    private String title;
    private boolean resizable;
    private boolean closable;
    private boolean maximizable;
    private boolean iconifiable;
    private boolean icon;
    private Dimension preferredSize = new Dimension(600, 400);

    public Component getComponent() {
        return component;
    }

    public FrameInfo setComponent(Component component) {
        this.component = component;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public FrameInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean isResizable() {
        return resizable;
    }

    public FrameInfo setResizable(boolean resizable) {
        this.resizable = resizable;
        return this;
    }

    public boolean isClosable() {
        return closable;
    }

    public FrameInfo setClosable(boolean closable) {
        this.closable = closable;
        return this;
    }

    public boolean isMaximizable() {
        return maximizable;
    }

    public FrameInfo setMaximizable(boolean maximizable) {
        this.maximizable = maximizable;
        return this;
    }

    public boolean isIconifiable() {
        return iconifiable;
    }

    public FrameInfo setIconifiable(boolean iconifiable) {
        this.iconifiable = iconifiable;
        return this;
    }

    public boolean isIcon() {
        return icon;
    }

    public FrameInfo setIcon(boolean icon) {
        this.icon = icon;
        return this;
    }

    public Dimension getPreferredSize() {
        return preferredSize;
    }

    public FrameInfo setPreferredSize(Dimension preferredSize) {
        this.preferredSize = preferredSize;
        return this;
    }
}
