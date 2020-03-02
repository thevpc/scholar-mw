package net.vpc.scholar.hadruwaves;

import java.util.Objects;

public class Material {
    public static final Material VACUUM = new Material("Vacuum", 1, 1, 0);
    public static final Material PEC = new Material("PEC", Double.POSITIVE_INFINITY, 1, Double.POSITIVE_INFINITY);
    public static final Material PMC = new Material("PMC", Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
    public static final Material PERIODIC_FACE = new Material("PERIODIC", Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN);
    public static final Material INFINITE_FACE = new Material("INFINITE_FACE", Double.POSITIVE_INFINITY, Double.NaN, Double.NaN);
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
        return substrate("substrate(" + epsr + ")", epsr);
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Material material = (Material) o;
        return Double.compare(material.permettivity, permettivity) == 0 &&
                Double.compare(material.permeability, permeability) == 0 &&
                Double.compare(material.electricConductivity, electricConductivity) == 0 &&
                Objects.equals(name, material.name);
    }
}
