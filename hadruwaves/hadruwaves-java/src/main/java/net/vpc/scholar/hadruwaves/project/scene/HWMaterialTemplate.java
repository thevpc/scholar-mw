package net.vpc.scholar.hadruwaves.project.scene;

import net.vpc.common.prpbind.Props;
import net.vpc.common.prpbind.WritablePValue;
import net.vpc.scholar.hadruwaves.Material;
import net.vpc.scholar.hadruwaves.project.HWProjectEnv;
import net.vpc.scholar.hadruwaves.project.Props2;
import net.vpc.scholar.hadruwaves.props.WritablePExpression;

public class HWMaterialTemplate {
    private WritablePValue<String> name = Props.of("name").valueOf(String.class, null);
    private WritablePExpression<Double> permettivity = Props2.of("permettivity").doubleOf(1.0);
    private WritablePExpression<Double> permeability = Props2.of("permeability").doubleOf(1.0);
    private WritablePExpression<Double> electricConductivity = Props2.of("electricConductivity").doubleOf(0.0);

    public HWMaterialTemplate() {
    }

    public HWMaterialTemplate(Material m) {
        set(m);
    }


    public WritablePValue<String> name() {
        return name;
    }

    public WritablePExpression<Double> permettivity() {
        return permettivity;
    }

    public WritablePExpression<Double> permeability() {
        return permeability;
    }

    public WritablePExpression<Double> electricConductivity() {
        return electricConductivity;
    }

    public void set(Material material) {
        if (material == null) {
            material = Material.VACUUM;
        }
        name.set(material.getName());
        permettivity.set(String.valueOf(material.getPermettivity()));
        permeability.set(String.valueOf(material.getPermeability()));
        electricConductivity.set(String.valueOf(material.getElectricConductivity()));
    }

    public Material eval(HWProjectEnv env) {
        String n = name.get();
        if (n == null) {
            n = "";
        } else {
            n = n.trim().toLowerCase();
        }
        switch (n) {
            case "vacuum":
                return Material.VACUUM;
            case "pec":
                return Material.PEC;
            case "pmc":
                return Material.PMC;
        }
        return new Material(
                n,
                permettivity.eval(env),
                permeability.eval(env),
                electricConductivity.eval(env)
        );
    }
}
