package net.thevpc.scholar.hadrumaths.util.config;

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 17:22:33
 * To change this template use File | Settings | File Templates.
 */
public class ConfigOptions implements Serializable, Cloneable {

    DateFormat dateFormat;
    NumberFormat numberFormat;
    boolean helpAsEntry;
    transient File file;
    boolean autoSave;
    boolean storeComments = true;
    boolean readOnly;

    public ConfigOptions() {
    }

    public boolean isAutoSave() {
        return autoSave;
    }

    public void setAutoSave(boolean enable) {
        autoSave = enable;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public DateFormat getDateFormat() {
        return dateFormat != null ? dateFormat : DateFormat.getInstance();
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public NumberFormat getNumberFormat() {
        return numberFormat != null ? numberFormat : NumberFormat.getInstance();
    }

    public void setNumberFormat(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }

    public boolean isHelpAsEntry() {
        return helpAsEntry;
    }

    public void setHelpAsEntry(boolean enable) {
        helpAsEntry = enable;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isStoreComments() {
        return storeComments;
    }

    public void setStoreComments(boolean storeComments) {
        this.storeComments = storeComments;
    }

    @Override
    public ConfigOptions clone() {
        try {
            return (ConfigOptions) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }


}
