package net.vpc.scholar.hadruwaves.mom.console.yaxis;

import net.vpc.scholar.hadruplot.console.params.YAxisType;

public class MoMYAxisType extends YAxisType {
    public static final MoMYAxisType Z_DIRECT = new MoMYAxisType("Z_DIRECT");
    public static final MoMYAxisType Z_MODELE = new MoMYAxisType("Z_MODELE");
    public static final MoMYAxisType Z_ERREUR = new MoMYAxisType("Z_ERREUR");
    public static final MoMYAxisType S_DIRECT = new MoMYAxisType("S_DIRECT");
    public static final MoMYAxisType S_MODELE = new MoMYAxisType("S_MODELE");
    public static final MoMYAxisType S_ERREUR = new MoMYAxisType("S_ERREUR");
    public static final MoMYAxisType J_ESSAI_ALL = new MoMYAxisType("J_ESSAI_ALL");
    public static final MoMYAxisType J_ESSAI_DIRECT = new MoMYAxisType("J_ESSAI_DIRECT");
    public static final MoMYAxisType J_ESSAI_MODELE = new MoMYAxisType("J_ESSAI_MODELE");
    public static final MoMYAxisType J_BASE_ALL = new MoMYAxisType("J_BASE_ALL");
    public static final MoMYAxisType J_BASE_DIRECT = new MoMYAxisType("J_BASE_DIRECT");
    public static final MoMYAxisType J_BASE_MODELE = new MoMYAxisType("J_BASE_MODELE");
    public static final MoMYAxisType E_BASE_DIRECT = new MoMYAxisType("E_BASE_DIRECT");
    public static final MoMYAxisType E_BASE_MODELE = new MoMYAxisType("E_BASE_MODELE");
    public static final MoMYAxisType E_BASE_HALF_SANS_HALH_AVEC_COUPLAGE_DIRECT = new MoMYAxisType("E_BASE_HALF_SANS_HALH_AVEC_COUPLAGE_DIRECT");
    public static final MoMYAxisType E_BASE_HALF_SANS_HALH_AVEC_COUPLAGE_MODELE = new MoMYAxisType("E_BASE_HALF_SANS_HALH_AVEC_COUPLAGE_MODELE");
    public static final MoMYAxisType E_BASE_SANS_COUPLAGE_DIRECT = new MoMYAxisType("E_BASE_SANS_COUPLAGE_DIRECT");
    public static final MoMYAxisType E_BASE_SANS_COUPLAGE_MODELE = new MoMYAxisType("E_BASE_SANS_COUPLAGE_MODELE");
    public static final MoMYAxisType E_BASE_COUPLAGE_SEUL_DIRECT = new MoMYAxisType("E_BASE_COUPLAGE_SEUL_DIRECT");
    public static final MoMYAxisType E_BASE_COUPLAGE_SEUL_MODELE = new MoMYAxisType("E_BASE_COUPLAGE_SEUL_MODELE");

    protected MoMYAxisType(String name) {
        super(name);
    }
}
