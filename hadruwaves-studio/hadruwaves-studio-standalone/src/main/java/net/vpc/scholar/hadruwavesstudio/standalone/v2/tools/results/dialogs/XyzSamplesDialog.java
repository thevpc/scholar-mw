/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.dialogs;

import javax.swing.JComponent;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.components.AppDialog;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.components.AppForm;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.components.AppFormBuilder;

/**
 *
 * @author vpc
 */
public class XyzSamplesDialog extends AppDialog {

    private AppForm form;

    public XyzSamplesDialog(Object parent, String title) {
        super(parent, title);
        form = new AppForm(new AppFormBuilder());

        form.addField("xmin", "x min", String.class).setValue("xmin");
        form.addField("xmax", "x max", String.class).setValue("xmax").newLine();
        form.addField("xn", "x samples", String.class).setValue("100").colspan(2).newLine();
        form.addSeparator().colspan(2).newLine();

        form.addField("ymin", "y min", String.class).setValue("ymin");
        form.addField("ymax", "y max", String.class).setValue("ymax").newLine();
        form.addField("yn", "y samples", String.class).setValue("100").colspan(2).newLine();
        form.addSeparator().colspan(2).newLine();

        form.addField("zmin", "z min", String.class).setValue("0");
        form.addField("zmax", "z max", String.class).setValue("0").newLine();
        form.addField("zn", "z samples", String.class).setValue("1").colspan(2).newLine();
        form.addSeparator().colspan(2).newLine();

//        form.getField("xn").setValue("50");
//        form.getField("yn").setValue("50");
//        form.getField("zmin").setValue("0");
//        form.getField("zmax").setValue("0");
//        form.getField("zn").setValue("1");
        form.build();
    }

    @Override
    protected JComponent createComponent() {
        return form;
    }

    protected void check() {

    }

    public Result get(HWConfigurationRun configuration) {
        Result r = new Result();
        r.xn = configuration.evalInteger(get("xn"), 50);
        r.yn = configuration.evalInteger(get("yn"), 50);
        r.zn = configuration.evalInteger(get("zn"), 1);
        r.xmin = configuration.evalDouble(get("xmin"), 0.0);
        r.xmax = configuration.evalDouble(get("xmax"), 0.0);
        r.ymin = configuration.evalDouble(get("ymin"), 0.0);
        r.ymax = configuration.evalDouble(get("ymax"), 0.0);
        r.zmin = configuration.evalDouble(get("zmin"), 0.0);
        r.zmax = configuration.evalDouble(get("zmax"), 0.0);
        return r;
    }

    public <T> T get(String name) {
        return form.getValue(name);
    }

    public static class Result {

        public int xn;
        public int yn;
        public int zn;
        public double xmin;
        public double xmax;
        public double ymin;
        public double ymax;
        public double zmin;
        public double zmax;
    }

}
