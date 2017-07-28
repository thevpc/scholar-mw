package net.vpc.scholar.hadrumaths.util;

import java.util.logging.Level;

/**
 * Created by vpc on 3/20/17.
 */
public class StringPrefixProgressMessage implements ProgressMessage {
    private String prefix;
    private ProgressMessage message;

    public StringPrefixProgressMessage(String prefix,ProgressMessage message) {

        this.message = message;
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public ProgressMessage getMessage() {
        return message;
    }

    @Override
    public Level getLevel() {
        return message.getLevel();
    }

    @Override
    public String getText() {
        return prefix+message.getText();
    }

    public String toString(){
        return getText();
    }
}
