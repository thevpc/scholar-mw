package net.thevpc.scholar.hadruwaves.mom;

/**
 * Enumeration of Method of Moments (MoM) solver formulations supported by Hadruwaves.
 */
public enum MomSolverType {
    /**
     * Cavity-backed spectral modal expansion (the classical Hadruwaves engine).
     * Solves the structure in a shielded cavity box using waveguide modes.
     * Optimal and fast for rectangular structures using entire-domain sinusoidal
     * basis functions (e.g. UserSinePattern).
     */
    CAVITY_MODAL,

    /**
     * Spatial-domain Mixed-Potential Integral Equation (MPIE).
     * Solves open-boundary planar microstrip structures using RWG triangular meshes,
     * analytical Wilton potential singularity extraction, and ground-plane image theory.
     * Optimal and robust for arbitrary geometries (inset notches, corporate feeds, bends, T-junctions).
     */
    SPATIAL_MPIE
}
