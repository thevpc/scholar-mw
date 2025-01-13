package net.thevpc.scholar.hadruwaves.str;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public enum MWParamType {

    //this field should not be serialized
    TRANSIENT,
    // this field should never be used in daughter structures based on this (in fractal mode)
    PRIVATE,
    // this field should be used in daughter structures based on this (in fractal mode)
    PUBLIC,
    // this field has significance only for daughter structures
    PROTECTED,
}
