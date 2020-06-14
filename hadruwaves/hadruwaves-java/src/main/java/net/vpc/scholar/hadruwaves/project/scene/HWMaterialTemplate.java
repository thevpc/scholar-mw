package net.vpc.scholar.hadruwaves.project.scene;

import net.vpc.common.props.PValue;
import net.vpc.common.props.Props;
import net.vpc.common.props.WritablePValue;
import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadruwaves.Material;
import net.vpc.scholar.hadruwaves.project.HWProject;
import net.vpc.scholar.hadruwaves.project.HWProjectElement;
import net.vpc.scholar.hadruwaves.project.HWSolution;
import net.vpc.scholar.hadruwaves.project.Props2;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwaves.props.WritablePExpression;

public class HWMaterialTemplate implements HWProjectElement {

    private final WritablePValue<HWSolution> solution = Props.of("solution").valueOf(HWSolution.class, null);
    private final WritablePValue<HWProject> project = Props.of("project").valueOf(HWProject.class, null);
    private final WritablePValue<String> name = Props.of("name").valueOf(String.class, null);
    private final WritablePValue<String> description = Props.of("description").valueOf(String.class, null);
    private final WritablePExpression<Double> permettivity = Props2.of("permettivity").exprDoubleOf(1.0);
    private final WritablePExpression<Double> permeability = Props2.of("permeability").exprDoubleOf(1.0);
    private final WritablePExpression<Double> electricConductivity = Props2.of("electricConductivity").exprDoubleOf(0.0);

    public HWMaterialTemplate(HWProject project) {
        this.project.set(project);
        this.solution.set(project.solution().get());
    }

    public HWMaterialTemplate(Material m, HWProject project) {
        set(m);
        this.project.set(project);
        this.solution.set(project.solution().get());
    }

    @Override
    public PValue<HWSolution> solution() {
        return solution.readOnly();
    }

    @Override
    public WritablePValue<HWProject> project() {
        return project;
    }

    @Override
    public WritablePValue<String> name() {
        return name;
    }

    public WritablePValue<String> description() {
        return description;
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

    public Material eval(HWConfigurationRun configuration) {
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
        }
        return new Material(
                n,
                permettivity.eval(configuration),
                permeability.eval(configuration),
                electricConductivity.eval(configuration)
        );
    }

    @Override
    public String toString() {
        return String.valueOf(name.get());
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return 
                Tson.obj("Material")
                .add("name",name.get())
                .add("description",description.get())
                .add("permettivity",permettivity.get())
                .add("permeability",permeability.get())
                .add("Conductivity",electricConductivity.get())
                .build();
    }
    
}
