package net.vpc.scholar.hadrumaths;

/**
* Created by vpc on 4/28/14.
*/
public enum InverseStrategy {

    ADJOINT,
    SOLVE,
    GAUSS,
    BLOCK_OJALGO,
    BLOCK_ADJOINT,
    BLOCK_SOLVE,
    BLOCK_GAUSS,
    OJALGO,
    DEFAULT, //equivalent to BLOCK_OJALGO
}
