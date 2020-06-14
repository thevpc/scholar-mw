package net.vpc.scholar.hadruwaves;

import java.text.DecimalFormat;
import java.util.Objects;

public class Material {

    public static final Material VACUUM = new Material("Vacuum", 1, 1, 0);
    public static final Material PEC = new Material("PEC", Double.POSITIVE_INFINITY, 1, Double.POSITIVE_INFINITY);
    private String name;
    private double permettivity;
    private double permeability;
    private double electricConductivity;

    public Material(String name, double permettivity, double permeability, double electricConductivity) {
        this.name = name;
        this.permettivity = permettivity;
        this.permeability = permeability;
        this.electricConductivity = electricConductivity;
    }

    public static Material substrate(double epsr) {
        return substrate("Substrate" + new DecimalFormat("0.0####").format(epsr).replace('.', '_').replace('-', '_'), epsr);
    }

    public static Material substrate(String name, double epsr) {
        return new Material(name, epsr, 1, 0);
    }

    public boolean isSubstrate() {
        return electricConductivity == 0 && permeability == 1;
    }

    public boolean isPEC() {
        return this.equals(PEC);
    }

    public boolean isVacuum() {
        return this.equals(VACUUM);
    }

    public String getName() {
        return name;
    }

    public double getPermettivity() {
        return permettivity;
    }

    public double getPermeability() {
        return permeability;
    }

    public double getElectricConductivity() {
        return electricConductivity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, permettivity, permeability, electricConductivity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Material material = (Material) o;
        return Double.compare(material.permettivity, permettivity) == 0
                && Double.compare(material.permeability, permeability) == 0
                && Double.compare(material.electricConductivity, electricConductivity) == 0
                && Objects.equals(name, material.name);
    }

    @Override
    public String toString() {
        return String.valueOf(name);
    }

}
