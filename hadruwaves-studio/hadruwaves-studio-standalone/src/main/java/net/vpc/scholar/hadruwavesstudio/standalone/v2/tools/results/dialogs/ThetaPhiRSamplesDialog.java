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
public class ThetaPhiRSamplesDialog extends AppDialog {

    private AppForm form;

    public ThetaPhiRSamplesDialog(Object parent, String title) {
        super(parent, title);
        form = new AppForm(new AppFormBuilder());

        form.addField("thetamin", "Theta min", String.class).setValue("0");
        form.addField("thetamax", "Theta max", String.class).setValue("2*pi").newLine();
        form.addField("thetan", "Theta samples", String.class).setValue("100").colspan(2).newLine();
        form.addSeparator().colspan(2).newLine();

        form.addField("phimin", "Phi min", String.class).setValue("0");
        form.addField("phimax", "Phi max", String.class).setValue("2*pi").newLine();
        form.addField("phin", "Phi samples", String.class).setValue("100").colspan(2).newLine();
        form.addSeparator().colspan(2).newLine();

        form.addField("rmin", "R min", String.class).setValue("1");
        form.addField("rmax", "R max", String.class).setValue("1").newLine();
        form.addField("rn", "R samples", String.class).setValue("1").colspan(2).newLine();
        form.addSeparator().colspan(2).newLine();

//        form.getField("theta_n").setValue("50");
//        form.getField("phi_n").setValue("50");
//        form.getField("r_min").setValue("0");
//        form.getField("r_max").setValue("0");
//        form.getField("r_n").setValue("1");
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
        r.theta_n = configuration.evalInteger(get("thetan"), 50);
        r.phi_n = configuration.evalInteger(get("phin"), 50);
        r.r_n = configuration.evalInteger(get("rn"), 1);
        r.theta_min = configuration.evalDouble(get("thetamin"), 0.0);
        r.theta_max = configuration.evalDouble(get("thetamax"), 0.0);
        r.phi_min = configuration.evalDouble(get("phimin"), 0.0);
        r.phi_max = configuration.evalDouble(get("phimax"), 0.0);
        r.r_min = configuration.evalDouble(get("rmin"), 0.0);
        r.r_max = configuration.evalDouble(get("rmax"), 0.0);
        return r;
    }

    public <T> T get(String name) {
        return form.getValue(name);
    }

    public static class Result {

        public int theta_n;
        public int phi_n;
        public int r_n;
        public double theta_min;
        public double theta_max;
        public double phi_min;
        public double phi_max;
        public double r_min;
        public double r_max;
    }

}
