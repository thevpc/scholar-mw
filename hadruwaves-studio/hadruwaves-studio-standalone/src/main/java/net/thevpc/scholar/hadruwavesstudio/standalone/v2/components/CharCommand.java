package net.thevpc.scholar.hadruwavesstudio.standalone.v2.components;

public class CharCommand {
    private String command;
    private String name;
    private String description;
    private String text;
    private String icon;

    public CharCommand(String command, String name, String description, String text, String icon) {
        this.command = command;
        this.name = name;
        this.description = description;
        this.text = text;
        this.icon = icon;
    }

    public String getCommand() {
        return command;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getText() {
        return text;
    }

    public String getIcon() {
        return icon;
    }
}
