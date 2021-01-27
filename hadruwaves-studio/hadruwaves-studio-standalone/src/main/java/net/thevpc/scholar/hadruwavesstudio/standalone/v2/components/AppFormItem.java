/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.components;

/**
 *
 * @author vpc
 */
public abstract class AppFormItem {

    private String id;
    private boolean newLine;
    private int colspan=1;

    public AppFormItem(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public AppFormItem newLine() {
        return newLine(true);
    }

    public boolean isNewLine() {
        return newLine;
    }

    public AppFormItem newLine(boolean newLine) {
        this.newLine = newLine;
        return this;
    }
    public int colspan() {
        return colspan;
    }

    public AppFormItem colspan(int colspan) {
        this.colspan = colspan<1?1:colspan;
        return this;
    }

    public abstract void build(AppForm.FormBuildHelper bh);
}
